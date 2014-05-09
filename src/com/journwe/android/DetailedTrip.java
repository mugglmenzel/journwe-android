package com.journwe.android;

import android.graphics.Bitmap;

public class DetailedTrip extends Trip {
	private Trip trip;
	private String description;

	public DetailedTrip(String id, String name, String link, int people, Status status, Bitmap image, String imageTimeStamp, String favPlace, String favTime, String imageURL) {
		super(id, name, link, people, status, image, imageTimeStamp, favPlace, favTime, imageURL);
		
	}
	
	public DetailedTrip(Trip t) {
		super(t.getId(), t.getName(), t.getLink(), t.getPeople(), t.getStatus(), null/*TODO*/, t.getImageTimeStamp(), t.getFavPlace(), t.getFavTime(), t.getImageURL());
		
		this.trip = t;
	}
	
	public Trip getTrip() {
		return trip;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
