/**
 * 
 */
package com.mainmethod.trailmix1.preferences;

import android.app.Activity;
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

	        // Display the fragment as the main content.
	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new PrefFragment())
	                .commit();
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	    }
	   
	   @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			if(item.getItemId() == android.R.id.home){
				finish();
			}
			return super.onOptionsItemSelected(item);
		}
}
