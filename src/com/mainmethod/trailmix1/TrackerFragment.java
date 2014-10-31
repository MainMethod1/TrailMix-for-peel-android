package com.mainmethod.trailmix1;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Event;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Outline;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TrackerFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener {
	ProgressDialog pDialog;
	
	int updates = 0;
	PolylineOptions rectOptions;
	
	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;
	GoogleMap gMap;
    // Name of shared preferences repository that stores persistent state
    public static final String SHARED_PREFERENCES =
            "com.example.android.location.SHARED_PREFERENCES";

    // Key for storing the "updates requested" flag in shared preferences
    public static final String KEY_UPDATES_REQUESTED =
            "com.example.android.location.KEY_UPDATES_REQUESTED";
    
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;
    
    boolean mUpdatesRequested = false;
	
	// Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 2;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	
	public TrackerFragment() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tracker_fragment, container, false);
		mLocationRequest = LocationRequest.create();
		// Inflate the layout for this fragment
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		
		// Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Open Shared Preferences
        mPrefs = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();

		if(initMap()){
			
			gMap.setBuildingsEnabled(true);
			gMap.setMyLocationEnabled(true);
		  
		}
		mLocationClient = new LocationClient(getActivity(), this, this);
		final ImageButton fab = (ImageButton) v.findViewById(R.id.fab_playPauseBtn);;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		    // Call some material design APIs here
			
		      //fab.setOutline(outline);  
		    ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
		           @Override
		            public void getOutline(View view, Outline outline) {
		                 // Or read size directly from the view's width/height
		                 int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
		                 //outline.setRoundRect(0, 0, size, size, size/2);
		                 outline.setOval(0, 0, size, size);
		            }
		           };
		           fab.setClipToOutline(true);
		   fab.setOutlineProvider(viewOutlineProvider);
		} else {
		    // Implement this feature without material design
			
		}
		 fab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
		         if(!mUpdatesRequested){
		        	 fab.setSelected(true);
		        	 startUpdates(v);
		        	 Toast.makeText(getActivity(),"Started tracking",Toast.LENGTH_SHORT).show();
		         } else{
		        	 fab.setSelected(false);
		        	 stopUpdates(v);
		        	 Toast.makeText(getActivity(),"Stopped tracking",Toast.LENGTH_SHORT).show();
		         }
				}
			});
		
		
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 getView();
	}
	
	
   

	
	public class LoadEvents extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading Trails...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			//insertData();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();

		}
	}

    @Override
    public void onStop(){
    	if(mLocationClient.isConnected()){
    		stopPeriodicUpdates();
    	}
    	
    	mLocationClient.disconnect();
    	super.onStop();
    }
    
    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {

        // Save the current setting for updates
        mEditor.putBoolean(KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();

        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {

        super.onStart();

        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mLocationClient.connect();

    }
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();

        // If the app already has a setting for getting location updates, get it
        if (mPrefs.contains(KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(KEY_UPDATES_REQUESTED, false);

        // Otherwise, turn off location updates until requested
        } else {
            mEditor.putBoolean(KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }

    }
  
    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("TRACKER", "Successfully loaded Google API");

            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
//            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
//            if (dialog != null) {
//                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
//                errorFragment.setDialog(dialog);
//                errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
            }
            return false;
        }
    
    
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
    
	  /**
     * Report location updates to the UI.
     *
     * @param location The updated location.
     */
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
		
		if(updates == 0)
		{
			rectOptions = new PolylineOptions();
		}
		rectOptions.add(new LatLng(arg0.getLatitude(),arg0.getLongitude()));
		if(updates == 2)
		{
			updates = 0;
			Polyline polyline = gMap.addPolyline(rectOptions);
			polyline.setColor(Color.RED);
			polyline.setWidth(4);
			polyline.setVisible(true);
		}
	
		updates++;
	   // gMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(),arg0.getLongitude())));
		Toast.makeText(getActivity(),"Tracking",Toast.LENGTH_SHORT).show();
		CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(),arg0.getLongitude()));
		gMap.animateCamera(cameraPosition);
	}
	/**
     * Invoked by the "Start Updates" button
     * Sends a request to start location updates
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void startUpdates(View v) {
        mUpdatesRequested = true;

        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }

    /**
     * Invoked by the "Stop Updates" button
     * Sends a request to remove location updates
     * request them.
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void stopUpdates(View v) {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
        
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
      
    }
    
	private boolean initMap() {
		if (gMap == null) {
			SupportMapFragment mapFrag = (SupportMapFragment) getActivity().getSupportFragmentManager()
					.findFragmentById(R.id.trackerMap);
			gMap = mapFrag.getMap();
		}
		return (gMap != null);

	}

}
