package dk.itu.policyengine.domain;

import org.apache.log4j.Logger;

import dk.itu.policyengine.persistence.SensorValueCache;

public class Expression {
	private transient Logger logger = Logger.getLogger(this.getClass());
	
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
	
	public boolean evaluate() {
		FloatValue sensorValue = SensorValueCache.getValue(sensorId);
		
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
	
	public String getSensorId() {
		return sensorId;
	}
}