package com.mainmethod.trailmix1.sqlite.model;

import java.util.Date;

public class Trail {

	int id;
	String trailName;
	double length;
	String surface;
    String trailClass;
	
	public String getTrailClass() {
		return trailClass;
	}

	public void setTrailClass(String trailClass) {
		this.trailClass = trailClass;
	}

	public Trail() {
		// TODO Auto-generated constructor stub
	}

	public Trail(String trailName, double length, String surface, String trailClass) {
		super();
		
		this.trailName = trailName;
		this.length = length;
		this.surface = surface;
		this.trailClass = trailClass;
	}
	
	public Trail(int id) {
		super();
		this.id = id;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTrailName() {
		return trailName;
	}

	public void setTrailName(String trailName) {
		this.trailName = trailName;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}
    
	
}
