package com.mainmethod.trailmix1.sqlite.model;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> Events model class </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class Event {

	// Define field variables
	String title;
	int id;
	String desc;
	String url;
	String url_text;
	String date;
	String startTime;
	String endTime;
	boolean isAllDayEvent;
	String location;
	String contactName;
	String contactEmail;
	String poster_url;
	
	// Empty constructor
		public Event(){	
		}
		
	/***
	 * Create the constructor for the model
	 * @param title - Title
	 * @param id - Id
	 * @param desc - Description
	 * @param url - Url
	 * @param url_text - Url Text 
	 * @param date - Date
	 * @param startTime - Start Time
	 * @param endTime - End Time
	 * @param isAllDayEvent - All Day
	 * @param location - Location 
	 * @param contactName - Contact Name
	 * @param contactEmail - Contact Email
	 * @param poster_url - Poster Link
	 */
	public Event(String title, int id, String desc, String url,
			String url_text, String date, String startTime, String endTime,
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
	
	// Define Getters and Setters
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
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
	public String getPoster_url() {
		return poster_url;
	}
	public void setPoster_url(String poster_url) {
		this.poster_url = poster_url;
	}
}
