package dk.itu.scas.gsd.utils;

import java.util.Hashtable;

import dk.itu.kben.gsd.domain.FloatValue;
import dk.itu.kben.gsd.domain.Value;
import dk.itu.nicl.gsd.log.Log;

public class SensorValueCache {

	static Hashtable<String, FloatValue> hashtable = new Hashtable<String, FloatValue>();

	public static FloatValue getValue(String name) {
		return (FloatValue) hashtable.get(name);
	}

	public static void setValue(String name, FloatValue value) {
		Log.log("Setting " + name + " to " + value);
		System.out.println("Setting " + name + " to " + value);
		
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