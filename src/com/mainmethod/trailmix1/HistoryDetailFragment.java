/**
 * 
 */
package com.mainmethod.trailmix1;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Session;
import com.mainmethod.trailmix1.sqlite.model.SessionGeopoint;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author D4RK
 *
 */
public class HistoryDetailFragment extends Fragment {

	public static final String ARG_SESSION_ID = "id";
	private Session session;
	private static View v;
	GoogleMap gMap;
	UiSettings mapSettings;
	ArrayList<SessionGeopoint> sessionGeopoints;
	SupportMapFragment mapFrag;
	PolylineOptions rectOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_SESSION_ID)) {
			if (getArguments().getString(ARG_SESSION_ID) != null) {
				Log.i("HistoryDetailActivity", "id is " + getArguments().getString(ARG_SESSION_ID));
				session = getSession(Integer.parseInt(getArguments().getString(ARG_SESSION_ID)));
				sessionGeopoints = getSessionGeopoints(session.getId());

			} else {
				Log.i("HistoryDetailActivity", "id is null");
			}
		}
	}

	private Session getSession(int id) {
		DatabaseHelper db = new DatabaseHelper(getActivity());
		Session session = db.getSessionById(id);
		db.close();
		return session;

	}

	private ArrayList<SessionGeopoint> getSessionGeopoints(int sessionID) {
		DatabaseHelper db = new DatabaseHelper(getActivity());
		sessionGeopoints = db.getSessionGeopoints(sessionID);
		return sessionGeopoints;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//		if (v != null) {
//			ViewGroup parent = (ViewGroup) v.getParent();
//			if (parent != null)
//				parent.removeView(v);
//		}
//		try {
//			v = inflater.inflate(R.layout.fragment_history_detail, container, false);
//
//		} catch (InflateException e) {
//
//		}

		v = inflater.inflate(R.layout.fragment_history_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (session != null) {
			((TextView) v.findViewById(R.id.txt_session_speed)).setText(String.format("%.2f",session.getSpeed()) + " km/h");
			((TextView) v.findViewById(R.id.txt_session_distance)).setText(String.format("%.2f",session.getDistance())
					+ " meters");
			((TextView) v.findViewById(R.id.txt_session_duration)).setText(MapUtil.formatTime(session.getTime())
					+ " minutes");

		}
		mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFrag == null) {
			mapFrag = SupportMapFragment.newInstance();
			getChildFragmentManager().beginTransaction().replace(R.id.container_map_history, mapFrag).commit();
		}

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (gMap == null) {
			gMap = mapFrag.getMap();
			// googleMap.addMarker(new MarkerOptions().position(new LatLng(0,
			// 0)));
			mapSettings = gMap.getUiSettings();
			gMap.setBuildingsEnabled(true);
			mapSettings.setZoomControlsEnabled(false);
			// googleMap.setMyLocationEnabled(true);

			// LatLng trailMarker = new
			// LatLng(trailItem.getMidPointLat(),trailItem.getMidPointLng());
			
			// googleMap.addMarker(new MarkerOptions()
			// .position(trailMarker)
			// .title(trailItem.getTrailName()));
			ArrayList<LatLng> latlngs = new ArrayList<LatLng>();
			for (SessionGeopoint point : sessionGeopoints) {
				latlngs.add(new LatLng(point.getLat(), point.getLng()));
			}
			rectOptions = new PolylineOptions();
			rectOptions.addAll(latlngs);
			Polyline polyline = gMap.addPolyline(rectOptions);
			polyline.setColor(Color.MAGENTA);
			polyline.setWidth(8);
			polyline.setVisible(true);
			if(latlngs.size() > 0){
			 gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngs.get(0),
					 15));
			}else{
				 gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.6719449, -79.65912),
						 14));
			}
		}
	}

}
