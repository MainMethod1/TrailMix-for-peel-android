package com.mainmethod.trailmix1;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mainmethod.trailmix1.kmlparsing.NavigationSaxHandler;
import com.mainmethod.trailmix1.kmlparsing.PlacemarkObj;
import com.mainmethod.trailmix1.kmlparsing.TrailObj;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Event;
import com.mainmethod.trailmix1.sqlite.model.GeoPoint;
import com.mainmethod.trailmix1.sqlite.model.Placemark;
import com.mainmethod.trailmix1.sqlite.model.Trail;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Dialog;
import android.graphics.Color;
//import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MapActivity extends FragmentActivity {
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	GoogleMap mMap;
	private static final String TAG_NAME = "Name";
	private static final String TAG_LENGTH = "Length";
	private static final String TAG_TYPE = "Type";
	private static final String TAG_SURFACE = "Surface";
	private static final String TAG_AMENITIES = "Washrooms/Amenities";
	private static final String TAG_PARKING = "Parking";
	private static final String TAG_SEASON = "Season/Hours";
	private static final String TAG_LIGHTING = "Lighting";
	private static final String TAG_MAINTENANCE = "Winter Maintenance";
	private static final String TAG_PETS = "Pets";
	private static final String TAG_NOTES = "Notes/History";
	private static final String TAG_CITY = "City";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (servicesOK()) {

			setContentView(R.layout.activity_map);
			if (initMap()) {

				mMap.setBuildingsEnabled(true);
				mMap.setMyLocationEnabled(true);
				// mMap.setTrafficEnabled(true);

				try {
					// loadKML(mMap);
					HashMap<String, TrailObj> trailCollection = parser();

					// drawMap(trailCollection, mMap);
					doInsert(trailCollection);
					// drawTrails(mMap);
					// drawTrail(mMap, trailCollection.get("Bruce Trail"));
					// drawTrail(mMap,
					// trailCollection.get("Bruce Trail (Road Allowance)"));
					// drawTrail(mMap,
					// trailCollection.get("Bruce Trail (On Road)"));
					// drawTrail(mMap,
					// trailCollection.get("Elora Cataract Trailway"));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("KMLParingError", e.getMessage());
				}

			} else {
				Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			setContentView(R.layout.activity_main_navigationdrawer);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void drawMap(HashMap<String, TrailObj> trailCollection,
			GoogleMap mMap2) {
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

	public void drawTrailByName(GoogleMap map, String trailName) {
		DatabaseHelper db;
		db = new DatabaseHelper(this);
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

	public void drawTrail(GoogleMap map, TrailObj trail) {
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

	public void drawTrails(GoogleMap map) {
		DatabaseHelper db;
		db = new DatabaseHelper(this);
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

		map.addMarker(new MarkerOptions().position(new LatLng(43.95, -79.95))
				.title(String.valueOf(count) + " " + String.valueOf(pCount)));
		System.out.println("When reading from db:" + count);
	}

	private void doInsert(HashMap<String, TrailObj> trailCollection) {
		
		HashMap<String, Trail> trailInfoCollection = getJSONData();
//		String trailName = null;
//		TrailObj tempTrail = null;
//		for(TrailObj to : trailCollection.values())
//		{
//			tempTrail = new TrailObj();
//			trailName = to.getTrailName();
//			for(Trail t: getJSONData())
//			{
//				if(trailName.equals(t.getTrailName()))
//				{
//					tempTrail.setTrailName(t.getTrailName());
//					tempTrail.setAmenities(t.getAmenities());
//					tempTrail.setLength(t.getLength());
//					tempTrail.setCity(t.getCity());
//					tempTrail.setLighting(t.getLighting());
//					tempTrail.setNotes(t.getNotes());
//					tempTrail.setParking(t.getParking());
//					tempTrail.setSurface(t.getSurface());
//					tempTrail.setSeasonHours(t.getSeasonHours());
//					tempTrail.setPets(t.getPets());
//					
//					updatedTrailColection.put(trailName, tempTrail);
//					//add to hash map
//					break;
//				}
//			}
//		}
		DatabaseHelper db;
		db = new DatabaseHelper(getApplicationContext());

		int trailID = 0;
		int placeMarkId = 0;
		int counter = 0;
		for (TrailObj trail : trailCollection.values()) {
			trailID++;
			
			if(trailInfoCollection.containsKey(trail.getTrailName()))
			{
				Trail temp = trailInfoCollection.get(trail.getTrailName());
				db.createTrail(temp);
			}
			else
			{
				Trail trailModel = new Trail(trail.getTrailName(),
						trail.getLength(), trail.getSurface(),
						trail.getTrailClass());
				db.createTrail(trailModel);
			}
			
			for (PlacemarkObj pmark : trail.getPlacemarks()) {
				placeMarkId++;
				Placemark p = new Placemark();
				p.setTrail_id(trailID);
				db.createPlacemark(p);

				for (LatLng geoPoint : pmark.getCoordinates()) {

					GeoPoint gp = new GeoPoint(geoPoint.latitude,
							geoPoint.longitude, placeMarkId);
					db.createGeoPoint(gp);
					counter++;
				}
			}

		}
		db.closeDB();
		System.out.println("While inserting: " + counter);
	}

public HashMap<String,Trail> getJSONData() {
		
//		ArrayList<Trail> trailList = new ArrayList<Trail>();
		HashMap<String,Trail> trailList1 = new HashMap<String,Trail>();
		String jsonStr;
		int counter=0;
	
			try {
				InputStream is = getResources().openRawResource(
						getResources().getIdentifier("trail_detail", "raw",
								getPackageName()));
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				jsonStr = new String(buffer, "UTF-8");

				Log.d("Response: ", "> " + jsonStr);

				JSONArray jsonArray = new JSONArray(jsonStr);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
                    counter++;
					String name =jsonObj.getString(TAG_NAME);
					Double length = Double.parseDouble(jsonObj.getString(TAG_LENGTH));
					String type = jsonObj.getString(TAG_TYPE);
					String surface = jsonObj.getString(TAG_SURFACE);
					String amenities = jsonObj.getString(TAG_AMENITIES);
					String parking = jsonObj.getString(TAG_PARKING);
					String season = jsonObj.getString(TAG_SEASON);
					String lighting = jsonObj.getString(TAG_LIGHTING);
					String maintenance = jsonObj.getString(TAG_MAINTENANCE);
					String pets = jsonObj.getString(TAG_PETS);
					String notes = jsonObj.getString(TAG_NOTES);
					String city = jsonObj.getString(TAG_CITY);

					trailList1.put(name, new Trail(name, length, type, surface,
							amenities, parking, season, lighting, maintenance,
							pets, notes, city));
//			       trailList.add(new Trail(name, length, type, surface,
//							amenities, parking, season, lighting, maintenance,
//							pets, notes, city));

				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return trailList1;
	}

	private HashMap<String, TrailObj> parser() {

		HashMap<String, TrailObj> trailCollection = new HashMap<String, TrailObj>();
		TrailObj tempTrail;

		// TODO Auto-generated method stub
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// create a parser
			SAXParser parser = factory.newSAXParser();
			// create the reader (scanner)
			// XMLReader xmlreader = parser.getXMLReader();
			// instantiate our handler
			NavigationSaxHandler navSaxHandler = new NavigationSaxHandler();

			// parser.parse("/Users/D4RK/Dropbox/Study/MAPsPractice/KMLForAndroid/hiking_trails.kml",
			// navSaxHandler);
			// assign our handler
			// xmlreader.setContentHandler(navSaxHandler);
			// get our data via the url class
			// InputStream ins = this.file;
			InputStream ins = getResources().openRawResource(
					getResources().getIdentifier("trails_20141003", "raw",
							getPackageName()));
			InputSource is = new InputSource(ins);
			// perform the synchronous parse

			// xmlreader.parse(is);
			parser.parse(is, navSaxHandler);
			ArrayList<PlacemarkObj> ds = navSaxHandler.getPlacemarks();
			ArrayList<PlacemarkObj> currTrailPlacemarks;
			int counter = 0;
			Double currTrailLength = 0.0;

			System.out.println(ds.size());
			if (ds.isEmpty()) {
				System.out.println("It's still empty");
			} else {
				for (PlacemarkObj p : ds) {

					if (!trailCollection.containsKey(p.getTrailName())) {
						currTrailPlacemarks = new ArrayList<PlacemarkObj>();

						tempTrail = new TrailObj();
						tempTrail.setTrailName(p.getTrailName());
						currTrailLength = 0.0;
						tempTrail.setTrailClass(p.getTrailClass());
						tempTrail.setSurface(p.getSurface());

						for (PlacemarkObj pmark : ds) {

							if (pmark.getTrailName().equals(p.getTrailName())) {
								currTrailPlacemarks.add(pmark);

								currTrailLength += pmark.getLength();
							}
						}
						tempTrail.setLength(currTrailLength);
						tempTrail.setPlacemarks(currTrailPlacemarks);
						trailCollection.put(p.getTrailName(), tempTrail);
						for (PlacemarkObj o : currTrailPlacemarks) {
							counter += o.getCoordinates().size();
						}
					}
				}
				System.out.println("From parser" + counter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trailCollection;
	}

	public boolean loadKML(GoogleMap map) throws ParserConfigurationException,
			SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// create a parser
			SAXParser parser = factory.newSAXParser();
			// create the reader (scanner)
			XMLReader xmlreader = parser.getXMLReader();
			// instantiate our handler
			NavigationSaxHandler navSaxHandler = new NavigationSaxHandler();

			// parser.parse("/Users/D4RK/Dropbox/Study/MAPsPractice/KMLForAndroid/hiking_trails.kml",
			// navSaxHandler);
			// assign our handler
			xmlreader.setContentHandler(navSaxHandler);
			// get our data via the url class
			InputStream ins = getResources().openRawResource(
					getResources().getIdentifier("unpaved_multi_use_trails",
							"raw", getPackageName()));
			InputSource is = new InputSource(ins);
			// perform the synchronous parse
			xmlreader.parse(is);
			// get the results - should be a fully populated RSSFeed instance,
			// or null on error
			ArrayList<PlacemarkObj> ds = navSaxHandler.getPlacemarks();
			int counter = 0;
			int i = 0;
			System.out.println(ds.size());
			if (ds.isEmpty()) {
				System.out.println("It's still empty");
			} else {
				for (PlacemarkObj p : ds) {
					counter++;
					ArrayList<LatLng> coordinates = p.getCoordinates();

					for (LatLng lt : coordinates) {
						i++;
						/*
						 * map.addMarker(new MarkerOptions() .position(lt)
						 * .title(String.valueOf(lt.latitude) + " "
						 * +String.valueOf(lt.longitude)));
						 */
						// System.out.println(i + " "+"Lat:" +
						// String.valueOf(lt.latitude) + "  Lng:"+
						// String.valueOf(lt.longitude));
					}

					PolylineOptions rectOptions = new PolylineOptions();
					rectOptions.addAll(p.getCoordinates());
					Polyline polyline = map.addPolyline(rectOptions);
					polyline.setColor(Color.RED);
					polyline.setWidth(5);
					polyline.setVisible(true);
					Log.d("KMLParsing", counter + " "
							+ p.getCoordinates().size());
				}
				System.out.println("counter" + i);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean servicesOK() {
		int isAvailable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		} else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,
					this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		} else {
			Toast.makeText(this, "Can't connect to Google Play services",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	private boolean initMap() {
		if (mMap == null) {
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			mMap = mapFrag.getMap();
		}
		return (mMap != null);

	}
}
