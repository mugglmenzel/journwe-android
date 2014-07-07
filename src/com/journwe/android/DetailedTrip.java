package com.journwe.android;

import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;

public class DetailedTrip extends Trip {
	private Trip trip;
	private String description;
	private ArrayList<JournWeDate> dates;
	private ArrayList<JournWePlace> places;
	private ArrayList<JournWeAdventurer> adventurers;
	private JournWePlace favPlace;
	private JournWeDate favDate;
	private Bitmap googleMap;
	private Date placeDeadline;
	private Date dateDeadline;

	public DetailedTrip(String id, String name, String link, int people, Status status, Bitmap image, String imageTimeStamp, String favPlace, String favTime, String imageURL) {
		super(id, name, link, people, status, image, imageTimeStamp, favPlace, favTime, imageURL);
		
		dates = new ArrayList();
		places = new ArrayList();
		adventurers = new ArrayList();
	}
	
	public DetailedTrip(Trip t) {
		super(t.getId(), t.getName(), t.getLink(), t.getPeople(), t.getStatus(), null/*TODO*/, t.getImageTimeStamp(), t.getFavPlace(), t.getFavTime(), t.getImageURL());
		
		this.trip = t;
		dates = new ArrayList<JournWeDate>();
		places = new ArrayList<JournWePlace>();
		adventurers = new ArrayList<JournWeAdventurer>();
	}
	
	public void reset() {
		trip.reset();
		description = "";
		dates = new ArrayList<JournWeDate>();
		places = new ArrayList<JournWePlace>();
		adventurers = new ArrayList<JournWeAdventurer>();
		favPlace = null;
		favDate = null;
		googleMap = null;
	}

	public Date getDateDeadline() {
		return dateDeadline;
	}

	public void setDateDeadline(Date dateDeadline) {
		this.dateDeadline = dateDeadline;
	}

	public Date getPlaceDeadline() {
		return placeDeadline;
	}
	
	public void setPlaceDeadline(Date placeDeadline) {
		this.placeDeadline = placeDeadline;
	}
	
	public void setGoogleMap(Bitmap googleMap) {
		this.googleMap = googleMap;
	}
	
	public Bitmap getGoogleMap() {
		return googleMap;
	}

	public JournWePlace getFavoritePlace() {
		return favPlace;
	}

	public void setFavoritePlace(JournWePlace favPlace) {
		this.favPlace = favPlace;
	}

	public JournWeDate getFavoriteDate() {
		return favDate;
	}

	public void setFavoriteDate(JournWeDate favDate) {
		this.favDate = favDate;
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
