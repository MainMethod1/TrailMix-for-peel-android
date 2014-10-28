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

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

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
		
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.searchOption).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
