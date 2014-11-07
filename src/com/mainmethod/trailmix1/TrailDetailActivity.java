package com.mainmethod.trailmix1;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class TrailDetailActivity extends FragmentActivity {

	public TrailDetailActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trail_detail);  //create this
		
		// to make the statusbar tinted in API 19 or above, won't make any
				// difference in other devices
				SystemBarTintManager tintManager = new SystemBarTintManager(this);
				// enable status bar tint
				tintManager.setStatusBarTintEnabled(true);
				// enable navigation bar tint
				tintManager.setNavigationBarTintEnabled(true);
				tintManager.setTintColor(Color.parseColor("#00796b"));
				
				
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(TrailDetailFragment.ARG_TRAIL_NAME, getIntent()
					.getStringExtra(TrailDetailFragment.ARG_TRAIL_NAME));
			TrailDetailFragment fragment = new TrailDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.activity_trail_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_tracker) {
			Intent i = new Intent(this,MainActivity.class);
			startActivity(i);
			return true;
		} else if(id == android.R.id.home){
			NavUtils.navigateUpTo(this, new Intent(this,
					MainActivity.class));
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}
}
