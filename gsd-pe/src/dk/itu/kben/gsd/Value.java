package dk.itu.kben.gsd;

import java.io.Serializable;

public interface Value extends Comparable<Value>, Serializable {
	
	public Object getValue();

}
