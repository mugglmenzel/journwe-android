package com.journwe.android;

public class JournWePlace {
	private String place;
	private double vote;
	private String favorite;
	
	public JournWePlace(String place, double vote, String favorite) {
		this.place = place;
		this.vote = vote;
		this.favorite = favorite;
	}

	public String getFavorite() {
		return favorite;
	}

	public void setFavorite(String favorite) {
		this.favorite = favorite;
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
