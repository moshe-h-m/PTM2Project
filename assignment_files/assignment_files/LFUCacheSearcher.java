package test;

import java.util.*;

public class LFUCacheSearcher extends IOSearcher implements CacheSearcher{

    private LinkedHashMap<String, String> LFUmyres;
    public int cacheSize;
    Set<Result> setOfResults = new HashSet<>();
    public LFUCacheSearcher(CacheSearcher cacheSearcher, int cacheSize) {
        super();
        this.LFUmyres = new LinkedHashMap<>(cacheSize,1.00f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > cacheSize;
            }
        };

        if (cacheSize <= 0) {
            throw new IllegalArgumentException("Cache size must be positive");
        }
        this.cacheSize = cacheSize;
    }
    @Override
    public Set<Result> getCachedResults() {

        int i = 0;
        for (Map.Entry<String, String> stringResultEntry : LFUmyres.entrySet()) {

            System.out.println("result number: "+(++i)+"");
            System.out.println("the query: "+stringResultEntry.getKey());
            System.out.println("the answer: "+stringResultEntry.getValue().charAt(0));


        }
        return setOfResults;
    }

    @Override
    public void clear() {

    }

    @Override
    public void remove(Result r) {
        LFUmyres.remove(r.getQuery());
    }

    @Override
    public Result search(String text, String rootPath) {
        Result result = null;
        Integer frequency = 1;
        String numInString = frequency.toString();
        if(LFUmyres.containsKey(text)){
            numInString = (LFUmyres.get(text).charAt(0))+"";
            frequency = Integer.parseInt(numInString);
            frequency++;
            numInString = frequency.toString();

            LFUmyres.put(text,numInString + LFUmyres.get(text).substring(1));
        }
        else if(LFUmyres.size() == cacheSize){
            for (Map.Entry<String, String> stringResultEntry : LFUmyres.entrySet()) {
                if(stringResultEntry.getValue().charAt(0) == '1'){
                    LFUmyres.remove(stringResultEntry.getKey());
                    break;
                }
            }
            result = super.search(text, rootPath);
            LFUmyres.put(text,numInString + result.getAnswer().toString());
            setOfResults.add(result); // add to set of results

        }else {
            result = super.search(text, rootPath);
            LFUmyres.put(text,numInString + result.getAnswer().toString());
            setOfResults.add(result); // add to set of results
        }

        return result;
    }
}
