package dk.itu.policyengine.domain;

public interface Value extends Comparable<Value> {
	public Object getValue();
	
	public int getIntValue();
}