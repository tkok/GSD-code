package dk.itu.policyengine.integration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dk.itu.nicl.gsd.log.Log;

public class ServiceProperties {
	
	private final static String [] PROPS =  {"gain","production","state"};
	/*private final static String [] LIGHT = {"gain","production","state"};
	private final static String [] AC = {"gain","production","state"};
	private final static String [] HEATER =  {"gain","production","state"};
	private final static String [] WATER =  {"gain","production","state"};*/
	private final static String [] BLINDS = {"setpoint","state"};
	
	/**
	 * Return a list with each sensor's properties, given as input a list of sensor's id. 
	 * @param sensorIds
	 * @return
	 */
	public static List<String> allSensorsWithProperties(List<String> sensorIds){
		List<String> list = new ArrayList<String>();
		Iterator iterator = sensorIds.iterator();
		while(iterator.hasNext()){
			String id = (String) iterator.next();
			System.out.println(id.trim());
			Log.log(id.trim());
			if(id.contains("blind")){
				for(String s : BLINDS)
					list.add(id+"-"+s.toString());
			}
			else{
				for(String s : PROPS)
					list.add(id+"-"+s.toString());
			}
		}
		return list;
	}
	public static List<String> allSensorsWithProperties(String id){
		List<String> list = new ArrayList<String>();
		if(id.contains("blind")){
			for(String s : BLINDS)
				list.add(id+"-"+s.toString());
		}
		else{
			for(String s : PROPS)
				list.add(id+"-"+s.toString());
		}
		return list;
	}
}