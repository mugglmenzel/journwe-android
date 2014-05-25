package com.journwe.android;

import java.util.Date;

public class JournWeDate {
	private double vote;
	private Date start;
	private Date end;
	
	public JournWeDate(Date start, Date end, double vote) {
		this.start = start;
		this.end = end;
		this.vote = vote;
	}
	
	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	public double getVote() {
		return vote;
	}
}
