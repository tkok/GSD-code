package dk.itu.kben.gsd;

import java.util.Hashtable;

public class BuildingDAO {

	static Hashtable<String, Value> hashtable = new Hashtable<String, Value>();
	
	public static Value getValue(String name) 
	{
		return hashtable.get(name);
	}

	public static void setValue(String name, Value value) {
		System.out.println("Setting " + name + " to " + value);		
		hashtable.put(name, value);
	}
	
	public static void FillTable()
	{
		Hashtable<String, Value> lol = BuildingDAL.GetHashtableWithStringValueFromDB("repo_measurement");
		hashtable.putAll(lol);
	}
	
	
}