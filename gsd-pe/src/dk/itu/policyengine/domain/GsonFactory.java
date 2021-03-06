package dk.itu.policyengine.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory {
	
	static Gson gson = null;
	
	public static Gson getInstance() {
		if (gson == null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			
			gsonBuilder.registerTypeAdapter(Statement.class, new InterfaceAdapter<Statement>());
			gsonBuilder.registerTypeAdapter(Value.class, new InterfaceAdapter<Value>());
			gsonBuilder.registerTypeAdapter(Interval.class, new IntervalAdapter());
			
			gson = gsonBuilder.create();
		}
		
		return gson;
	}
}