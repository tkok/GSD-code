package dk.itu.kben.gsd.domain;

import dk.itu.scas.gsd.utils.SensorValueCache;

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
	}
}
