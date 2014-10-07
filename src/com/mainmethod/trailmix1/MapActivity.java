package com.mainmethod.trailmix1;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
import com.mainmethod.trailmix1.kmlparsing.Placemark;
import com.mainmethod.trailmix1.kmlparsing.TrailObj;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.GeoPoint;
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
					// HashMap<String,TrailObj> trailCollection = parser();
					drawTrails(mMap);
					// doInsert(trailCollection);
					// InputStream ins = getResources().openRawResource(
					// getResources().getIdentifier("unpaved_multi_use_trails",
					// "raw", getPackageName()));
					//
					// DatabaseHelper db;
					// db = new DatabaseHelper(getApplicationContext(), ins);
					// db.closeDB();
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

	public void drawTrails(GoogleMap map) {
		DatabaseHelper db;
		db = new DatabaseHelper(this);
		ArrayList<GeoPoint> points = db.getTrailGeoPoints("Humber Valley Heritage Trail");
		PolylineOptions rectOptions = new PolylineOptions();
		for (GeoPoint point : points) {
			
			rectOptions.add(new LatLng(point.getLat(), point.getLng()));
			
		}
		Polyline polyline = map.addPolyline(rectOptions);
		polyline.setColor(Color.RED);
		polyline.setWidth(5);
		polyline.setVisible(true);
	}

	private void doInsert(HashMap<String, TrailObj> trailCollection) {
		DatabaseHelper db;
		db = new DatabaseHelper(getApplicationContext());
/*
		int keyID = 0;
		// TODO Auto-generated method stub
		for (TrailObj trail : trailCollection.values()) {
			Trail trailModel = new Trail(trail.getTrailName(),
					trail.getLength(), trail.getSurface(),
					trail.getTrailClass());
			db.createTrail(trailModel);
			keyID++;
			for (LatLng geoPoint : trail.getGeopoints()) {
				GeoPoint gp = new GeoPoint(geoPoint.latitude,
						geoPoint.longitude, keyID);
				db.createGeoPoint(gp);
			}
		}
		db.closeDB();
		*/
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
			XMLReader xmlreader = parser.getXMLReader();
			// instantiate our handler
			NavigationSaxHandler navSaxHandler = new NavigationSaxHandler();

			// parser.parse("/Users/D4RK/Dropbox/Study/MAPsPractice/KMLForAndroid/hiking_trails.kml",
			// navSaxHandler);
			// assign our handler
			xmlreader.setContentHandler(navSaxHandler);
			// get our data via the url class
			// InputStream ins = this.file;
			InputStream ins = getResources().openRawResource(
					getResources().getIdentifier("hiking_trails", "raw",
							getPackageName()));
			InputSource is = new InputSource(ins);
			// perform the synchronous parse

			String abc = is.toString();
			xmlreader.parse(is);
			ArrayList<Placemark> ds = navSaxHandler.getPlacemarks();
			ArrayList<Placemark> currTrailPlacemarks;
			String pmarkName_1 = "";
			String pmarkName_2 = "";
			Double currTrailLength = 0.0;

			int counter = 0;
			int i = 0;
			System.out.println(ds.size());
			if (ds.isEmpty()) {
				System.out.println("It's still empty");
			} else {
				for (Placemark p : ds) {
					counter++;

					if (p.getTrailName() != null) {
						pmarkName_1 = p.getTrailName();
					} else {
						pmarkName_1 = "UnknownTrail"+String.valueOf(counter);
					}

					currTrailLength = 0.0;

					if (!trailCollection.containsKey(pmarkName_1)) {
						currTrailPlacemarks = new ArrayList<Placemark>();

						tempTrail = new TrailObj();
						tempTrail.setTrailName(pmarkName_1);
						tempTrail.setTrailClass(p.getTrailClass());
						tempTrail.setSurface(p.getSurface());

						for (Placemark pmark : ds) {
                            i++;
							if (pmark.getTrailName() != null) {
								pmarkName_2 = pmark.getTrailName();
							} else {
								pmarkName_2 = "UnknownTrail"+String.valueOf(i);
							}

							if (pmarkName_2.equals(pmarkName_1)) {
								currTrailPlacemarks.add(pmark);
								currTrailLength += pmark.getLength();
							}
						}
						tempTrail.setLength(currTrailLength);
						tempTrail.setPlacemarks(currTrailPlacemarks);
						trailCollection.put(pmarkName_1, tempTrail);
					}
				}
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
			ArrayList<Placemark> ds = navSaxHandler.getPlacemarks();
			int counter = 0;
			int i = 0;
			System.out.println(ds.size());
			if (ds.isEmpty()) {
				System.out.println("It's still empty");
			} else {
				for (Placemark p : ds) {
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
