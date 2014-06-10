package com.journwe.android;

public class JournWePlace {
	private String place;
	private double vote;
	private String favorite;
	private double lat;
	private double lng;
	
	public JournWePlace(String place, double vote, String favorite, double lat, double lng) {
		this.place = place;
		this.vote = vote;
		this.favorite = favorite;
		this.lat = lat;
		this.lng = lng;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
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
