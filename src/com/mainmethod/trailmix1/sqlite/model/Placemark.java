package com.mainmethod.trailmix1.sqlite.model;

public class Placemark {
 
	int id;
	int trail_id;
	
	public Placemark(int id, int trail_id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.trail_id = trail_id;
	}

	public Placemark() {
		// TODO Auto-generated constructor stub
	}

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
