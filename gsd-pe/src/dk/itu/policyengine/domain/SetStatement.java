package dk.itu.policyengine.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.log4j.Logger;

import dk.itu.policyengine.integration.Connection;
import dk.itu.policyengine.utils.Wildcards;

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
		//SensorValueCache.setValue(sensorID, aValue);
		if(sensorID.contains("floor") && !sensorID.contains("room")){
			logger.info("It is a wildcard");
			try {
				List<String> sensors = new Wildcards().getSensorListByWildcard(sensorID);
				for(String s : sensors){
					new Connection().setSensorValue(s+"-gain", aValue.getIntValue());
				}
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
		else{
			try {
				new Connection().setSensorValue(sensorID, aValue.getIntValue());
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
}