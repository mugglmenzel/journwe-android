package com.journwe.android;

import java.util.Date;

public class JournWeDate {
	private double vote;
	private Date start;
	private Date end;
	private String favorite;
	
	public JournWeDate(Date start, Date end, double vote, String favorite) {
		this.start = start;
		this.end = end;
		this.vote = vote;
		this.favorite = favorite;
	}
	
	public String getFavorite() {
		return favorite;
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
