package com.journwe.android;

public class JournWePlace {
	private String place;
	private double vote;
	
	public JournWePlace(String place, double vote) {
		this.place = place;
		this.vote = vote;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public double getVote() {
		return vote;
	}

	public void setVote(double vote) {
		this.vote = vote;
	}
}
