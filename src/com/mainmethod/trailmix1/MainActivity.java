package com.mainmethod.trailmix1;

import com.mainmethod.trailmix1.preferences.PrefActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	private FragmentNavigationDrawer dlDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_navigationdrawer);

		// to make the statusbar tinted in API 19 or above, won't make any
		// difference in other devices
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(Color.parseColor("#00796b"));

		// Find our drawer view
		dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
		// Setup drawer view
		dlDrawer.setupDrawerConfiguration(
				(ListView) findViewById(R.id.left_drawer),
				R.layout.activity_main_navigationdrawerlistitem,
				R.id.content_frame);
		// Add nav items
		dlDrawer.addNavItem("Home", R.drawable.ic_drawer_home, "TrailMix",
				HomeFragment.class);
		dlDrawer.addNavItem("Tracker", R.drawable.ic_drawer_tracker, "Tracker",
				TrackerFragment.class);
		dlDrawer.addNavItem("Events", R.drawable.ic_drawer_events, "Events",
				EventsFragment.class);
		dlDrawer.addNavItem("Explore Trails", R.drawable.ic_drawer_explore_trails,
				"Explore Trails", ExploreTrailsFragment.class);
		// Select default
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
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}
		
		
		//opens preferences from the actionbar
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
