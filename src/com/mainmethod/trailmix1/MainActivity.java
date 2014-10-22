package com.mainmethod.trailmix1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.google.android.gms.maps.model.LatLng;
import com.mainmethod.trailmix1.kmlparsing.NavigationSaxHandler;
import com.mainmethod.trailmix1.kmlparsing.PlacemarkObj;
import com.mainmethod.trailmix1.kmlparsing.TrailObj;
import com.mainmethod.trailmix1.preferences.PrefActivity;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.GeoPoint;
import com.mainmethod.trailmix1.sqlite.model.Placemark;
import com.mainmethod.trailmix1.sqlite.model.Trail;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> This is the class that is initialized when the app first launches </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class MainActivity extends FragmentActivity {
	
	/**
	 * <p> The FragmentNavigationDrawer is a custom class which extends the DrawerLayout class.
	 * In turn, the DrawerLayout allows views to be pulled from the edge of the window
	 * (ie. Side Menu Bar). </p>
	 */
	private FragmentNavigationDrawer dlDrawer;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * <p> This references the custom activity_main_navigationdrawer.xml 
		 * inside the layout folder </p>
		 */
		setContentView(R.layout.activity_main_navigationdrawer);
		
		/**
		 * <p> The SystemBarTintManager class allows the status bar to be "tinted" in
		 * Android API 19 and above only. </p>
		 */
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// Enable status bar tint 
		tintManager.setStatusBarTintEnabled(true);
		// Enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);
		// Set color changes for the tint
		tintManager.setTintColor(Color.parseColor("#00796b"));

		// Find our drawer view
		dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
		
		// Setup drawer view
		dlDrawer.setupDrawerConfiguration(
				(ListView) findViewById(R.id.left_drawer),
				R.layout.activity_main_navigationdrawerlistitem,
				R.id.content_frame);
		
		// Call the methods in the FragmentNavigationDrawer class and add nav items along with 
		// their corresponding fragment classes
		dlDrawer.addNavItem("Home", R.drawable.ic_drawer_home, "TrailMix",
				HomeFragment.class); // HomeFragment.java
		dlDrawer.addNavItem("Tracker", R.drawable.ic_drawer_tracker, "Tracker",
				TrackerFragment.class); // TrackerFragment.java
		dlDrawer.addNavItem("Events", R.drawable.ic_drawer_events, "Events",
				EventsFragment.class); //EventsFragment.java
		dlDrawer.addNavItem("Explore Trails", R.drawable.ic_drawer_explore_trails,
				"Explore Trails", ExploreTrailsFragment.class); //ExploreTrailsFragment.java
		
		// Select the default nav item
		if (savedInstanceState == null) {
			dlDrawer.selectDrawerItem(0);
		}
		
		// *** Below code snippet reserved for testing purposes ***
		
		// HashMap<String, TrailObj> trailCollection = parser();
		// doInsert(trailCollection);
	}
	
	// *** Below code snippet reserved for testing purposes ***
	
	private void doInsert(HashMap<String, TrailObj> trailCollection) {
		DatabaseHelper db;
		db = new DatabaseHelper(getApplicationContext());

		int trailID = 0;
		int placeMarkId = 0;
        int counter = 0;
		for (TrailObj trail : trailCollection.values()) {
			trailID++;
			Trail trailModel = new Trail(trail.getTrailName(),
					trail.getLength(), trail.getSurface(),
					trail.getTrailClass());
			db.createTrail(trailModel);

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
        System.out.println("While inserting: "+ counter);
	}
	
	// *** Below code snippet reserved for testing purposes ***
	
	private HashMap<String, TrailObj> parser() {

		HashMap<String, TrailObj> trailCollection = new HashMap<String, TrailObj>();
		TrailObj tempTrail;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// Create a parser
			SAXParser parser = factory.newSAXParser();
			// Create the reader (scanner)
			// XMLReader xmlreader = parser.getXMLReader();
			// Instantiate our handler
			NavigationSaxHandler navSaxHandler = new NavigationSaxHandler();

			// parser.parse("/Users/D4RK/Dropbox/Study/MAPsPractice/KMLForAndroid/hiking_trails.kml",
			// navSaxHandler);
			// Assign our handler
			// xmlreader.setContentHandler(navSaxHandler);
			// Get our data via the url class
			// InputStream ins = this.file;
			InputStream ins = getResources().openRawResource(
					getResources().getIdentifier("trails_20141003", "raw",
							getPackageName()));
			InputSource is = new InputSource(ins);
			// Perform the synchronous parse
			
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
						for(PlacemarkObj o: currTrailPlacemarks)
						{
							counter+=o.getCoordinates().size();
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
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		if (dlDrawer.isDrawerOpen()) {
			// Uncomment to hide menu items
			// menu.findItem(R.id.mi_test).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Uncomment to inflate menu items to Action Bar
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}
			
		// opens preferences from the actionbar
		if(item.getItemId() == R.id.action_preferences){
			Intent settingsIntent = new Intent(this, PrefActivity.class);
			startActivity(settingsIntent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		dlDrawer.getDrawerToggle().syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
	}

}
