/**
 * 
 */
package com.mainmethod.trailmix1.preferences;

import android.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

/**
 * @author D4RK
 *
 */
public class PrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(com.mainmethod.trailmix1.R.xml.preferences);
       
    }
 
   
}
