package test;

import java.util.HashMap;

public interface Result {




	String getQuery();// the searched string
	// Map< file name , Set <lines the query appeared in> >
	HashMap<String,String> getAnswer();

	int getNumOfColumns();
}
