package com.journwe.android;

public class JournWeAdventurer {

	private String id;
	private Status status;
	private String name;
	private String link;

	public JournWeAdventurer(String id, String status, String name, String link) {
		this.id = id;
		this.name = name;
		this.link = link;

		if (status.toLowerCase().equals("going")) {
			this.status = Status.GOING;
		}

		else if (status.toLowerCase().equals("booked")) {
			this.status = Status.BOOKED;
		}

		else if (status.toLowerCase().equals("notgoing")) {
			this.status = Status.NOTGOING;
		}

		else if (status.toLowerCase().equals("undecided")) {
			this.status = Status.UNDECIDED;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
