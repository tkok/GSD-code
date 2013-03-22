package dk.itu.kben.gsd;

public class BooleanValue implements Value {
	
	Boolean theValue;
	
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
}