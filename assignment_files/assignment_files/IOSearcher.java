package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class IOSearcher implements TextSearcher{
    public static int IOnumColumans = 0;
    File directory;

    String QueryString;

    HashMap<String,String> IOmyres = new HashMap<String,String>();

    Result myResult = new Result() {


        @Override
        public String getQuery() {
            return QueryString;
        }

        @Override
        public HashMap<String, String> getAnswer() {
            return IOmyres;
        }

        @Override
        public int getNumOfColumns() {
            return IOnumColumans;
        }

    };


//    public IOSearcher() {
//
//    }


    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    @Override
    public Result search(String text, String rootPath) {
        File dir = directory(rootPath);
        //HashMap<String, String> myres = new HashMap<>();
        this.QueryString =text;
        if(dir.listFiles().length==0){
            System.out.println("the directory is empty");
        }else
            System.out.println("num of files in directory "+dir.listFiles().length);
        for(File file : dir.listFiles()){

            int dot = file.toString().lastIndexOf(".");
            int slash = file.toString().lastIndexOf("\\");
            System.out.println("the place of dot " + dot +" the place of slash "+ slash);
            if(dot < slash){
                search(text, file.toString());
            }
            if(file.toString().endsWith(".txt")){
                searchSimple(text,file);

            }else
                System.out.println("not txt file");
            System.out.println(file);
        }
        return this.myResult;
    }

    private File directory(String rootPath) {
        File dir = new File(rootPath);
        return dir;
    }

    public Result searchSimple(String text, File file) {
        //HashMap<String, String> myres = new HashMap<>();
        boolean found = false;
        String lines = "";
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String lineStr = scanner.nextLine();
                if(lineStr.indexOf(text+" ")!=(-1)){
                    lines += lineStr;
                    found = true;
                    IOnumColumans += 1;
                    //System.out.println(lineStr);
                }

            }

            scanner.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(found){
            String finalLines = lines;
            IOmyres.put(String.valueOf(file),lines);

        }

        return myResult;
    }

}

