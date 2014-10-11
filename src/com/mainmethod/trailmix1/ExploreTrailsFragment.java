package com.mainmethod.trailmix1;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mainmethod.trailmix1.EventsFragment.LoadEvents;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Event;
import com.mainmethod.trailmix1.sqlite.model.GeoPoint;
import com.mainmethod.trailmix1.sqlite.model.Placemark;
import com.mainmethod.trailmix1.sqlite.model.Trail;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class ExploreTrailsFragment extends Fragment {

	ProgressDialog pDialog;

	public ExploreTrailsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.explore_trails_fragment, null, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		new LoadEvents().execute();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView();
	}

	private void insertData() {
		DatabaseHelper db;
		db = new DatabaseHelper(getActivity().getApplicationContext());
		
		db.closeDB();
		
	}
   
	
	private void readData(){
		DatabaseHelper db;
		db = new DatabaseHelper(getActivity().getApplicationContext());
		//ArrayList<Placemark> placemarks = db.getTrailPlacemarks("Bruce Trail");
	    ArrayList<Event> events = db.getAllEvents();
	    
        TextView trailInfo = (TextView) getActivity().findViewById(R.id.trailInfo);
		trailInfo.setText(String.valueOf(events.size()) + "\n");
    	String s="";
//		for(Placemark p: placemarks){
//			s+= String.valueOf(p.getId())+",";
//		}
		trailInfo.append(s);
	
//	  for(String st: db.getAllTrails().keySet()){
//		  s += st + "\n";
//		  
//	  }
//	  trailInfo.setText(s);
	  db.close();
	  
		/*for(GeoPoint gp: points){
			System.out.println("Lat: " + gp.getLat() + "\t Lng: "+ gp.getLng());
			trailInfo.append("Lat: " + gp.getLat() + "\t Lng: "+ gp.getLng());
		}*/
		
	}
	
	public class LoadEvents extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading Events...");
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

			readData();
		}
	}
}
