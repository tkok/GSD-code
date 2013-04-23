package dk.itu.kben.gsd.domain;

import dk.itu.kben.gsd.persistence.BuildingDAO;
import dk.itu.scas.gsd.utils.SensorValueCache;

public class SetStatement implements Statement {
	
	Value aValue;
	
	String sensorID = "";
	
	public SetStatement() {
	}
	
	public SetStatement(String sensorId, Value aValue) {
		this.sensorID = sensorId;
		this.aValue = aValue;
	}
	
	public void execute() {
		SensorValueCache.setValue(sensorID, aValue);
	}
}
