package dk.itu.scas.gsd.net;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Connection {
	// variables
	private static String [] services = {"lights", "acs", "heaters", "blinds", "waters"};
	private static final String BUILDING_INFO = "http://gsd.itu.dk/api/user/building/entry/description/1/?format=json";
	private static final String file = "sensor.json";
	
	/**
	 * Get list of sensors. Return a list of sensors id's
	 * @return List<String> sensor id's
	 */
	public static List<String> getSensorIds(){
		List<String> sensors = new ArrayList<String>();
		try {
			String data = connect(BUILDING_INFO);
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
	 */
	public static String connect(String url) throws MalformedURLException, IOException {
		URL _url = new URL(url);
		URLConnection urlConnection = _url.openConnection();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		StringBuffer bufferString = new StringBuffer();
		String line;
		while((line = bufferedReader.readLine()) !=null){
			bufferString.append(line);
		}
		bufferedReader.close();
		return bufferString.toString();
	}
	/**
	 * Query the simulator on a specific sensor for the current value.
	 * @param query
	 * @return Returns an Object - this should actually be the value of the sensor's property
	 * @throws Exception
	 */
	public static Object querySimulator(String query) throws Exception{
		String data = connect(query);
		//String data = readFromFile("query_sensor.json");
		Object object = new Object();
		JSONObject jsonObject = new JSONObject(data);
		JSONArray objects = jsonObject.getJSONArray("objects");
		for(int i=0;i<objects.length();i++){
			JSONObject jsonobject = objects.getJSONObject(i);
			//System.out.println(jsonobject.getInt("bid"));
			if(jsonobject.getInt("bid")==0){
				object = jsonobject.get("val");
				//System.out.println("sensor "+query+" had value "+object.toString()+" at "+jsonobject.getString("timestamp"));
				break;
			}
		}
		return object;
		
	}
	/**
	 * Read from file. Just for testing purposes when the simulator's server is not working. 
	 * @return Returns a String in which there is a JSON.
	 * @throws IOException
	 */
	public static String readFromFile(String filename) throws IOException{
		StringBuffer buffer = new StringBuffer();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		while((line=br.readLine())!=null){
			buffer.append(line);
		}
		br.close();
		return buffer.toString();
	}
}
