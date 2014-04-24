package com.journwe.android;

import android.graphics.Bitmap;

public class DetailedTrip extends Trip {
	private Trip trip;
	
	public DetailedTrip(String id, String name, String link, int people, Status status, Bitmap image, String imageTimeStamp, String favPlace, String favTime) {
		super(id, name, link, people, status, image, imageTimeStamp, favPlace, favTime);
		
	}
	
	public DetailedTrip(Trip t) {
		super(t.getId(), t.getName(), t.getLink(), t.getPeople(), t.getStatus(), t.getImage(), t.getImageTimeStamp(), t.getFavPlace(), t.getFavTime());
		
		this.trip = t;
	}
	
	public Trip getTrip() {
		return trip;
	}
}
