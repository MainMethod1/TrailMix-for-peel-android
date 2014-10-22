package com.mainmethod.trailmix1.sqlite.model;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> Placemark model class </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class Placemark {
 
	// Define field variables
	int id;
	int trail_id;
	
	// Empty constructor
	public Placemark() {
		// TODO Auto-generated constructor stub
	}
	
	/***
	 * Create the constructor for the model
	 * @param id - Placemark Id
	 * @param trail_id - Trail Id
	 */
	public Placemark(int id, int trail_id) {
		this.id = id;
		this.trail_id = trail_id;
	}
	
	// Define Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrail_id() {
		return trail_id;
	}

	public void setTrail_id(int trail_id) {
		this.trail_id = trail_id;
	}
}
