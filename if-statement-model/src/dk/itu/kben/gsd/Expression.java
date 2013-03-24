package dk.itu.kben.gsd;

public class Expression {
	
	// Default should be AND
	LogicalOperator prefixOperator = LogicalOperator.AND;

	Value aValue = null;
	
	Operator operator = null;
	
	String sensorId = null;
	
	public Expression(String sensorId, Operator operator, Value someValue) {
		this.sensorId = sensorId;
		this.operator = operator;
		this.aValue = someValue;
	}
	
	public void setPrefixOperator(LogicalOperator prefixOperator) {
		this.prefixOperator = prefixOperator;
	}
	
	public boolean evaluate() {
		Value sensorValue = BuildingDAO.getValue(sensorId);
		
		if (operator == Operator.EQUALS) {
			int c = sensorValue.compareTo(aValue);
			
			System.out.print("Is " + sensorValue + " equal to " + aValue + " ? ");
			
			if (c == 0) {
				System.out.println("Yes.");
				return true;
			}
			else {
				System.out.println("No.");
			}
		}
		else {
			if (operator == Operator.LESS_THAN) {
				int c = sensorValue.compareTo(aValue);
				System.out.print("Is " + sensorValue + " less than " + aValue + " ? ");
				
				if (c < 0) {
					System.out.println("Yes.");
					return true;
				}
				else {
					System.out.println("No.");
				}
					
			}
			else {
				if (operator == Operator.GREATER_THAN) {
					int c = sensorValue.compareTo(aValue);
					System.out.print("Is " + sensorValue + " greater than " + aValue + " ? ");
					
					if (c > 0) {
						System.out.println("Yes.");
						return true;
					}
					else {
						System.out.println("No.");
					}
				}
				else {
					if (operator == Operator.NOT) {
						int c = sensorValue.compareTo(aValue);
						System.out.print("Is " + sensorValue + " different from " + aValue + " ? ");
						
						if (c != 0) {
							System.out.println("Yes.");
							return true;
						}
						else {
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