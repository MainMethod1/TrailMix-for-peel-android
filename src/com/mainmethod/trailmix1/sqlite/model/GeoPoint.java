package com.mainmethod.trailmix1.sqlite.model;

public class GeoPoint {
    
	int id;
	double lat;
	double lng;
	int placemark_id;
	
	public GeoPoint() {
		// TODO Auto-generated constructor stub
	}
	
	public GeoPoint( double lat, double lng, int placemark_id) {
		super();
		
		this.lat = lat;
		this.lng = lng;
		this.placemark_id = placemark_id;
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

	public int getPlacemark_id() {
		return placemark_id;
	}

	public void setPlacemark_id(int placemark_id) {
		this.placemark_id = placemark_id;
	}	

}
