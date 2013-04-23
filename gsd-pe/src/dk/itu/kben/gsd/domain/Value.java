package dk.itu.kben.gsd.domain;

import java.io.Serializable;

public interface Value extends Comparable<Value>, Serializable {
	public Object getValue();
	
	public int getIntValue();
}