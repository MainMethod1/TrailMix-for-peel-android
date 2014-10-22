package com.mainmethod.trailmix1.sqlite.model;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> GeoPoint model class </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class GeoPoint {
    
	// Define field variables
	int id;
	double lat;
	double lng;
	int placemark_id;
	
	// Empty constructor
	public GeoPoint() {
	}
	
	/***
	 * Create the constructor for the model
	 * @param lat - Latitude
	 * @param lng - Longitude
	 * @param placemark_id - Placemark Id
	 */
	public GeoPoint( double lat, double lng, int placemark_id) {
		super();
		
		this.lat = lat;
		this.lng = lng;
		this.placemark_id = placemark_id;
	}
	
	public GeoPoint( double lat, double lng) {
		super();
		
		this.lat = lat;
		this.lng = lng;
	}

	// Define Getters and Setters
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
