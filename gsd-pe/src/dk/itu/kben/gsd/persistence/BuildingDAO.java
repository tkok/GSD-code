package dk.itu.kben.gsd.persistence;

import java.util.Hashtable;

import dk.itu.kben.gsd.domain.Value;
import dk.itu.nicl.gsd.log.Log;

public class BuildingDAO {

	static Hashtable<String, Value> hashtable = new Hashtable<String, Value>();

	public static Value getValue(String name) {
		return hashtable.get(name);
	}

	public static void setValue(String name, Value value) {
		Log.log("Setting " + name + " to " + value);
		System.out.println("Setting " + name + " to " + value);
		hashtable.put(name, value);
	}

	public static void fillTable() {
		Hashtable<String, Value> lol = BuildingDAL.GetHashtableWithStringValueFromDB("repo_measurement");
		hashtable.putAll(lol);
	}

	public static boolean isEmpty() {
		return (hashtable.size() == 0);
	}

	public String toString() {
		return hashtable.toString();
	}
}