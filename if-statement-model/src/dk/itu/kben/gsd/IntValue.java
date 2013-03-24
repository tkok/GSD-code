package dk.itu.kben.gsd;

public class IntValue implements Value {
	
	Integer theValue;
	
	public IntValue() {
	}
	
	public IntValue(int value) {
		theValue = new Integer(value);
	}
	
	@Override
	public Integer getValue() {
		return theValue;
	}
	
	public void setValue(Integer value) {
		theValue = value;
	}
	
	public int compareTo(Value aValue) {
		int iAValue = ((IntValue) aValue).getValue().intValue();
		int iTheValue = theValue.intValue();
		
		if (iAValue == iTheValue) return 0;
		if (iAValue < iTheValue) return 1;
		if (iAValue < iTheValue) return -1;
		
		return -1;
	}
	
	@Override
	public String toString() {
		return theValue.toString();
	}
}