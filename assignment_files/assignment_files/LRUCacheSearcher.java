package test;

import java.util.*;
import java.util.Map.Entry;

public class LRUCacheSearcher extends IOSearcher implements CacheSearcher {

    private LinkedHashMap<String, String> LRUmyres;
    public int cacheSize;
    Set<Result> setOfResults = new HashSet<>();
    public LRUCacheSearcher(CacheSearcher cacheSearcher, int cacheSize) {
        super();
        this.LRUmyres = new LinkedHashMap<>(cacheSize,1.00f, true){
            @Override
            protected boolean removeEldestEntry(Entry<String, String> eldest) {
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
        for (Entry<String, String> stringResultEntry : LRUmyres.entrySet()) {

            System.out.println("result number: "+(++i)+"");
            System.out.println("the query: "+stringResultEntry.getKey());
            System.out.println("the answer: "+stringResultEntry.getValue());


        }
        return setOfResults;
    }

    @Override
    public void clear() {

    }

    @Override
    public void remove(Result r) {
        LRUmyres.remove(r.getQuery());
    }

    @Override
    public Result search(String text, String rootPath) {
        Result result = null;

        result = super.search(text, rootPath);
        LRUmyres.put(text, result.getAnswer().toString());
        setOfResults.add(result);
        return result;
    }
}
