package dk.itu.scas.gsd.net;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServiceProperties {
	
	private final static String [] LIGHT = {"wattage","efficiency","gain_input","luminosity","production","state"};
	private final static String [] AC = {"efficiency","wattage","gain_input","production","state","gain_output"};
	private final static String [] HEATER = {"efficiency","wattage","gain_input","production","state","gain_output"};
	private final static String [] WATER = {"efficiency","gain_input","flow","production","state","gain_output"};
	private final static String [] BLINDS = {"speed","size","setpoint"}; 
	
	/**
	 * Return a list with each sensor's properties, given as input a list of sensor's id. 
	 * @param sensorIds
	 * @return
	 */
	public static List<String> allSensorsWithProperties(List<String> sensorIds){
		List<String> list = new ArrayList<String>();
		Iterator<String> iterator = sensorIds.iterator();
		while(iterator.hasNext()){
			String id = iterator.next();
			if(id.contains("light")){
				for(String s: LIGHT){
					list.add(id+"-"+s);
				}
			}
			else if(id.contains("ac")){
				for(String s: AC){
					list.add(id+"-"+s);
				}
			}
			else if(id.contains("heater")){
				for(String s: HEATER){
					list.add(id+"-"+s);
				}
			}
			else if(id.contains("water")){
				for(String s: WATER){
					list.add(id+"-"+s);
				}
				
			}
			else if(id.contains("blind")){
				for(String s: BLINDS){
					list.add(id+"-"+s);
				}
			}
		}
		
		return list;
	}
	public List<String> getLightProperties(){
		List<String> list = new ArrayList<String>();
		for(String s : LIGHT )
			list.add(s);
		return list;
	}
	
	public List<String> getAcProperties(){
		List<String> list = new ArrayList<String>();
		for(String s : AC )
			list.add(s);
		return list;
	}
	public List<String> getHeaterProperties(){
		List<String> list = new ArrayList<String>();
		for(String s : HEATER )
			list.add(s);
		return list;
	}
	public List<String> getWaterProperties(){
		List<String> list = new ArrayList<String>();
		for(String s : WATER )
			list.add(s);
		return list;
	}
	public List<String> getBlindsProperties(){
		List<String> list = new ArrayList<String>();
		for(String s : BLINDS )
			list.add(s);
		return list;
	}
}