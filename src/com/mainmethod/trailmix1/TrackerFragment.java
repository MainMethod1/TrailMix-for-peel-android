package com.mainmethod.trailmix1;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Event;
import com.mainmethod.trailmix1.sqlite.model.Session;
import com.mainmethod.trailmix1.sqlite.model.SessionGeopoint;
import com.mainmethod.trailmix1.tileprovider.CustomTileProvider;

import android.animation.ValueAnimator;
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
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrackerFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	ProgressDialog pDialog;
	String activity = "";
	String queryStatement = "";
	ArrayList<ArrayList<LatLng>> points = null;
	int updates = 0;
	PolylineOptions rectOptions;
	ArrayList<SessionGeopoint> currGeopoints;
	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;
	GoogleMap gMap;
	UiSettings mapSettings;
	Session currentSession;
	double distance;
	int time;
	SupportMapFragment mapFrag;
    Context c = null;
	TimerTask currSessionTimer;
	int s = 0;
	int m = 0;
	int secondsTaken = 0;
	double distanceTravelled = 0;
	Location lastLocation = null;

	double currSpeed = 0;

	boolean isFirstTime = false;
	TextView speedTxt;
	TextView distanceTxt;
	TextView timer;
	RelativeLayout relLayout;
	RelativeLayout bar;
	boolean calledAfterStop = true;

	private static final LatLng PEEL = new LatLng(43.6719449, -79.65912);

	// Name of shared preferences repository that stores persistent state
	public static final String SHARED_PREFERENCES = "com.mainmethod.trailmix.SHARED_PREFERENCES";

	// Key for storing the "updates requested" flag in shared preferences
	public static final String KEY_UPDATES_REQUESTED = "com.mainmethod.trailmix.KEY_UPDATES_REQUESTED";

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
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 5;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	// smallest distance traveled between updates in meters
	private static final int SMALLEST_DISPLACEMENT = 10;

	private static View v;

	public TrackerFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       c = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (v != null) {
			ViewGroup parent = (ViewGroup) v.getParent();
			if (parent != null)
				parent.removeView(v);
		}
		try {
			v = inflater.inflate(R.layout.tracker_fragment, container, false);
			isFirstTime = true;
		} catch (InflateException e) {

		}
		if (getArguments() != null) {
			if (getArguments().containsKey(MapActivity.ARG_TRACKER_FLAG)) {
				Log.i("ActLoadError", "intent contans activity extra");
				DatabaseHelper db = new DatabaseHelper(getActivity());
				
				if (getArguments().getString(MapActivity.ARG_TRACKER_FLAG).equals("hike")) {
					if (initMap())
						MapUtil.drawTrailMarkersByClass(gMap, "Hiking%", db);
					queryStatement = MapUtil.hikeStatement;
					activity = "hike";

				} else if (getArguments().getString(MapActivity.ARG_TRACKER_FLAG).equals("run")) {
					if (initMap())
						MapUtil.drawTrailMarkersByClass(gMap, "%", db);
					queryStatement = MapUtil.walkStatement;
					activity = "run";
				} else if (getArguments().getString(MapActivity.ARG_TRACKER_FLAG).equals("bike")) {
					if (initMap())
						MapUtil.drawTrailMarkersByClass(gMap, "Multi%", db);
					queryStatement = MapUtil.bikeStatement;
					activity = "bike";
				} else {
					// do nothing
					Log.i("ActLoadError",
							"not loading activity based markers but has "
									+ getArguments().getString(MapActivity.ARG_TRACKER_FLAG));
				}

			} else if (getArguments().containsKey(TrailDetailActivity.ARG_TRAIL_FLAG)) {
				DatabaseHelper db = new DatabaseHelper(getActivity());
				MapUtil.isComingFromTrailDetail = true;
				db.createTrailReportItem(getArguments().getString(TrailDetailActivity.ARG_TRAIL_FLAG));
				if (initMap())
					MapUtil.drawTrailByName(gMap, getArguments().getString(TrailDetailActivity.ARG_TRAIL_FLAG), db);
			}
		} else {
			Log.i("ArgLoadError", "fragment doesn't have arguments");
		}
		new LoadMap().execute();
		// v = inflater.inflate(R.layout.tracker_fragment, container, false);
		mLocationRequest = LocationRequest.create();
		// Inflate the layout for this fragment
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
		// Note that location updates are off until the user turns them on
		mUpdatesRequested = false;

		// Open Shared Preferences
		mPrefs = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

		// Get an editor
		mEditor = mPrefs.edit();

		speedTxt = (TextView) v.findViewById(R.id.txt_speed);
		distanceTxt = (TextView) v.findViewById(R.id.txt_distance);
		timer = (TextView) v.findViewById(R.id.timer);

		final ImageButton fab = (ImageButton) v.findViewById(R.id.fab_playPauseBtn);
		final ImageButton fab_stop = (ImageButton) v.findViewById(R.id.fab_stopBtn);
		bar = (RelativeLayout) v.findViewById(R.id.bar);

		fab_stop.setAlpha(0f);
		speedTxt.setAlpha(0);
		distanceTxt.setAlpha(0);
		timer.setAlpha(0);

		fab_stop.setY(dpToPx(-37));

		relLayout = (RelativeLayout) v.findViewById(R.id.bar);
		relLayout.getBackground().setAlpha(50);

		if (initMap()) {
			mapSettings = gMap.getUiSettings();
			gMap.setBuildingsEnabled(true);
			gMap.setMyLocationEnabled(true);
			if (!MapUtil.isComingFromTrailDetail) {
				gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PEEL, 11));
			} else {
				MapUtil.isComingFromTrailDetail = false;
			}

			mapSettings.setZoomControlsEnabled(false);

		}
		mLocationClient = new LocationClient(getActivity(), this, this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			// Call some material design APIs here

			// fab.setOutline(outline);
			ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
				@Override
				public void getOutline(View view, Outline outline) {
					// Or read size directly from the view's width/height
					int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
					// outline.setRoundRect(0, 0, size, size, size/2);
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
			public void onClick(View view) {

				if (!mUpdatesRequested) {
					fab.setSelected(true);
					currGeopoints = new ArrayList<SessionGeopoint>();
					startUpdates(view);
					Toast.makeText(getActivity(), "Started tracking", Toast.LENGTH_SHORT).show();
					startTimer(view);

					if (calledAfterStop) {

						// RelativeLayout.LayoutParams lp =
						// (RelativeLayout.LayoutParams) bar.getLayoutParams();
						// ResizeAnimation a = new ResizeAnimation(bar);
						//
						// // set the starting height (the current height) and
						// the new height that the view should have after the
						// animation
						// a.setParams(lp.height, dpToPx(100));
						// a.setDuration(1000);
						// bar.setAnimation(a);

						ValueAnimator anim = ValueAnimator.ofInt(bar.getMeasuredHeight(), dpToPx(100));
						anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
							@Override
							public void onAnimationUpdate(ValueAnimator valueAnimator) {
								int val = (Integer) valueAnimator.getAnimatedValue();
								ViewGroup.LayoutParams layoutParams = bar.getLayoutParams();
								layoutParams.height = val;
								bar.setLayoutParams(layoutParams);
							}
						});
						anim.setDuration(1000);
						anim.start();

						fab_stop.animate().alpha(1).setStartDelay(1000);
					
					
//						fab.animate().
						speedTxt.animate().alpha(1).setStartDelay(1000);
						distanceTxt.animate().alpha(1).setStartDelay(1000);
						timer.animate().alpha(1).setStartDelay(1000);
						fab.animate().translationYBy(dpToPx(-38)).setDuration(1000);
						// fab_stop.animate().translationYBy(dpToPx(-37)).setDuration(1000).start();;

					}
					calledAfterStop = false;
					// Bar Animation:

					// getting the layoutparams might differ in your
					// application, it depends on the parent layout

					// isFirstTime = false;
				} else {

					stopTimer(view);
					fab.setSelected(false);

					stopUpdates(view);
					Toast.makeText(getActivity(), "Stopped tracking", Toast.LENGTH_SHORT).show();
				}
			}
		});

		fab_stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// animations

				Runnable endAction = new Runnable() {
					public void run() {

						ValueAnimator anim = ValueAnimator.ofInt(bar.getMeasuredHeight(), dpToPx(55));
						anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
							@Override
							public void onAnimationUpdate(ValueAnimator valueAnimator) {
								int val = (Integer) valueAnimator.getAnimatedValue();
								ViewGroup.LayoutParams layoutParams = bar.getLayoutParams();
								layoutParams.height = val;
								bar.setLayoutParams(layoutParams);
							}
						});
						anim.setDuration(1000);

						anim.start();
						fab.animate().translationYBy(dpToPx(38)).setDuration(1000);
					}
				};
				speedTxt.animate().alpha(0);
				distanceTxt.animate().alpha(0);
				timer.animate().alpha(0);

				fab_stop.animate().alpha(0).withEndAction(endAction);

				// fab_stop.animate().translationYBy(dpToPx(37)).setDuration(1000).start();;

				calledAfterStop = true;

				if (currGeopoints.size() > 0) {
					time = (m * 60) + s;

					DatabaseHelper db = new DatabaseHelper(getActivity());
					long count = db.getRowCount("sessions") + 1;
					currentSession = new Session();
					currentSession.setDistance(Double.parseDouble(String.format("%.2f", distance)));
					currentSession.setTime(time);
					currentSession.setSpeed(distance / time);
					DateFormat dFormat = DateFormat.getDateTimeInstance();
					dFormat.setTimeZone(TimeZone.getTimeZone("GMT-05:00"));
					Date now = new Date();
					currentSession.setCreated_at(dFormat.format(now));

					db.createSession(currentSession);
					SessionGeopoint point;

					for (SessionGeopoint sgp : currGeopoints) {
						sgp.setSession_id((int) count);
						db.createSessionGeopoint(sgp);

					}
				}

				stopTimer(v);
				fab.setSelected(false);
				stopUpdates(v);
				resetValues();
			}

			private void resetValues() {
				distance = 0;
				time = 0;
				s = 0;
				m = 0;
				distanceTravelled = 0;
				secondsTaken = 0;
				currSpeed = 0;
				lastLocation = null;
				updates = 0;

				speedTxt.setText("0.0 km/h");
				timer.setText("0:00");
				distanceTxt.setText("0m");
				currentSession = null;
			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView();
	}

	public class LoadMap extends AsyncTask<Void, Void, Boolean> {

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
			DatabaseHelper db = new DatabaseHelper(getActivity());
			points = MapUtil.setPoints(db, queryStatement);
			// insertData();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (activity.equals("bike")) {
				gMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomTileProvider(points, Color.rgb(0,
						0, 153))));

			} else if (activity.equals("hike")) {
				gMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomTileProvider(points, Color.rgb(255,
						0, 0))));
			} else if (activity.equals("run")) {
				gMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomTileProvider(points, Color.rgb(0,
						102, 51))));
			}
			pDialog.dismiss();

		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		Fragment mapFragment = getChildFragmentManager().findFragmentById(R.id.trackerMap);
