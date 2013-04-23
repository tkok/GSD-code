package dk.itu.scas.gsd.utils;

import java.util.Hashtable;

import dk.itu.kben.gsd.domain.Value;

import dk.itu.nicl.gsd.log.Log;

public class SensorValueCache {

	static Hashtable<String, Value> hashtable = new Hashtable<String, Value>();

	public static Value getValue(String name) {
		return hashtable.get(name);
	}

	public static void setValue(String name, Value value) {
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