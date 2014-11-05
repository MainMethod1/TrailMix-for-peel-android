package com.mainmethod.trailmix1;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mainmethod.trailmix1.kmlparsing.PlacemarkObj;
import com.mainmethod.trailmix1.kmlparsing.TrailObj;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.GeoPoint;
import com.mainmethod.trailmix1.sqlite.model.Placemark;
import com.mainmethod.trailmix1.sqlite.model.Trail;

public class MapUtil {

	private static void drawMap(HashMap<String, TrailObj> trailCollection, GoogleMap mMap2) {
		// TODO Auto-generated method stub

		for (TrailObj trail : trailCollection.values()) {
			for (PlacemarkObj p : trail.getPlacemarks()) {
				PolylineOptions rectOptions = new PolylineOptions();
				for (LatLng g : p.getCoordinates()) {
					rectOptions.add(g);
				}
				Polyline polyline = mMap2.addPolyline(rectOptions);
				polyline.setColor(Color.RED);
				polyline.setWidth(5);
				polyline.setVisible(true);
			}
		}
	}

	public static void drawTrailByName(GoogleMap map, String trailName, DatabaseHelper db) {
//		DatabaseHelper db;
//		db = new DatabaseHelper(this);
		ArrayList<Placemark> placemarks = db.getTrailPlacemarks(trailName);
		ArrayList<GeoPoint> points;
		PolylineOptions rectOptions;
		for (Placemark p : placemarks) {
			rectOptions = new PolylineOptions();
			points = db.getPlacemarkGeoPoints(p.getId());
			for (GeoPoint g : points) {
				rectOptions.add(new LatLng(g.getLat(), g.getLng()));
			}
			Polyline polyline = map.addPolyline(rectOptions);
			polyline.setColor(Color.RED);
			polyline.setWidth(8);
			polyline.setVisible(true);
		}

	}

	public static void drawTrailByClass(GoogleMap map, String trailClass, int color, DatabaseHelper db) {
//		DatabaseHelper db;
//		db = new DatabaseHelper(this);
		ArrayList<Placemark> placemarks = db.getTrailPlacemarksByClass(trailClass);
		ArrayList<GeoPoint> points;
		PolylineOptions rectOptions;
		for (Placemark p : placemarks) {
			rectOptions = new PolylineOptions();
			points = db.getPlacemarkGeoPoints(p.getId());
			for (GeoPoint g : points) {
				rectOptions.add(new LatLng(g.getLat(), g.getLng()));
			}
			Polyline polyline = map.addPolyline(rectOptions);
			polyline.setColor(color);
			polyline.setWidth(8);
			polyline.setVisible(true);
		}
	}

	public static void drawTrail(GoogleMap map, TrailObj trail) {
		ArrayList<LatLng> allPoints = new ArrayList<LatLng>();
		ArrayList<GeoPoint> points;

		PolylineOptions rectOptions;
		for (PlacemarkObj p : trail.getPlacemarks()) {
			rectOptions = new PolylineOptions();
			allPoints.addAll(p.getCoordinates());
			for (LatLng g : p.getCoordinates()) {
				rectOptions.add(g);
			}
			Polyline polyline = map.addPolyline(rectOptions);
			polyline.setColor(Color.RED);
			polyline.setWidth(8);
			polyline.setVisible(true);
		}

		// map.addMarker(new MarkerOptions()
		// .position(center)
		// .title(trail.getTrailName()));
	}

	public static void drawTrails(GoogleMap map, DatabaseHelper db) {
//		DatabaseHelper db;
//		db = new DatabaseHelper(this);
		ArrayList<ArrayList<GeoPoint>> sortedGeoPoints = db.getPlacemarks();
		int count = 0;
		int pCount = 0;
		PolylineOptions rectOptions;
		for (ArrayList<GeoPoint> p : sortedGeoPoints) {
			rectOptions = new PolylineOptions();
			pCount++;
			for (GeoPoint g : p) {
				rectOptions.add(new LatLng(g.getLat(), g.getLng()));
				count++;
			}
			Polyline polyline = map.addPolyline(rectOptions);
			polyline.setColor(Color.RED);
			polyline.setWidth(8);
			polyline.setVisible(true);
		}

		// map.addMarker(new MarkerOptions().position(new LatLng(43.95, -79.95))
		// .title(String.valueOf(count) + " " + String.valueOf(pCount)));
		System.out.println("When reading from db:" + count);
	}

	public static String formatTime(int inSeconds) {
		int minutes = (int) inSeconds / 60;

		int seconds = inSeconds % 60;

		if (seconds > 9) {
			return minutes + ":" + seconds + "";
		} else {

			return minutes + ":0" + seconds + "";
		}

	}
	
}
