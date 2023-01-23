package test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class CacheIOSearcher implements CacheSearcher {

    public HashMap<String, Integer> colNumMap = new HashMap<>();
    int numOfColumnsInPath;
    private IOSearcher ioSercher =  new IOSearcher();

    private Result parallelSearcher =  new ParallelIOSearcher();
    public static Set<Result> setOfResults = new HashSet<Result>();

    Result getResult;
    @Override
    public Set<Result> getCachedResults() {
        System.out.println("nothing");
        for (Result result : setOfResults){
            System.out.println("the query: "+result.getQuery());
            System.out.println("the result: "+result.getAnswer());
            System.out.println("the num   of rows: "+result.getNumOfColumns());
        }
        return setOfResults;
    }
//        Iterator data = setOfResults.iterator();
//        while (data.hasNext()) {
//            System.out.println( data.next());
//            Result result = (Result) data.next();
//            System.out.println("the query: "+result.getQuery());
//            System.out.println("the answer: "+result.getAnswer());
//      }
//        return null;
//    }

    @Override
    public void clear() {
        setOfResults.clear();

    }

    @Override
    public void remove(Result r) {
        setOfResults.remove(r);

    }

    @Override
    public Result search(String text, String rootPath) {
        return null;
    }

    public Result proxyIOSearch(String text, String rootPath) throws FileNotFoundException {
        Boolean isFind = false;
        for (Result result: setOfResults){
            if (result.getQuery().equals(text)) {
                ioSercher.myResult = result;
                isFind = true;
            }
        }
        if (isFind  ) {
            return ioSercher.myResult;
        } else if((!colNumMap.containsKey(rootPath))||(colNumMap.get(rootPath)<(howManyColumns(rootPath)))){
            getResult = ioSercher.search(text, rootPath);
            setOfResults.add(getResult);
            return getResult;
        }else {
            clear();
            getResult = ioSercher.search(text, rootPath);
            setOfResults.add(getResult);
            return getResult;
        }
    }

    public Result proxyParallelSearch(String text, String rootPath) throws FileNotFoundException {
        Boolean isFind = false;
        for (Result result: setOfResults){
            if (result.getQuery().equals(text)) {
                parallelSearcher = result;
                isFind = true;
            }
        }
        if (isFind  ) {
            return parallelSearcher;
        } else if((!colNumMap.containsKey(rootPath))||(colNumMap.get(rootPath)<(howManyColumns(rootPath)))){
            ForkJoinPool pool = new ForkJoinPool();

            ParallelIOSearcher myParallelSearch = new ParallelIOSearcher(new File(rootPath), text);
            pool.invoke(myParallelSearch);
            getResult = (Result) myParallelSearch;
            setOfResults.add(getResult);
            addColumnToMap(rootPath,getResult.getNumOfColumns());
            return getResult;
        }else {
            clear();
            ForkJoinPool pool = new ForkJoinPool();

            ParallelIOSearcher myParallelSearch = new ParallelIOSearcher(new File(rootPath), text);
            pool.invoke(myParallelSearch);
            getResult = (Result) myParallelSearch.myResult;
            setOfResults.add(getResult);
            addColumnToMap(rootPath,getResult.getNumOfColumns());
            return getResult;
        }
    }

    public int howManyColumns(String path) throws FileNotFoundException {
        int count = 0;
        File file = new File(path);
        if(colNumMap.containsKey(path)){
            return (int)colNumMap.get(path);
        }else if (file.length()==0) {
            return 0;
        }else {
            for(File specificFile : file.listFiles()) {
                if (specificFile.isDirectory()) {
                    howManyColumns(specificFile.toString());
                }else if (specificFile.toString().endsWith(".txt")){
                    Scanner sc = new Scanner(specificFile);

                    while(sc.hasNextLine()) {
                        sc.nextLine();
                        count++;
                    }
                    sc.close();
                }
            }
            return count;
        }
    }

//    public void cleanIfFull(Set<Result> set, String rootPath) {
//        Iterator data = set.iterator();
//        while (data.hasNext()) {
//
//        }
//    }

    public int addColumnToMap(String rootPath, int col){
        int oldNum =0;
        if (colNumMap.get(rootPath)!=null) {
            oldNum = (int)colNumMap.get(rootPath);
        };
        colNumMap.put(rootPath, col + oldNum);
        return col + oldNum;
    }

}

