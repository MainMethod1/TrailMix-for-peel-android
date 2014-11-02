package com.mainmethod.trailmix1.sqlite.model;

public class SessionGeopoint {
	int id;
	double lat;
	double lng;
	int session_id;
	public SessionGeopoint(int id, double lat, double lng, int session_id) {
		super();
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.session_id = session_id;
	}
	public SessionGeopoint(double lat, double lng, int session_id) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.session_id = session_id;
	}
	
	
	public SessionGeopoint(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}
	public SessionGeopoint() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public int getSession_id() {
		return session_id;
	}
	public void setSession_id(int session_id) {
		this.session_id = session_id;
	}
}
