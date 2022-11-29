package test;

import java.util.Map;
import java.util.Set;

public interface Result {
	String getQuery(); // the searched string
	// Map< file name , Set <lines the query appeared in> >
	Map<String,Set<String>> getAnswer(); 
}
