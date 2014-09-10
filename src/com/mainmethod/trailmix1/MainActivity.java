package com.mainmethod.trailmix1;

//import com.mainmethod.trailmix1.gmapsapp.R;

import com.mainmethod.trailmix1.R;
import com.mainmethod.trailmix1.preferences.PrefActivity;

import android.app.Activity;
//import android.app.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
	private String[] menuItems;
	private ListView listView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_navigationdrawer);

		menuItems = getResources().getStringArray(R.array.menu_items);
		listView = (ListView) findViewById(R.id.left_drawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mTitle = mDrawerTitle = getTitle();
		listView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.activity_main_navigationdrawerlistitem, menuItems));

		// listView.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
			}
		};
		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		final Button button_run = (Button) findViewById(R.id.button_run);
		button_run.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				openMap(v);
			}
		});
		
		final Button button_bike = (Button) findViewById(R.id.button_bike);
		button_bike.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				openMap(v);
			}
		});
		
		final Button button_hike = (Button) findViewById(R.id.button_hike);
		button_hike.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				openMap(v);
			}
		});
		

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_preferences:
			Intent settingsIntent = new Intent(this, PrefActivity.class);
			startActivity(settingsIntent);
			break;
	
		default:
			break;

		}
		return super.onOptionsItemSelected(item);

	}

	public void openMap(View v) {

		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}

}
