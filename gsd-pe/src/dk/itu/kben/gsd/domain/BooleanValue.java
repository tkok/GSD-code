package dk.itu.kben.gsd.domain;

public class BooleanValue implements Value {
	
	Boolean theValue;
	
	public BooleanValue() {
	}
	
	public BooleanValue(boolean aValue) {
		theValue = new Boolean(aValue);
	}
	
	@Override
	public Boolean getValue() {
		return theValue;
	}
	
	public void setValue(boolean aValue) {
		theValue = aValue;
	}

	@Override
	public int compareTo(Value aValue) {
		if (((BooleanValue) aValue).getValue() == theValue) return 0;
		
		return -1;
	}
	
	@Override
	public String toString() {
		return theValue.toString();
	}

	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		return 0;
	}
}