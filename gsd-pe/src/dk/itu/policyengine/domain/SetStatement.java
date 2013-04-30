package dk.itu.policyengine.domain;

import org.apache.log4j.Logger;

import dk.itu.policyengine.integration.Connection;
import dk.itu.policyengine.persistence.SensorValueCache;

public class SetStatement implements Statement {
	private transient final Logger logger = Logger.getLogger(this.getClass());
	
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
		} catch (Exception e) {
			logger.error(e);
		} 
	}
}