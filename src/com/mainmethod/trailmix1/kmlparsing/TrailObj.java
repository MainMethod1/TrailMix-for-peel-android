package com.mainmethod.trailmix1.kmlparsing;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;


public class TrailObj {

	
	private String trailName;
	private String trailClass;
	private String surface;
	private Double Length;
	ArrayList<Placemark> placemarks;
	public TrailObj() {
		super();
	
	}
	public String getTrailName() {
		return trailName;
	}
	public void setTrailName(String trailName) {
		this.trailName = trailName;
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
		return Length;
	}
	public void setLength(Double length) {
		Length = length;
	}
	public ArrayList<Placemark> getPlacemarks() {
		return placemarks;
	}
	public void setPlacemarks(ArrayList<Placemark> placemarks) {
		this.placemarks = placemarks;
	}
	
	
	
}
