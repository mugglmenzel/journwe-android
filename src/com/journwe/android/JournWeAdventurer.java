package com.journwe.android;

public class JournWeAdventurer implements Comparable<JournWeAdventurer> {

	private String id;
	private Status status;
	private String name;
	private String link;
	private String imageURL;

	public JournWeAdventurer(String id, String status, String name, String link, String imageURL) {
		this.id = id;
		this.name = name;
		this.link = link;
		this.imageURL = imageURL;

		if (status.toLowerCase().equals("going")) {
			this.status = Status.GOING;
		}

		else if (status.toLowerCase().equals("booked")) {
			this.status = Status.BOOKED;
		}

		else if (status.toLowerCase().equals("notgoing") || status.toLowerCase().equals("not going")) {
			this.status = Status.NOTGOING;
		}

		else {
			this.status = Status.UNDECIDED;
		}
	}

	public String getimageURL() {
		return imageURL;
	}

	public void setimageURL(String imageURL) {
		this.imageURL = imageURL;
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

	@Override
	public int compareTo(JournWeAdventurer another) {
		switch (this.getStatus()) {
		case BOOKED:
			if (another.getStatus() == Status.BOOKED) {
				return 0;
			}
			
			else {
				return -1;
			}
		case GOING:
			if (another.getStatus() == status.BOOKED) {
				return 1;
			}
			
			else if (another.getStatus() == status.GOING) {
				return 0;
			}
			
			else {
				return -1;
			}
		case UNDECIDED:
			if (another.getStatus() == status.BOOKED || another.getStatus() == status.GOING) {
				return 1;
			}
			
			else if (another.getStatus() == status.UNDECIDED) {
				return 0;
			}
			
			else {
				return -1;
			}
		case NOTGOING:
			if (another.getStatus() == status.NOTGOING) {
				return 0;
			}
			
			else {
				return 1;
			}
		default:
			break;
		}
		
		return 0;
	}
}
