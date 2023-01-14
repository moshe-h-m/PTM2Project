package test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class MainTrain {

	public static void printResolt(Result rs){

		System.out.println("the word for searching is: "+rs.getQuery()+"and the number of columns: "+rs.getNumOfColumns());
		for (Map.Entry<String, String> entry : rs.getAnswer().entrySet()){
			String answer = entry.getKey();
			String answerText = entry.getValue();
			System.out.println(" the key " + answer + " the value " + answerText + "");
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
//		System.out.println("your code will be checked manually after the deadline. please ignor the grade.");
//		System.out.println("done");
		System.out.println("~~~~~~~~~~~~~~ IOsercher test ~~~~~~~~~~~~~~~");
		String wordToSearch = "Egypt";
		String path ="C:\\Users\\mhm23\\Desktop\\CS Y4 23\\S A 23\\PTM2\\demo1\\src\\main\\java\\com\\projectAssignment\\search_folder\\search_folder";
		File myafile = new File(path);
		IOSearcher isearch = new IOSearcher();
		//isearch.checkValidDir();
		Result result = isearch.search(wordToSearch, path);
		System.out.println( "num of rews: "+result.getNumOfColumns()+" the quary: "+result.getQuery()+ "\n the answer: " );
		for (Map.Entry<String, String> entry : result.getAnswer().entrySet()){
			String answer = entry.getKey();
			String answerText = entry.getValue();
			System.out.println(" the key " + answer + " the value " + answerText + "");
		}


		System.out.println("~~~~~~~~~~~~~~ Parallel IOsercher test ~~~~~~~~~~~~~~~");
		ForkJoinPool pool = new ForkJoinPool();

		ParallelIOSearcher myParllSearch = new ParallelIOSearcher(myafile, wordToSearch);
		//System.out.println("herre" + 23);
		//pool.execute(myParllSearch);
		pool.invoke(myParllSearch);
		System.out.println("**********************"+pool.getPoolSize()+"********************\n");
		System.out.println("Main: Parallelism: "+ pool.getParallelism());
		System.out.println("Main: Steal Count: "+ pool.getStealCount());
		System.out.println("******************************************\n");

		System.out.println("herre" + 25);
		//isearch = myParllSearch.MySearcher;
		System.out.println(myParllSearch.myResult.size());
		Result answer1 = (Result) myParllSearch;
		System.out.println("the word for searching is: "+answer1.getQuery()+"and the number of columns: "+answer1.getNumOfColumns());
		for (Map.Entry<String, String> entry : answer1.getAnswer().entrySet()){
			String answer = entry.getKey();
			String answerText = entry.getValue();
			System.out.println(" the key " + answer + " the value " + answerText + "");
		}
		System.out.println("\n********************** and of ParallelIOsearcher test ********************\n");

		System.out.println("********************** cacheIOSearcher test ********************");


		CacheIOSearcher IOCache =  new CacheIOSearcher();
		//Result  IOResult = IOCache.proxyIOSearch( wordToSearch, path);
		Result  IOResult1 = IOCache.proxyIOSearch( "Jon", path);
		Result IOResult2 = IOCache.proxyParallelSearch(wordToSearch, path);
		System.out.println( "printing results for IOCache test");
		MainTrain.printResolt(IOResult2);
//		CacheIOSearcher ParallelCache = new CacheIOSearcher();
//		Result ParallelResult = ParallelCache.proxyIOSearch(wordToSearch, path);
//		System.out.println( "printing results for ParallelCache test");
//		MainTrain.printResolt(ParallelResult);
		System.out.println("\n\n@@@@@@@@@@@@@@@@@@@@ check if inserted to set @@@@@@@@@@@@@@@@@@@@");
		IOCache.getCachedResults();

	}



}