//		if (mapFragment != null) {
//			getActivity().getSupportFragmentManager().beginTransaction().remove(mapFragment).commitAllowingStateLoss();
//		}
	}

	@Override
	public void onStop() {
		if (mLocationClient.isConnected()) {
			mUpdatesRequested = false;
			mEditor.putBoolean(KEY_UPDATES_REQUESTED, mUpdatesRequested);
			mEditor.commit();
			stopPeriodicUpdates();

		}

		mLocationClient.disconnect();
		super.onStop();
	}

	/*
	 * Called when the Activity is going into the background. Parts of the UI
	 * may be visible, but the Activity is inactive.
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
		 * Connect the client. Don't re-start any requests here; instead, wait
		 * for onResume()
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
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("TRACKER", "Successfully loaded Google API");

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			// Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
			// this, 0);
			// if (dialog != null) {
			// ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			// errorFragment.setDialog(dialog);
			// errorFragment.show(getSupportFragmentManager(),
			// LocationUtils.APPTAG);
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
	 * @param location
	 *            The updated location.
	 */
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

		// gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new
		// LatLng(arg0.getLatitude(),arg0.getLongitude()), 15));
		currGeopoints.add(new SessionGeopoint(arg0.getLatitude(), arg0.getLongitude()));
		rectOptions = new PolylineOptions();

		rectOptions.add(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
		updates++;
		if (updates > 1) {
			rectOptions.add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));

			Polyline polyline = gMap.addPolyline(rectOptions);
			polyline.setColor(Color.MAGENTA);
			polyline.setWidth(8);
			polyline.setVisible(true);

			distanceTravelled = (double) arg0.distanceTo(lastLocation);
			distance += distanceTravelled;

			distanceTxt.setText(String.format("%.2f", distance) + "m");

		}

		lastLocation = arg0;

		// gMap.addMarker(new MarkerOptions().position(new
		// LatLng(arg0.getLatitude(),arg0.getLongitude())));
		// Toast.makeText(getActivity(),"Tracking",Toast.LENGTH_SHORT).show();
		CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(
				new LatLng(arg0.getLatitude(), arg0.getLongitude()), 17);
		gMap.animateCamera(cameraPosition);
	}

	/**
	 * Invoked by the "Start Updates" button Sends a request to start location
	 * updates
	 *
	 * @param v
	 *            The view object associated with this method, in this case a
	 *            Button.
	 */
	public void startUpdates(View v) {
		mUpdatesRequested = true;

		if (servicesConnected()) {
			startPeriodicUpdates();
		}
	}

	/**
	 * Invoked by the "Stop Updates" button Sends a request to remove location
	 * updates request them.
	 *
	 * @param v
	 *            The view object associated with this method, in this case a
	 *            Button.
	 */
	public void stopUpdates(View v) {
		mUpdatesRequested = false;

		if (servicesConnected()) {
			stopPeriodicUpdates();
		}
	}

	/**
	 * In response to a request to start updates, send a request to Location
	 * Services
	 */
	private void startPeriodicUpdates() {

		mLocationClient.requestLocationUpdates(mLocationRequest, this);

	}

	/**
	 * In response to a request to stop updates, send a request to Location
	 * Services
	 */
	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);

	}

	private boolean initMap() {
		if (gMap == null) {
		    FragmentManager fm = getChildFragmentManager();
			mapFrag = (SupportMapFragment) fm.findFragmentById(R.id.trackerMap);
			if (mapFrag == null) {
				mapFrag = SupportMapFragment.newInstance();
//				getChildFragmentManager().beginTransaction().replace(R.id.container_map, mapFrag).commit();
			}
			gMap = mapFrag.getMap();
		}
		return (gMap != null);
	}

	// Timer logic
	public void startTimer(View view) {
		final Handler handler = new Handler();
		Timer ourtimer = new Timer();
		currSessionTimer = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						s++;
						secondsTaken++;
						if (s > 60) {
							s = 0;
							m++;
						}
						if (s > 9) {
							timer.setText(m + ":" + s);
						} else {
							timer.setText(m + ":0" + s);
						}

						if (distanceTravelled != 0) {
							currSpeed = distanceTravelled / secondsTaken;
							speedTxt.setText(String.format("%.2f", currSpeed * 3.6) + " km/h");
							distanceTravelled = 0;
							secondsTaken = 0;
						} else if (secondsTaken > 15) {
							currSpeed = 0;
							secondsTaken = 0;
							speedTxt.setText("0.0 km/h");
						}

					}
				});
			}
		};

		ourtimer.schedule(currSessionTimer, 0, 1000);

	}

	public void stopTimer(View view) {
		if (currSessionTimer != null) {
			currSessionTimer.cancel();
		}
		currSessionTimer = null;
		// s = 0;
		// m = 0;
	}

	public int dpToPx(int dp) {
		DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

}
