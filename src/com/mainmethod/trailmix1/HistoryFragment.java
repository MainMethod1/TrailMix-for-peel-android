/**
 * 
 */
package com.mainmethod.trailmix1;

import java.util.ArrayList;

import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Session;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * @author D4RK
 *
 */
public class HistoryFragment extends ListFragment {
	
	ProgressDialog pDialog;
	DatabaseHelper db;
	ArrayList<Session> sessionList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		new LoadTrails().execute();
		return inflater.inflate(R.layout.fragment_history, null, false);
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
    
	

	private class HistoryItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			// selectItem(position);
			Session t = (Session) parent.getItemAtPosition(position);

			Intent detailIntent = new Intent(getActivity(),
					HistoryDetailActivity.class);
			detailIntent.putExtra(HistoryDetailFragment.ARG_SESSION_ID,
					String.valueOf(t.getId()));
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
			sessionList = new ArrayList<Session>(db.getAllSessions());

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
			HistoryListAdapter trailAdapter = new HistoryListAdapter(getActivity(),
					sessionList);
			setListAdapter(trailAdapter);
			final ListView lv = getListView();
			lv.setOnItemClickListener(new HistoryItemClickListener());
		}
	}
	
	
}
