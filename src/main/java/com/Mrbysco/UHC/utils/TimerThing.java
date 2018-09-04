package com.Mrbysco.UHC.utils;

public class TimerThing {
	
	private long milliTime;

	public TimerThing() {
		this.milliTime = System.currentTimeMillis();
	}
	
	public void setMilliTimeToCurrent() {
		this.milliTime = System.currentTimeMillis();;
	}

	public long getMilliTime() {
		return this.milliTime;
	}
}
