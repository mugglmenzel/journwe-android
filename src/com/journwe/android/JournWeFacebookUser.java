package com.journwe.android;

import java.io.Serializable;

import android.util.Log;

import com.facebook.Session;
import com.facebook.model.GraphUser;

public class JournWeFacebookUser implements Serializable {
	private Session session;
	private String id;
	private String name;
	private String first_name;
	private String last_name;
	private String link;
	private String username;
	private String gender;
	private String email;
	private String timezone;
	private String locale;
	private String verified;
	private String updated_time;
	private String birthday;
	
	public JournWeFacebookUser(GraphUser gu, Session s) {
		this.session = s;
		Log.i("session", s.toString());
		this.id = gu.getId();
		this.name = gu.getName();
		this.first_name = gu.getFirstName();
		this.last_name = gu.getLastName();
		this.link = gu.getLink();
		this.username = gu.getUsername();
//		this.gender = gu.ge;
		this.email = (String) gu.getProperty("email");
//		this.timezone = gu.get;
//		this.locale = gu.getLocation().toString();
//		this.verified = gu.get;
//		this.updated_time = gu.get;
		this.birthday = gu.getBirthday();
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
