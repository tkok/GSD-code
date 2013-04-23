package dk.itu.kben.gsd.domain;

import java.io.Serializable;

import dk.itu.kben.gsd.persistence.BuildingDAO;
import dk.itu.nicl.gsd.log.*;
import dk.itu.scas.gsd.utils.SensorValueCache;

public class Expression implements Serializable {
	
	// Default should be AND
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
		Value sensorValue = SensorValueCache.getValue(sensorId);
		
		if (operator == Operator.EQUALS) {
			int c = sensorValue.compareTo(aValue);
			
			Log.log("Is " + sensorValue + " equal to " + aValue + " ? ");
			System.out.print("Is " + sensorValue + " equal to " + aValue + " ? ");
			
			if (c == 0) {
				Log.log("Yes.");
				System.out.println("Yes.");
				return true;
			}
			else {
				Log.log("No.");
				System.out.println("No.");
			}
		}
		else {
			if (operator == Operator.LESS_THAN) {
				int c = sensorValue.compareTo(aValue);
				Log.log("Is " + sensorValue + " less than " + aValue + " ? ");
				System.out.print("Is " + sensorValue + " less than " + aValue + " ? ");
				
				if (c < 0) {
					Log.log("Yes.");
					System.out.println("Yes.");
					return true;
				}
				else {
					Log.log("No.");
					System.out.println("No.");
				}
					
			}
			else {
				if (operator == Operator.GREATER_THAN) {
					int c = sensorValue.compareTo(aValue);
					Log.log("Is " + sensorValue + " greater than " + aValue + " ? ");
					System.out.print("Is " + sensorValue + " greater than " + aValue + " ? ");
					
					if (c > 0) {
						Log.log("Yes.");
						System.out.println("Yes.");
						return true;
					}
					else {
						Log.log("No.");
						System.out.println("No.");
					}
				}
				else {
					if (operator == Operator.NOT) {
						int c = sensorValue.compareTo(aValue);
						Log.log("Is " + sensorValue + " different from " + aValue + " ? ");
						System.out.print("Is " + sensorValue + " different from " + aValue + " ? ");
						
						if (c != 0) {
							Log.log("Yes.");
							System.out.println("Yes.");
							return true;
						}
						else {
							Log.log("No.");
							System.out.println("No.");
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