package test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ObservableCacheSearcher  extends IOSearcher {

    private LinkedHashMap<String, String> ObservableResults;

    private CacheSearcher cacheSearcher;
    private List<Observer> observers;

    public ObservableCacheSearcher(CacheSearcher cacheSearcher) {
        this.cacheSearcher = cacheSearcher;
        this.observers = new ArrayList<>();
    }

    public List<Result> search(String query) {
        List<Result> results = cacheSearcher.search(query);
        List<String> changes = new ArrayList<>();
        for (Result result : results) {
            if (result.isNew()) {
                cacheSearcher.addResult(result);
                changes.add(query + " added");
            } else if (result.isRemoved()) {
                cacheSearcher.removeResult(result);
                changes.add(query + " removed");
            }
        }
        if (!changes.isEmpty()) {
            String about = String.join("; ", changes);
            notifyObservers(this, about);
        }
        return results;
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
        return result;
    }
}