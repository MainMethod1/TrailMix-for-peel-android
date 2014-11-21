package com.mainmethod.trailmix1;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

/***
 * <h1>TrailMix for Android Capstone Project</h1> <h2>This is the class that is
 * initialized when the app first launches</h2>
 * <p>
 * Client: Erica Duque
 * </p>
 * <p>
 * Oganization: Region of Peel
 * </p>
 * 
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class MainActivity extends FragmentActivity {

	private static final String mySharedPreferences = "previousUpdate";
	private static final String TRAILREPORT_POST_URL = "http://142.55.49.125/trailmixadmin/api/trailreportsapi";
	private static final String ACTREPORT_POST_URL = "http://142.55.49.125/trailmixadmin/api/activityreportsapi";
	/**
	 * <p>
	 * The FragmentNavigationDrawer is a custom class which extends the
	 * DrawerLayout class. In turn, the DrawerLayout allows views to be pulled
	 * from the edge of the window (ie. Side Menu Bar).
	 * </p>
	 */
	private FragmentNavigationDrawer dlDrawer;
	TrackerFragment trackerFragment;
	String actJson = "";
	String trailJson = "";
	String response = "";
	HashMap<String, String> activityReportData;
	HashMap<String, String> trailReportData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * <p>
		 * This references the custom activity_main_navigationdrawer.xml inside
		 * the layout folder
		 * </p>
		 */
		setContentView(R.layout.activity_main_navigationdrawer);

		/**
		 * <p>
		 * The SystemBarTintManager class allows the status bar to be "tinted"
		 * in Android API 19 and above only.
		 * </p>
		 */
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// Enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// Enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);
		// Set color changes for the tint
		tintManager.setTintColor(Color.parseColor("#303F9F"));

		// to push local data to server for reports
		SharedPreferences sharedPrefs = getSharedPreferences(mySharedPreferences, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		Calendar calobj = Calendar.getInstance();
		DatabaseHelper db = new DatabaseHelper(this);
		try {
			db.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (sharedPrefs.getInt("previousUpdateDate", 0) == 0) {
			editor.putInt("previousUpdateDate", calobj.get(Calendar.DATE));
			Toast.makeText(this, "hey from shared prefs 1", Toast.LENGTH_LONG).show();
			editor.commit();
		} else if (sharedPrefs.getInt("previousUpdateDate", 0) != calobj.get(Calendar.DATE)) {
			editor.putInt("previousUpdateDate", calobj.get(Calendar.DATE));
			editor.commit();
			Toast.makeText(this, "hey from shared prefs", Toast.LENGTH_LONG).show();

			activityReportData = db.getActivityReportData();
			trailReportData = db.getTrailReportData();
			
			new DoTrailPost().execute();
			new DoActPost().execute();
			
			 db.clearTable("activityReport");
			 db.clearTable("trailReport");
		} else {
			Toast.makeText(this, "NADA", Toast.LENGTH_LONG).show();
		}
		db.closeDB();

		// Find our drawer view
		dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);

		// Setup drawer view
		dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.left_drawer),
				R.layout.activity_main_navigationdrawerlistitem, R.id.content_frame);

		// Call the methods in the FragmentNavigationDrawer class and add nav
		// items along with
		// their corresponding fragment classes
		dlDrawer.addNavItem("Home", R.drawable.ic_drawer_home, "Select Activity", HomeFragment.class); // HomeFragment.java
		dlDrawer.addNavItem("Explore Trails", R.drawable.ic_drawer_explore, "Explore Trails",
				ExploreTrailsFragment.class);
		dlDrawer.addNavItem("Tracker", R.drawable.ic_drawer_tracker, "Tracker", TrackerFragment.class); // TrackerFragment.java

		dlDrawer.addNavItem("History", R.drawable.ic_drawer_history, "History", HistoryFragment.class);
		dlDrawer.addNavItem("Events", R.drawable.ic_drawer_event, "Events", EventsFragment.class); // EventsFragment.java
		// ExploreTrailsFragment.java

		// Select the default nav item
		if (savedInstanceState == null) {
			System.out.println("Error: " + MapActivity.ARG_TRACKER_FLAG);
			if (getIntent().hasExtra(MapActivity.ARG_TRACKER_FLAG)) {
				if (getIntent().getStringExtra(MapActivity.ARG_TRACKER_FLAG).equals(new String("bike"))
						|| getIntent().getStringExtra(MapActivity.ARG_TRACKER_FLAG).equals(new String("run"))
						|| getIntent().getStringExtra(MapActivity.ARG_TRACKER_FLAG).equals(new String("hike"))) {

					FragmentManager fm = getSupportFragmentManager();
					try {
						trackerFragment = TrackerFragment.class.newInstance();
						Bundle arguments = new Bundle();
						arguments.putString(MapActivity.ARG_TRACKER_FLAG,
								getIntent().getStringExtra(MapActivity.ARG_TRACKER_FLAG));
						trackerFragment.setArguments(arguments);
					} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					FragmentTransaction transaction = fm.beginTransaction()
							.replace(R.id.content_frame, trackerFragment);
					// transaction.addToBackStack(null);
					transaction.commit();

				}
			} else if (getIntent().hasExtra(TrailDetailActivity.ARG_TRAIL_FLAG)) {
				if (getIntent().getStringExtra(TrailDetailActivity.ARG_TRAIL_FLAG) != null) {
					FragmentManager fm = getSupportFragmentManager();
					try {
						trackerFragment = TrackerFragment.class.newInstance();
						Bundle arguments = new Bundle();
						arguments.putString(TrailDetailActivity.ARG_TRAIL_FLAG,
								getIntent().getStringExtra(TrailDetailActivity.ARG_TRAIL_FLAG));
						trackerFragment.setArguments(arguments);
					} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					FragmentTransaction transaction = fm.beginTransaction()
							.replace(R.id.content_frame, trackerFragment);
					// transaction.addToBackStack(null);
					transaction.commit();
				} else {
					// do nothing
				}
			} else {
				dlDrawer.selectDrawerItem(0);
			}
		}

	}

	// async task to POST activity report data
	public class DoActPost extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				
				for (String date : activityReportData.keySet()) {
					actJson += "{\"" + "Activity" + "\":\"";
					actJson += activityReportData.get(date);
					actJson += "\", \"CreatedAt\":\"";
					actJson += date.substring(0, 10) + "\"}";
					Log.e("POST JSON", actJson);
					response = ServiceHandler.makePOSTCall(ACTREPORT_POST_URL, actJson);
					Log.e("ACTIVITY POST RESPONSE", response);
					actJson = "";
				}
				// System.out.println(json);

			} catch (Exception e) {
				Log.e("POST ERROR", e.getMessage());
			}
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			

		}
	}

	// async task to POST trail report data
	public class DoTrailPost extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {

				for (String date : trailReportData.keySet()) {
					trailJson += "{\"" + "TrailName" + "\":\"";
					trailJson += trailReportData.get(date);
					trailJson += "\", \"CreatedAt\":\"";
					trailJson += date.substring(0, 10) + "\"}";
					Log.e("POST JSON", trailJson);
					response = ServiceHandler.makePOSTCall(TRAILREPORT_POST_URL, trailJson);
					Log.e("TRAIL POST RESPONSE", response);
					trailJson = "";
				}
				// System.out.println(json);

			} catch (Exception e) {
				Log.e("POST ERROR", e.getMessage());
			}
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			

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

		// SearchManager searchManager = (SearchManager)
		// getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.searchOption).getActionView();
		// searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
		if (item.getItemId() == R.id.action_preferences) {
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
