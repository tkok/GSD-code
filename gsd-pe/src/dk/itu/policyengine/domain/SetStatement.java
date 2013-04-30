package dk.itu.policyengine.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import dk.itu.policyengine.integration.Connection;
import dk.itu.policyengine.persistence.SensorValueCache;

public class SetStatement implements Statement {
	
	FloatValue aValue;
	
	String sensorID = "";
	
	public SetStatement() {
	}
	
	public SetStatement(String sensorId, FloatValue aValue) {
		this.sensorID = sensorId;
		this.aValue = aValue;
	}
	
	public void execute() {
		SensorValueCache.setValue(sensorID, aValue);
		try {
			new Connection().setSensorValue(sensorID, aValue.getIntValue());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
