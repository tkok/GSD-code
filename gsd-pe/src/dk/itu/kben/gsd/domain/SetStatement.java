package dk.itu.kben.gsd.domain;

import dk.itu.kben.gsd.persistence.BuildingDAO;

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
		BuildingDAO.setValue(sensorID, aValue);
	}
}