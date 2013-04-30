package dk.itu.policyengine.persistence;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import dk.itu.policyengine.domain.FloatValue;

public class SensorValueCache {
	static Hashtable<String, FloatValue> hashtable = new Hashtable<String, FloatValue>();
	
	static final String statePrefix = "STATE";

	public static FloatValue getValue(String name) {
		return (FloatValue) hashtable.get(name);
	}

	public static void setValue(String name, FloatValue value) {
		final Logger logger = Logger.getLogger(SensorValueCache.class);
		
		logger.debug("Setting " + name + " to " + value);
		
		hashtable.put(name, value);
	}
	
	public static boolean containsKey(String name){
		return hashtable.contains(name);
	}
	public static void clearCache(){
		hashtable.clear();
	}
	public String toString() {
		return hashtable.toString();
	}
}