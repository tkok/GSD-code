package dk.itu.kben.gsd;

import java.util.Iterator;
import java.util.List;

import dk.itu.scas.gsd.net.Connection;

public class Testing {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String [] args) throws Exception{
		String sensorId = "room-2-light-5-state";
		String query = "http://127.0.0.1:8000/api/user/measurement/?uuid="+sensorId+"&bid=1&limit=2&format=json";
		Object data = Connection.getSensorValue(query);
		System.out.println(data.toString());
		System.out.println(Connection.setSensorValue(sensorId, 1));
		System.out.println(Connection.getSensorValue(query).toString());
		List<String> sensors = Connection.getSensorListByRoomId("room-6-");
		Iterator iterator = sensors.iterator();
		while(iterator.hasNext()){
			System.out.println((String) iterator.next());
		}
	}
} 