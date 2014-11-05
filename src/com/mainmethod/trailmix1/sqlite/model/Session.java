package com.mainmethod.trailmix1.sqlite.model;

public class Session {
    int id;
	double distance;
	double speed;
	int time;
	String created_at;
	public Session(int id, double distance, double speed, int time, String created_at) {
		super();
		this.id = id;
		this.distance = distance;
		this.speed = speed;
		this.time = time;
		this.created_at = created_at;
	}
	
	public Session(int id, double distance, double speed, int time) {
		super();
		this.id = id;
		this.distance = distance;
		this.speed = speed;
		this.time = time;
	}
	public Session(double distance, double speed, int time) {
		super();
		this.distance = distance;
		this.speed = speed;
		this.time = time;
	}
	public Session() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
}
