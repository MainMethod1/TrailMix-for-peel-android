package com.mainmethod.trailmix1.kmlparsing;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

//import com.google.android.gms.maps.model.LatLng;


public class PlacemarkObj {

	ArrayList<LatLng> coordinates = new ArrayList<LatLng>();
	String trailClass;
	String trailName;
	String surface;
	Double length;

	public ArrayList<LatLng> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<LatLng> coordinates) {
		this.coordinates = coordinates;
	}

	public String getTrailClass() {
		return trailClass;
	}

	public void setTrailClass(String trailClass) {
		this.trailClass = trailClass;
	}

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public String getTrailName() {
		return trailName;
	}

	public void setTrailName(String trailName) {
		this.trailName = trailName;
	}
	
	
	
}
