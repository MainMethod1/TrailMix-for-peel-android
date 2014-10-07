package com.mainmethod.trailmix1.sqlite.model;

//import java.util.Date;

import java.util.Date;

import android.text.format.Time;

public class Event {

	String title;
	int id;
	String desc;
	String url;
	String url_text;
	Date date;
	Time startTime;
	Time endTime;
	boolean isAllDayEvent;
	String location;
	String contactName;
	String contactEmail;
	String poster_url;

	public String getPoster_url() {
		return poster_url;
	}

	public void setPoster_url(String poster_url) {
		this.poster_url = poster_url;
	}

	public Event() {
		// TODO Auto-generated constructor stub
	}

	public Event(String title, int id, String desc, String url,
			String url_text, Date date, Time startTime, Time endTime,
			boolean isAllDayEvent, String location, String contactName,
			String contactEmail, String poster_url) {
		super();
		this.title = title;
		this.id = id;
		this.desc = desc;
		this.url = url;
		this.url_text = url_text;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isAllDayEvent = isAllDayEvent;
		this.location = location;
		this.contactName = contactName;
		this.contactEmail = contactEmail;
		this.poster_url = poster_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl_text() {
		return url_text;
	}

	public void setUrl_text(String url_text) {
		this.url_text = url_text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public boolean isAllDayEvent() {
		return isAllDayEvent;
	}

	public void setAllDayEvent(boolean isAllDayEvent) {
		this.isAllDayEvent = isAllDayEvent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

}
