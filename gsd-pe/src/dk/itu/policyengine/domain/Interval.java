package dk.itu.policyengine.domain;

import java.sql.Time;

public class Interval {
	
	private Time fromTime;
	
	private Time toTime;
	
	public Interval() {
	}

	public Interval(Time fromTime, Time toTime) {
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
	
	public Time getFromTime() {
		return fromTime;
	}
	
	public Time getToTime() {
		return toTime;
	}
	
	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}
	
	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}
}