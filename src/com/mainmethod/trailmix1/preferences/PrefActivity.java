/**
 * 
 */
package com.mainmethod.trailmix1.preferences;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * @author D4RK
 * 
 */
public class PrefActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// to make the statusbar tinted in API 19 or above, won't make any
		// difference in other devices
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(Color.parseColor("#00796b"));

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefFragment()).commit();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
