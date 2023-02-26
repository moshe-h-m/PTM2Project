package test;

import java.util.*;

public class ObservableCacheSearcher  extends IOSearcher {

    private LinkedHashMap<String, String> ObservableResults;

    List<String> changes = new ArrayList<>();
    private CacheSearcher cacheSearcher;
    private List<Observer> observers;
    Set<Result> setOfResults = new HashSet<>();

    int cacheSize;

    public ObservableCacheSearcher(CacheSearcher cacheSearcher, int cacheSize) {
        super();
        this.ObservableResults = new LinkedHashMap<>(cacheSize,1.00f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                changes.add(eldest.getKey() + " removed");
                return size() > cacheSize;
            }
        };

        if (cacheSize <= 0) {
            throw new IllegalArgumentException("Cache size must be positive");
        }
        this.cacheSize = cacheSize;
    }





    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(ObservableCacheSearcher observable, String about) {
        for (Observer observer : observers) {
            observer.update(observable, about);
        }
    }

    public Collection<Object> search(String s) {
        return null;
    }

    public interface Observer {
        void update(ObservableCacheSearcher observable, String about);
    }
    
    @Override
    public Result search(String text, String rootPath) {
        Result result = null;

        result = super.search(text, rootPath);
        ObservableResults.put(text, result.getAnswer().toString());
        //result = ObservableResults.get(text);
        setOfResults.add(result); // add to set of results
        changes.add(text + " added");
        return result;
    }
}