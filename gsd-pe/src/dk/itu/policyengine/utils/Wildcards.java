package dk.itu.policyengine.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import dk.itu.policyengine.integration.Connection;

public class Wildcards {
	private final String [] type = {"heater", "ac", "light", "blind"};
	private static final String HEATING = "heating";
	private static final String AC = "ac";
	private static final String LIGHTS = "lights";
	private static final String BLINDS = "blinds";
	private List<String> wildcards;
	private Connection connection;
	public Wildcards(){
		wildcards = new ArrayList<String>();
		connection = new Connection();
	}
	
	public List<String> getWildcards() throws IOException, URISyntaxException{
		List<String> floors = connection.getFloorIds();
		//System.out.println(floors.size());
		List<String> rooms = new ArrayList<String>();
		
		for(String s : floors){
			for(String t : type){
				wildcards.add(s.replaceAll(" ", "-")+"-"+t);
			}
		}
		return wildcards;
	}
	public List<String> getSensorListByWildcard(String wildcard) throws MalformedURLException, IOException, URISyntaxException{
		List<String> sensors = new ArrayList<String>();
		String [] data = wildcard.split("-");
		String floor = data[0]+"-"+data[1];
		System.out.println(floor);
		List<String> sensorIds = connection.getSensorListByFloor(floor);
		System.out.println(sensorIds.size());
		for(String s: sensorIds){
			if(s.contains(data[2]))
				sensors.add(s);
		}
		return sensors;
	}
	
	public static void main(String [] args) throws IOException, URISyntaxException{
		Wildcards wildcards = new Wildcards();
		List<String> list = wildcards.getWildcards();
		for(String s : list){
			System.out.println(s);
		}
		/*
		List<String> test = wildcards.getSensorListByWildcard("floor-0-ac");
		//List<String> t = new Connection().getRoomListByFloor("floor-0");
		for(String s:test){
			System.out.println(s);
		}
		*/
	}
}
