package com.mainmethod.trailmix1.sqlite.model;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> Trail model class </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class Trail {

	// Define field variables
	int id;
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

	/***
	 * Create the constructor for the model
	 * @param trailName - Name
	 * @param length - Length
	 * @param trailClass - Class
	 * @param surface - Surface
	 * @param amenities - Amenities
	 * @param parking - Parking
	 * @param seasonHours - Season Hours
	 * @param lighting - Lighting
	 * @param winterMaintenance - Winter Maintenance
	 * @param pets - Pets
	 * @param notes - Notes
	 * @param city - City
	 */
	public Trail(String trailName, double length, String trailClass,
			String surface, String amenities, String parking,
			String seasonHours, String lighting, String winterMaintenance,
			String pets, String notes, String city) {
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
	}

	// Define Getters and Setters
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

	public String getTrailClass() {
		return trailClass;
	}

	public void setTrailClass(String trailClass) {
		this.trailClass = trailClass;
	}

	public Trail() {
		// TODO Auto-generated constructor stub
	}

	public Trail(String trailName, double length, String surface,
			String trailClass) {
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
