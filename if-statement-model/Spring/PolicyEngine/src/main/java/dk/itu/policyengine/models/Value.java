package dk.itu.policyengine.models;

import java.io.Serializable;

public interface Value extends Comparable<Value>, Serializable {
	
	public Object getValue();

}
