package dk.itu.policyengine.integration;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dk.itu.nicl.gsd.log.Log;
import dk.itu.policyengine.servlet.Configuration;

public class Connection {
	// variables
	private String [] services = {"lights", "acs", "heaters", "blinds", "waters"};
	//private static final String BUILDING_INFO = "api/user/building/entry/description/1/?format=json";
	//private static final String BUILDING_INFO_LOCAL = "http://127.0.0.1:8000/api/user/building/entry/description/1/?format=json";
	//private static final String SET_SENSOR_VALUE = "api/user/building/entry/set/1/";
	//private static final String SET_SENSOR_VALUE_LOCAL= "http://127.0.0.1:8000/api/user/building/entry/set/1/";
	private static final String file = "sensor.json";
	private final Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Get list of sensors. Return a list of sensors id's
	 * @return List<String> sensor id's
	 */
	public Connection(){
		
	}
	public List<String> getSensorIds(){ 
		List<String> sensors = new ArrayList<String>();
		try {
			String data = "";
			if(Configuration.getServer() != null){
				data = connect(Configuration.getServer() + Configuration.getBuilding() + Configuration.getFormat());
			}
			else
				data = connect("http://gsd.itu.dk/api/user/building/entry/description/1/?format=json");
			JSONObject jsonObject = new JSONObject(data.toString());
			JSONObject value = jsonObject.getJSONObject("value");
			JSONObject rooms = value.getJSONObject("rooms");
			String [] s = rooms.getNames(rooms);
			// parse the json object by retrieving the keys of a room
			for(String str : s){
				JSONObject room = rooms.getJSONObject(str);
				for(String string : services){
					JSONObject obj = room.getJSONObject(string);
					Iterator iterator = obj.keys();
					while(iterator.hasNext()){
						sensors.add(iterator.next().toString());
						//System.out.println(iterator.next().toString());
					}
				}
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sensors;
	}
	public List<String> getSensorIds(String url){ 
		List<String> sensors = new ArrayList<String>();
		try {
			String data = connect(url);
			JSONObject jsonObject = new JSONObject(data.toString());
			JSONObject value = jsonObject.getJSONObject("value");
			JSONObject rooms = value.getJSONObject("rooms");
			String [] s = rooms.getNames(rooms);
			// parse the json object by retrieving the keys of a room
			for(String str : s){
				JSONObject room = rooms.getJSONObject(str);
				for(String string : services){
					JSONObject obj = room.getJSONObject(string);
					Iterator iterator = obj.keys();
					while(iterator.hasNext()){
						sensors.add(iterator.next().toString());
						//System.out.println(iterator.next().toString());
					}
				}
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sensors;
	}
	/**
	 * Connect to the simulator's api and get a JSON object.
	 * @param url
	 * @return Returns a JSON object as a String
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public String connect(String url) throws MalformedURLException, URISyntaxException {
		StringBuffer bufferString = new StringBuffer();
		try{
			System.out.println("Trying to connect to "+url);
			logger.info("Trying to fetch data from "+url);
			URL _url = new URL(url);
			URLConnection urlConnection = _url.openConnection();
			urlConnection.setReadTimeout(Configuration.getTimeout());
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
						String line;
			while((line = bufferedReader.readLine()) !=null){
				bufferString.append(line);
			}
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.log("Socket timeout exception");
			logger.info("Server is not responding");
			System.out.println("Socket timeout exception");
		}
			return bufferString.toString();
	}
	/**
	 * Query the simulator on a specific sensor for the current value.
	 * @param query
	 * @return Returns an Object - this should actually be the value of the sensor's property
	 * @throws Exception
	 */
	public String getSensorValue(String sensorId) {
		String value = "";
		
		String query = Configuration.getServer() + "api/user/measurement/?bid=1&uuid="+sensorId+"&order_by=-timestamp&format=json&limit=1";
		try {
			String data = "";
			
			try {
				data = connect(query);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String data = readFromFile("query_sensor.json");
			if(data != ""){
				JSONObject jsonObject = new JSONObject(data);
				JSONArray objects = jsonObject.getJSONArray("objects");
				for(int i=0;i<objects.length();i++){
					JSONObject jsonobject = objects.getJSONObject(i);
					//System.out.println(jsonobject.getInt("bid"));
					if(jsonobject.getInt("bid")==1){
						value = jsonobject.get("val").toString();
						//value = Double.valueOf(s);
						//System.out.println("sensor "+query+" had value "+object.toString()+" at "+jsonobject.getString("timestamp"));
						break;
					}
				}
				}
			}
		catch (JSONException e) {
			System.out.println("JSON Exception");
		}
		
		return value;
		
	}
	/**
	 * Read from file. Just for testing purposes when the simulator's server is not working. 
	 * @return Returns a String in which there is a JSON.
	 * @throws IOException
	 */
	public String readFromFile(String filename) throws IOException{
		StringBuffer buffer = new StringBuffer();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		while((line=br.readLine())!=null){
			buffer.append(line);
		}
		br.close();
		return buffer.toString();
	}
	/**
	 * Set a sensor value by using the sensor's Id.
	 * @param sensorId
	 * @param d
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public boolean setSensorValue(String sensorId, int d) throws MalformedURLException, IOException, URISyntaxException{
		String data = connect(Configuration.getServer() + Configuration.getSetvalue() + sensorId+"/"+String.valueOf(d)+"/?format="+Configuration.getFormat());
		/*JSONObject jsonObject = new JSONObject(data);
		Object response = jsonObject.getJSONObject("value").get("returnvalue");
		if(jsonObject.getJSONObject("value").get("returnvalue").toString().equals("true"))
			return true;
		else
			return false;
		*/
		Log.log(data);
		System.out.println(data);
		return true;
	}
	/**
	 * Get a list of sensors ids by the room id.
	 * @param roomId
	 * @return A list with the sensors ids.
	 */
	public List<String> getSensorListByRoomId(String roomId){
		List<String> sensors = getSensorIds();
		List<String> sensorList = new ArrayList<String>();
		Iterator iterator = sensors.iterator();
		while(iterator.hasNext()){
			String id = (String) iterator.next();
			if(id.contains(roomId+"-")){
				sensorList.add(id);
			}
		}
		return sensorList;
	}
	
	public List<String> getAllSensorIdsByType(String type){
		List<String> ids = new ArrayList<String>();
		List<String> requestedIds = new ArrayList<String>();
		ids = getSensorIds();
		for(String id : ids){
			if(id.contains(type)){
				requestedIds.add(id);
			}
		}
		return requestedIds;
	}
	public List<String> getSensorListByRoomIdAndType(String roomId,String sensor, String url){
		List<String> sensors = getSensorIds(url);
		List<String> sensorList = new ArrayList<String>();
		for(String id : sensors){
			if(id.contains(roomId) && id.contains(sensor)){
				sensorList.add(id);
			}
		}
		return sensorList;
	}
	public List<String> getRoomListByFloor(String floor) throws MalformedURLException, IOException, URISyntaxException{
		List<String> roomList = new ArrayList<String>();
		String data = connect(Configuration.getServer() + Configuration.getBuilding() + Configuration.getFormat());
		JSONObject jsonObject = new JSONObject(data);
		JSONObject value = jsonObject.getJSONObject("value");
		JSONObject rooms = value.getJSONObject("rooms");
		Iterator<String> iterator = rooms.keys();
		while(iterator.hasNext()){
			String s =(String) iterator.next();
			if(s.contains(floor))
				roomList.add(s);
		}
		return roomList;
	}
	
	public List<String> getFloorIds() throws MalformedURLException, IOException, URISyntaxException{
		HashSet<String> hashSet = new HashSet<String>();
		List<String> floors = new ArrayList<String>();
		String data = connect(Configuration.getServer() + Configuration.getBuilding() + Configuration.getFormat());
		JSONObject jsonObject = new JSONObject(data);
		JSONObject value = jsonObject.getJSONObject("value");
		JSONObject rooms = value.getJSONObject("rooms");
		Iterator<String> iterator = rooms.keys();
		while(iterator.hasNext()){
			String s = iterator.next().replaceFirst("-", " ");
			hashSet.add(s.split("-")[0]);
		}
		iterator = hashSet.iterator();
		while(iterator.hasNext()){
			floors.add(iterator.next());
		}
		return floors;
	}
}
