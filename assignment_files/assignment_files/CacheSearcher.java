package test;

import java.util.Set;

public interface CacheSearcher extends TextSearcher{
	Set<Result> getCachedResults();
	void clear();
	void remove(Result r);
}
