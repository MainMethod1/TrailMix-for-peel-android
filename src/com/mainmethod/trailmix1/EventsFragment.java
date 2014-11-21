package com.mainmethod.trailmix1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;

import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Event;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class EventsFragment extends ListFragment {

	private static final String SERVICE_URL = "http://142.55.49.125/trailmixadmin/api/eventsapi";

	private static final String TAG_EventName = "eventName";
	private static final String TAG_EventLocation = "eventLocation";
	private static final String TAG_EventInfo = "eventInfo";
	private static final String TAG_EventDatetime = "eventDateTime";
	private static final String TAG_ID = "Id";
	private static final String TAG_URL = "eventUrl";
	private static final String TAG_City = "Cities";
	private static final String TAG_URL_TEXT = "urlText";
	private static final String TAG_START_TIME = "startTime";
	private static final String TAG_END_TIME = "endTime";
	private static final String TAG_ISALLDAY = "isAllDay";
	private static final String TAG_CONTACT = "contactName";
	private static final String TAG_CONTACT_EMAIL = "contactEmail";
	private static final String TAG_POSTERURL = "posterUrl";
	SwipeRefreshLayout swipeLayout;
	DatabaseHelper db;
	private ArrayList<Event> eList;
	private ProgressDialog pDialog;
	private Handler handler = new Handler();

	public EventsFragment() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		new LoadEvents().execute();
		return inflater.inflate(R.layout.events_fragment, container, false);

	}

	@Override
	public void onResume() {
		super.onResume();

		swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				handler.post(refreshing);
				new UpdateEvents().execute();
//				swipeLayout.setRefreshing(false);
			}
		});
		
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

	}

	private final Runnable refreshing = new Runnable() {

		public void run() {
			try {
				if (swipeLayout.isRefreshing()) {
					handler.postDelayed(this, 6000);
				} else {

					swipeLayout.setRefreshing(false);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	};

	public void updateEventDatabase(ArrayList<Event> eventCollection) {
		db = new DatabaseHelper(getActivity());
		db.deleteAllEvent();
		for (Event e : eventCollection) {
			db.createEvent(e);
		}
		eList = db.getAllEvents();
		db.close();
	}

	public ArrayList<Event> getJSONData() {
		// mEventList = new ArrayList<HashMap<String, String>>();
		ArrayList<Event> eventList = new ArrayList<Event>();
		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();

		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(SERVICE_URL, ServiceHandler.GET);

		Log.d("Response: ", "> " + jsonStr);

		if (jsonStr != null) {
			try {
				JSONArray jsonArray = new JSONArray(jsonStr);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);

					//JSONArray cityArray = jsonObj.getJSONArray(TAG_City);
//					if (cityArray.length() != 0) {
//
//					}
					int id = Integer.parseInt(jsonObj.getString(TAG_ID));
					String title = jsonObj.getString(TAG_EventName);
					String location = jsonObj.getString(TAG_EventLocation);
					String desc = jsonObj.getString(TAG_EventInfo);
					String dateTime = jsonObj.getString(TAG_EventDatetime);
					String url = jsonObj.getString(TAG_URL);
					String url_text = jsonObj.getString(TAG_URL_TEXT);
					String startTime = jsonObj.getString(TAG_START_TIME);
					String endTime = jsonObj.getString(TAG_END_TIME);
					boolean isAllDay = Boolean.parseBoolean(jsonObj.getString(TAG_ISALLDAY));
					String contactName = jsonObj.getString(TAG_CONTACT);
					String contactEmail = jsonObj.getString(TAG_CONTACT_EMAIL);
					String posterUrl = jsonObj.getString(TAG_POSTERURL);

					String date = null;
					if (dateTime.length() > 10) {
						date = dateTime.substring(0, 10);
					}

					String s_time = startTime.substring(11, 19);
					String e_time = endTime.substring(11, 19);

					eventList.add(new Event(title, id, desc, url, url_text, date, s_time, e_time, isAllDay, location,
							contactName, contactEmail, posterUrl));

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return eventList;

	}

	private void updateList() {

		EventListAdapter eventAdapter = new EventListAdapter(getActivity(), eList);
		setListAdapter(eventAdapter);

		final ListView lv = getListView();
		lv.setOnItemClickListener(new EventItemClickListener());
		// lv.setOnClickListener();
		lv.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean enable = false;
				if (lv != null && lv.getChildCount() > 0) {
					boolean firstItemVisible = lv.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = lv.getChildAt(0).getTop() == 0;
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				swipeLayout.setEnabled(true);
			}
		});
		lv.setTextFilterEnabled(true);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView();
	}

	private class EventItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			// selectItem(position);
			Event e = (Event) parent.getItemAtPosition(position);

			Intent detailIntent = new Intent(getActivity(), EventDetailActivity.class);
			detailIntent.putExtra(EventDetailFragment.ARG_EVENT_NAME, e.getTitle());
			startActivity(detailIntent);
			// Toast.makeText(getActivity().getApplicationContext(),
			// e.getContactName() + e.getContactEmail(),
			// Toast.LENGTH_SHORT).show();

		}
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
			db = new DatabaseHelper(getActivity());
			eList = db.getAllEvents();
			if (eList.size() == 0) {
				updateEventDatabase(getJSONData());
			}

			db.close();

			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			updateList();

		}
	}

	public class UpdateEvents extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			updateEventDatabase(getJSONData());

			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			updateList();
			swipeLayout.setRefreshing(false);

		}
	}
}
