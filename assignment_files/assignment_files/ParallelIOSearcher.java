package test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;


public class ParallelIOSearcher  extends RecursiveTask<HashMap<String, String>> implements Result{
    private File file = null;
    private String word;

    HashMap<String,String> myResult = new HashMap<>();

    public static int PnumColumans;


    public ParallelIOSearcher(File file, String word) {
        this.file = file;
        this.word = word;

    }

    public ParallelIOSearcher() {

    }

    @Override
    protected HashMap compute() {

        List<ParallelIOSearcher> tasks = new ArrayList<ParallelIOSearcher>();

        File[] contentOfFile = this.file.listFiles();


        if(contentOfFile.length > 0 ) {

            for( File file : contentOfFile) {
                if(file.isDirectory()){
                    ParallelIOSearcher task = new ParallelIOSearcher(file, this.word);
                    task.fork();
                    tasks.add(task);

                } else {

                    if(checkFile(file.toString())) {
                        String line = searchIn(file, this.word);
                        if(line != null) {
                            myResult.put(file.toString(), line);
                        }
                    }
                }
            }
        } else {
            System.out.println("empty file");
        }
        for(ParallelIOSearcher parall : tasks)
        {
            HashMap<String, String> mapi = parall.join();
            myResult.putAll(mapi);
        }


        return myResult;
    }


    private boolean checkFile(String name) {
        return name.endsWith("txt");
    }

    public String  searchIn(File file, String word) {
        //MySearcher.searchSimple(word, file);
        boolean ifline = false;
        String lines = "";
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String lineStr = scanner.nextLine();
                if (lineStr.indexOf(word) != (-1)) {
                    lines += lineStr;
                    ifline = true;
                    PnumColumans += 1;
                    //System.out.println(lineStr);
                }

            }

            scanner.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String finalLines = null;
        if (ifline) {
            finalLines = lines;
        }
        return finalLines;
    }



    @Override
    public String getQuery() {
        return this.word;
    }

    @Override
    public HashMap<String, String> getAnswer() {
        return myResult;
    }

    @Override
    public int getNumOfColumns() {
        return PnumColumans;
    }
}



