package com.journwe.android;

import android.media.Image;

public class Trip {
	private String id;
	private String link;
	private Image image;
	private String imageTimeStamp;
	private String name;
	private int people;
	private String favPlace;
	private String favTime;
	private Status status;
	
	public Trip(String id, String name, String link, int people, Status status) {
		this.id = id;
		this.name = name;
		this.link = link;
		this.people = people;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getImageTimeStamp() {
		return imageTimeStamp;
	}

	public void setImageTimeStamp(String imageTimeStamp) {
		this.imageTimeStamp = imageTimeStamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public String getFavPlace() {
		return favPlace;
	}

	public void setFavPlace(String favPlace) {
		this.favPlace = favPlace;
	}

	public String getFavTime() {
		return favTime;
	}

	public void setFavTime(String favTime) {
		this.favTime = favTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
/*
 * [
 * {"id":"9487941a-e473-4502-b17e-72d822bf7f21",
 * "link":"http://www.journwe.com/adventure/9487941a-e473-4502-b17e-72d822bf7f21",
 * "image":null,
 * "imageTimestamp":null,
 * "name":"Test für Max 1",
 * "peopleCount":1,
 * "favoritePlace":null,
 * "favoriteTime":null,
 * "status":"GOING"}
 * ,
 * {"id":"17456351-d1fe-4ae1-86a9-0954c3948a4c",
 * "link":"http://www.journwe.com/adventure/17456351-d1fe-4ae1-86a9-0954c3948a4c",
 * "image":null,
 * "imageTimestamp":null,
 * "name":"Test für Max 2",
 * "peopleCount":1,
 * "favoritePlace":null,
 * "favoriteTime":null,
 * "status":"GOING"}
 * ]
 */