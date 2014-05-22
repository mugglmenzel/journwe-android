package com.journwe.android;

public class Date {
	private String start;
	private String end;
	private String vote;
	
	public Date(String start, String end, String vote) {
		this.start = start;
		this.end = end;
		this.vote = vote;
	}
	
	public String getStart() {
		return start;
	}
	
	public String getEnd() {
		return end;
	}
	
	public String getVote() {
		return vote;
	}
}
