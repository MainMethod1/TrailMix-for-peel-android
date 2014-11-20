package com.mainmethod.trailmix1;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mainmethod.trailmix1.kmlparsing.PlacemarkObj;
import com.mainmethod.trailmix1.kmlparsing.TrailObj;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.GeoPoint;
import com.mainmethod.trailmix1.sqlite.model.Placemark;
import com.mainmethod.trailmix1.sqlite.model.Trail;

public class MapUtil {
	public static boolean isComingFromSearch = false;
	public static boolean isComingFromTrailDetail = false;
	public static HashMap<String,LatLng> searchResults = null;
	public static final String bikeStatement = "Bicycle Lane' , 'Paved Multi-use Trail','Marked On Road Bicyle Route' "
			+ ",'Unpaved Multi-use Trail','Unmarked Dirt Trail";
	
	public static final String walkStatement = "Paved Multi-use Trail','Hiking Trail' "
			+ ",'Unpaved Multi-use Trail','Unmarked Dirt Trail";
	
	public static final String hikeStatement = "Hiking Trail','Unpaved Multi-use Trail";

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
		int i=0;
		ArrayList<Placemark> placemarks = db.getTrailPlacemarks(trailName);
		ArrayList<GeoPoint> points;
		PolylineOptions rectOptions;
		for (Placemark p : placemarks) {
			rectOptions = new PolylineOptions();
			points = db.getPlacemarkGeoPoints(p.getId());
			for (GeoPoint g : points) {
				if(i==0){
					LatLng trailMarker = new LatLng(g.getLat(),g.getLng());
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(trailMarker, 14));
					map.addMarker(new MarkerOptions()
					 .position(trailMarker)).setTitle(trailName);
				}
				rectOptions.add(new LatLng(g.getLat(), g.getLng()));
				i++;
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
	public static void drawTrailMarkersByClass(GoogleMap map, String trailClass, DatabaseHelper db) {
	
		for (Trail trail: db.getAllTrailsWithInfo(trailClass).values())
		{
			LatLng center = new LatLng(trail.getMidPointLat(), trail.getMidPointLng());
			map.addMarker(new MarkerOptions()
			 .position(center)
			 .title(trail.getTrailName())
			 .snippet("Length: "+ trail.getLength()+"km Surface: "+trail.getSurface())
			 .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
		}
	}
	
	public static ArrayList<ArrayList<GeoPoint>> getGeoPointsByClass (DatabaseHelper db, String trailClasses)
	{
		
	
		ArrayList<Placemark> placemarks = db.getTrailPlacemarksByClass(trailClasses);
		
		
		ArrayList<ArrayList<GeoPoint>> reqPoints = new ArrayList<ArrayList<GeoPoint>>();
		ArrayList<GeoPoint> coords;
		for( Placemark pmark: placemarks)
		{
			coords = db.getPlacemarkGeoPoints(pmark.getId());
			reqPoints.add(coords);
		}
		db.closeDB();
		return reqPoints;
	}
	
	public static ArrayList<ArrayList<LatLng>> setPoints(DatabaseHelper db,String trailClasses)
	{
		ArrayList<ArrayList<GeoPoint>> geoPoints = getGeoPointsByClass(db,trailClasses);//db.getPlacemarks();
		ArrayList<ArrayList<LatLng>> points = new ArrayList<ArrayList<LatLng>>();
		ArrayList<LatLng> currPoints;
		for(ArrayList<GeoPoint> currGeoPoints: geoPoints)
		{
			currPoints = new ArrayList<LatLng>();
			for(GeoPoint gp: currGeoPoints)
			{
				currPoints.add(new LatLng(gp.getLat(), gp.getLng()));
			}
			points.add(currPoints);
		}
		return points;
	}
	
}
