package dk.itu.kben.gsd;

public class SetStatement implements Statement {
	
	Value aValue;
	
	String sensorID = "";
	
	public SetStatement(String sensorId, Value aValue) {
		this.sensorID = sensorId;
		this.aValue = aValue;
	}
	
	public void execute() {
		BuildingDAO.setValue(sensorID, aValue);
	}
}
