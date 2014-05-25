package com.journwe.android;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class DetailedTrip extends Trip {
	private Trip trip;
	private String description;
	private ArrayList<JournWeDate> dates;
	private ArrayList<JournWePlace> places;
	private ArrayList<JournWeAdventurer> adventurers;

	public DetailedTrip(String id, String name, String link, int people, Status status, Bitmap image, String imageTimeStamp, String favPlace, String favTime, String imageURL) {
		super(id, name, link, people, status, image, imageTimeStamp, favPlace, favTime, imageURL);
		
		dates = new ArrayList();
		places = new ArrayList();
		adventurers = new ArrayList();
	}
	
	public DetailedTrip(Trip t) {
		super(t.getId(), t.getName(), t.getLink(), t.getPeople(), t.getStatus(), null/*TODO*/, t.getImageTimeStamp(), t.getFavPlace(), t.getFavTime(), t.getImageURL());
		
		this.trip = t;
		dates = new ArrayList();
		places = new ArrayList();
		adventurers = new ArrayList();
	}
	
	public void addAdventurer(JournWeAdventurer a) {
		adventurers.add(a);
	}
	
	public ArrayList<JournWeAdventurer> getAdventurers() {
		return adventurers;
	}
	
	public void addPlace(JournWePlace p) {
		places.add(p);
	}
	
	public ArrayList<JournWePlace> getPlaces() {
		return places;
	}
	
	public void addDate(JournWeDate d) {
		dates.add(d);
	}

	public ArrayList<JournWeDate> getDates() {
		return dates;
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
