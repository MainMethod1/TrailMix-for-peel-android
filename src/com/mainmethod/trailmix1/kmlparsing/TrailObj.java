package com.mainmethod.trailmix1.kmlparsing;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;


public class TrailObj {

	
	String trailName;
	double length;
	String trailClass;
	String surface;
	String amenities;
	String parking;
	String seasonHours;
	String lighting;
	String winterMaintenance;
	String pets;
	String notes;
	String city;
	ArrayList<PlacemarkObj> placemarkObjs;
	public TrailObj() {
		super();
	
	}
	
	
	public TrailObj(String trailName, double length, String trailClass,
			String surface, String amenities, String parking,
			String seasonHours, String lighting, String winterMaintenance,
			String pets, String notes, String city,
			ArrayList<PlacemarkObj> placemarkObjs) {
		super();
		this.trailName = trailName;
		this.length = length;
		this.trailClass = trailClass;
		this.surface = surface;
		this.amenities = amenities;
		this.parking = parking;
		this.seasonHours = seasonHours;
		this.lighting = lighting;
		this.winterMaintenance = winterMaintenance;
		this.pets = pets;
		this.notes = notes;
		this.city = city;
		this.placemarkObjs = placemarkObjs;
	}


	public String getAmenities() {
		return amenities;
	}


	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}


	public String getParking() {
		return parking;
	}


	public void setParking(String parking) {
		this.parking = parking;
	}


	public String getSeasonHours() {
		return seasonHours;
	}


	public void setSeasonHours(String seasonHours) {
		this.seasonHours = seasonHours;
	}


	public String getLighting() {
		return lighting;
	}


	public void setLighting(String lighting) {
		this.lighting = lighting;
	}


	public String getWinterMaintenance() {
		return winterMaintenance;
	}


	public void setWinterMaintenance(String winterMaintenance) {
		this.winterMaintenance = winterMaintenance;
	}


	public String getPets() {
		return pets;
	}


	public void setPets(String pets) {
		this.pets = pets;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public ArrayList<PlacemarkObj> getPlacemarkObjs() {
		return placemarkObjs;
	}


	public void setPlacemarkObjs(ArrayList<PlacemarkObj> placemarkObjs) {
		this.placemarkObjs = placemarkObjs;
	}


	public void setLength(double length) {
		this.length = length;
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
		return length;
	}
	public void setLength(Double length) {
		length = length;
	}
	public ArrayList<PlacemarkObj> getPlacemarks() {
		return placemarkObjs;
	}
	public void setPlacemarks(ArrayList<PlacemarkObj> placemarkObjs) {
		this.placemarkObjs = placemarkObjs;
	}
	
	
	
}
