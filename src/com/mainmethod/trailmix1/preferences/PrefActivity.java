/**
 * 
 */
package com.mainmethod.trailmix1.preferences;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> This class contains functionality for a user to select which trail they
 * wish to have displayed on the map.  Moreover, this class is in conjunction
 * with the PrefFragment class </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class PrefActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// To make the statusbar tinted in API 19 or above. Otherwise, it
		// won't make any difference in other devices
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// Enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// Enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);
		// Set tint color
		tintManager.setTintColor(Color.parseColor("#00796b"));

		// Display the PrefFragment class as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefFragment()).commit();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
