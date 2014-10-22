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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ExploreTrailsFragment extends ListFragment {
    DatabaseHelper db;
	ProgressDialog pDialog;
    ArrayList<Trail> tList;
	public ExploreTrailsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		new LoadTrails().execute();
		return inflater.inflate(R.layout.explore_trails_fragment, null, false);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView();
	}
    
	

	private class TrailItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			// selectItem(position);
			Trail t = (Trail) parent.getItemAtPosition(position);

			Intent detailIntent = new Intent(getActivity(),
					TrailDetailActivity.class);
			detailIntent.putExtra(TrailDetailFragment.ARG_TRAIL_NAME,
					t.getTrailName());
			startActivity(detailIntent);
			// Toast.makeText(getActivity().getApplicationContext(),
			// e.getContactName() + e.getContactEmail(),
			// Toast.LENGTH_SHORT).show();

		}
	}
	
	public class LoadTrails extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading trails...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			db = new DatabaseHelper(getActivity());
			tList = new ArrayList<Trail>(db.getAllTrailsWithInfo().values());

			db.close();

			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			updateList();

		}

		private void updateList() {
			// TODO Auto-generated method stub
			TrailListAdapter trailAdapter = new TrailListAdapter(getActivity(),
					tList);
			setListAdapter(trailAdapter);
			final ListView lv = getListView();
			lv.setOnItemClickListener(new TrailItemClickListener());
		}
	}
	
}
