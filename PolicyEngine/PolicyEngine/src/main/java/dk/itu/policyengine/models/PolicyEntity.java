package dk.itu.policyengine.models;

import java.io.Serializable;
import java.sql.Time;

public class PolicyEntity implements Serializable {
	
	long id = -1;
	
	Policy policy;
	
	Time fromTime;
	
	Time toTime;
	
	boolean active = false;
	
	public PolicyEntity() {
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Time getFromTime() {
		return fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Time getToTime() {
		return toTime;
	}

	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}