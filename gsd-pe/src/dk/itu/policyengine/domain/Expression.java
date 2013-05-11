package dk.itu.policyengine.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.log4j.Logger;

import dk.itu.policyengine.integration.Connection;
import dk.itu.policyengine.persistence.SensorValueCache;
import dk.itu.policyengine.utils.Wildcards;

public class Expression {
	private transient Logger logger = Logger.getLogger(this.getClass());
	private boolean wildcard = false;
	
	// The current expression language supports only AND between expressions inside an IF.
	LogicalOperator prefixOperator = LogicalOperator.AND;

	Value aValue = null;
	
	Operator operator = null;
	
	String sensorId = null;
	
	public Expression() {
	}
	
	public Expression(String sensorId, Operator operator, Value someValue) {
		this.sensorId = sensorId;
		this.operator = operator;
		this.aValue = someValue;
	}
	
	public void setPrefixOperator(LogicalOperator prefixOperator) {
		this.prefixOperator = prefixOperator;
	}
	/*
	public boolean evaluate() {
		if(getSensorId().contains("wildcard")){
			logger.info("This is a wildcard");
			wildcard = true;
			try {
				String [] type = getSensorId().split("-");
				List<String> sensorList = new Wildcards().getSensorListByWildcard(getSensorId());
				for(String s : sensorList){
					String id = s+"-"+type[type.length-1];
					System.out.println(id);
					SensorValueCache.setValue(id, new FloatValue(Float.parseFloat(new Connection().getSensorValue(id))));
					logger.info("Value set in sensor cache");
					return eval(id);
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
			return eval(getSensorId());
		}
		setWildcard(false);
		return false;
	}
	*/
	public boolean evaluate(){
		return eval(sensorId);
	}
	
	public String getSensorId() {
		return sensorId;
	}
	
	public boolean eval(String id){
		FloatValue sensorValue = SensorValueCache.getValue(id);
		logger.info("Value is " + sensorValue);
		if (operator == Operator.EQUALS) {
			float c = sensorValue.compareTo(aValue);
			
			logger.debug("Is " + sensorValue + " equal to " + aValue + " ? ");
			
			if (c == 0) {
				logger.debug("Yes.");
				return true;
			}
			else {
				logger.debug("No.");
			}
		}
		else {
			if (operator == Operator.LESS_THAN) {
				int c = sensorValue.compareTo(aValue);
				logger.debug("Is " + sensorValue + " less than " + aValue + " ? ");
				
				if (c < 0) {
					logger.debug("Yes.");
					return true;
				}
				else {
					logger.debug("No.");
				}
					
			}
			else {
				if (operator == Operator.GREATER_THAN) {
					int c = sensorValue.compareTo(aValue);
					logger.debug("Is " + sensorValue + " greater than " + aValue + " ? ");
					
					if (c > 0) {
						logger.debug("Yes.");
						return true;
					}
					else {
						logger.debug("No.");
					}
				}
				else {
					if (operator == Operator.NOT) {
						int c = sensorValue.compareTo(aValue);
						logger.debug("Is " + sensorValue + " different from " + aValue + " ? ");
						
						if (c != 0) {
							logger.debug("Yes.");
							return true;
						}
						else {
							logger.debug("No.");
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean isWildcard() {
		if(getSensorId().contains("wildcard"))
			return true;
		else
			return false;
	}

	public void setWildcard(boolean wildcard) {
		this.wildcard = wildcard;
	}
}