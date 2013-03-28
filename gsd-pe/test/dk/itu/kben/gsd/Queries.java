package dk.itu.kben.gsd;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import dk.itu.scas.gsd.net.Connection;

public class Queries {
	String sensorId = "room-2-light-5-state";
	String query = "http://127.0.0.1:8000/api/user/measurement/?uuid="+sensorId+"&bid=1&limit=1&format=json";
	@Test
	public void test() throws Exception {
		assertEquals(126, Connection.getSensorIds().size());
		System.out.println(Connection.setSensorValue(sensorId, 1));
		Assert.assertEquals(1, Connection.getSensorValue(query));
	}	
}
