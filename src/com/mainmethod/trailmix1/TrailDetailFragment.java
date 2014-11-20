package com.mainmethod.trailmix1;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Trail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrailDetailFragment extends Fragment {
	public static final String ARG_TRAIL_NAME = "name";
	private Trail trailItem;
	GoogleMap googleMap;
	SupportMapFragment mapFrag;
	UiSettings mapSettings;
	DatabaseHelper db ;
	
	private static final LatLng PEEL = new LatLng(43.6719449, -79.65912);

	public TrailDetailFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_TRAIL_NAME)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			// mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
			// ARG_ITEM_ID));
			trailItem = getTrail(getArguments().getString(ARG_TRAIL_NAME));

		}
		// Fragment subFragment =
		// getChildFragmentManager().findFragmentById(R.id.map);

	}

	private Trail getTrail(String argTrailName) {
		DatabaseHelper db = new DatabaseHelper(getActivity());
		Trail trail = db.getTrailByName(argTrailName);
		db.close();
		return trail;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_trail_detail, container, false);
		LinearLayout titleBar = (LinearLayout) rootView.findViewById(R.id.title_bar);
		titleBar.getBackground().setAlpha(40);
		
		// Show the dummy content as text in a TextView.
		if (trailItem != null) {
			((TextView) rootView.findViewById(R.id.txt_trailName)).setText(trailItem.getTrailName());
			((TextView) rootView.findViewById(R.id.txt_trailType)).setText(trailItem.getTrailClass());
			((TextView) rootView.findViewById(R.id.txt_trailLength)).setText(String.valueOf(trailItem.getLength())+" km");
			((TextView) rootView.findViewById(R.id.txt_trailSurface)).setText(trailItem.getSurface());
			((TextView) rootView.findViewById(R.id.txt_amenities)).setText(trailItem.getAmenities());
			((TextView) rootView.findViewById(R.id.txt_parking)).setText(trailItem.getParking());
			((TextView) rootView.findViewById(R.id.txt_seasonHours)).setText(trailItem.getSeasonHours());
			((TextView) rootView.findViewById(R.id.txt_lighting)).setText(trailItem.getLighting());
			((TextView) rootView.findViewById(R.id.txt_maintenance)).setText(trailItem.getWinterMaintenance());
			((TextView) rootView.findViewById(R.id.txt_pets)).setText(trailItem.getPets());
			((TextView) rootView.findViewById(R.id.txt_notes)).setText(trailItem.getNotes());

		}
		mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFrag == null) {
			mapFrag = SupportMapFragment.newInstance();
			getChildFragmentManager().beginTransaction().replace(R.id.container_map, mapFrag).commit();
		}

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		db = new DatabaseHelper(getActivity());
		if (googleMap == null) {
			googleMap = mapFrag.getMap();
			// googleMap.addMarker(new MarkerOptions().position(new LatLng(0,
			// 0)));
			mapSettings = googleMap.getUiSettings();
			googleMap.setBuildingsEnabled(true);
//			googleMap.setMyLocationEnabled(true);
			
			LatLng trailMarker = new LatLng(trailItem.getMidPointLat(),trailItem.getMidPointLng());
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trailMarker, 13));
//			googleMap.addMarker(new MarkerOptions()
//			 .position(trailMarker)
//			 .title(trailItem.getTrailName()));
			
			
			googleMap.setOnMapClickListener( new OnMapClickListener(){

				@Override
				public void onMapClick(LatLng arg0) {
					// TODO Auto-generated method stub
					db.createTrailReportItem(trailItem.getTrailName());
					Intent i = new Intent(getActivity(),MainActivity.class);
					i.putExtra(TrailDetailActivity.ARG_TRAIL_FLAG,
							trailItem.getTrailName());
					startActivity(i);
				}
				
			});
			
			mapSettings.setZoomControlsEnabled(false);
			
			
			
			MapUtil.drawTrailByName(googleMap, trailItem.getTrailName(), db);
			db.closeDB();
		}
	}

	
}
