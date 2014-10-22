/**
 * 
 */
package com.mainmethod.trailmix1.preferences;

import android.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> This class simply extends the PreferenceFragment from the API
 * and loads the custom preferences.xml in the xml folder</h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class PrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(com.mainmethod.trailmix1.R.xml.preferences);
       
    }
 
   
}
