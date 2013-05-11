package dk.itu.policyengine.domain;

public interface Statement {
	
	public void execute();
	
	public void execute(String sensorId);

}