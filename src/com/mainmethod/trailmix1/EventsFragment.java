package com.mainmethod.trailmix1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class EventsFragment extends ListFragment {

	private static final String SERVICE_URL = "http://142.55.49.125/trailmixadmin/api/eventapi";

	
	private static final String TAG_EventName = "eventName";
	private static final String TAG_EventLocation = "eventLocation";
	private static final String TAG_EventInfo = "eventInfo";
	private static final String TAG_EventDatetime = "eventDateTime";
	private static final String TAG_ID = "Id";
	private static final String TAG_City = "Cities";

	private ArrayList<HashMap<String, String>> mEventList;
	private ProgressDialog pDialog;
	public EventsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.events_fragment, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		new LoadEvents().execute();
	}

	public void updateJSONData() {
		mEventList = new ArrayList<HashMap<String, String>>();

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

					JSONArray cityArray = jsonObj.getJSONArray(TAG_City);
					if (cityArray.length() != 0) {

					}
					String id = jsonObj.getString(TAG_ID);
					String name = jsonObj.getString(TAG_EventName);
					String location = jsonObj.getString(TAG_EventLocation);
					String info = jsonObj.getString(TAG_EventInfo);
					String dateTime = jsonObj.getString(TAG_EventDatetime);
                    String date =  dateTime.substring(0, 10);
                    String time = dateTime.substring(11, 18);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(TAG_EventName, name);
					map.put(TAG_EventLocation, location);
					map.put(TAG_EventInfo, location);
					map.put(TAG_EventDatetime, date);
                    
					mEventList.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	private void updateList() {
       

		ListAdapter adapter = new SimpleAdapter(getActivity(), mEventList,
		        R.layout.events_list_item, 
		        new String[] { TAG_EventName, TAG_EventLocation, TAG_EventDatetime}, 
		        new int[] { R.id.eventName, R.id.location, R.id.date});



		            setListAdapter(adapter);
		            

		            ListView lv = getListView();    
		            lv.setTextFilterEnabled(true);
		            
//	               lv.setOnItemClickListener(new OnItemClickListener() {
//		                @Override
//		                public void onItemClick(AdapterView<?> parent, View view,
//		                        int position, long id) {
//		                	
//		                }
//		            });
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
	        pDialog.setMessage("Loading Events...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }
	    @Override
	    protected Boolean doInBackground(Void... arg0) {

	    	updateJSONData();
	        return null;

	    }


	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        pDialog.dismiss();

	        updateList();
	        }
	      }
	   
	}
	