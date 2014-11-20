package com.mainmethod.trailmix1;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Event;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventDetailFragment extends Fragment {
	public static final String ARG_EVENT_NAME = "name";
	private Event eventItem;

	public EventDetailFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_EVENT_NAME)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			// mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
			// ARG_ITEM_ID));
			eventItem = getEvent(getArguments().getString(ARG_EVENT_NAME));
		}
	}

	private Event getEvent(String argEventName) {
		DatabaseHelper db = new DatabaseHelper(getActivity());
		Event event = db.getEventByName(argEventName);
		db.close();
		return event;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_detail,
				container, false);
		// Show the dummy content as text in a TextView.
		if (eventItem != null) {
			((TextView) rootView.findViewById(R.id.eventTitle))
					.setText(eventItem.getTitle());
			((TextView) rootView.findViewById(R.id.eventDesc))
			.setText(eventItem.getDesc());
			((TextView) rootView.findViewById(R.id.eventContact))
			.setText(eventItem.getContactName());
			((TextView) rootView.findViewById(R.id.eventContactEmail))
			.setText(eventItem.getContactEmail());
			((TextView) rootView.findViewById(R.id.eventLocation))
			.setText(eventItem.getLocation());
			((TextView) rootView.findViewById(R.id.eventDate))
			.setText(eventItem.getDate());
			((TextView) rootView.findViewById(R.id.eventStartTime))
			.setText(eventItem.getStartTime());
			((TextView) rootView.findViewById(R.id.eventEndTime))
			.setText(eventItem.getEndTime());
			((TextView) rootView.findViewById(R.id.eventURL))
			.setText(eventItem.getUrl());
			//((TextView) rootView.findViewById(R.id.eventURL_TEXT))
			//.setText(eventItem.getUrl_text());
//			((TextView) rootView.findViewById(R.id.eventPoster_URL))
//			.setText(eventItem.getPoster_url());
			ImageView poster = (ImageView) rootView.findViewById(R.id.img_event_poster);
	        if(eventItem.getPoster_url().isEmpty()){
	        	//display default image
	        	poster.setImageResource(R.drawable.img_event_default);
	        	Log.i("poster url is null", eventItem.getPoster_url());
	        }else{
	        	//load from url
	        	new ImageLoadingTask(eventItem.getPoster_url(), poster).execute(null, null);
	        }
	        Log.i("poster url", eventItem.getPoster_url());
		}

		return rootView;
	}
	
	public class ImageLoadingTask extends AsyncTask<Void, Void, Bitmap> {

	    private String url;
	    private ImageView imageView;

	    public ImageLoadingTask(String url, ImageView imageView) {
	        this.url = url;
	        this.imageView = imageView;
	    }

	    @Override
	    protected Bitmap doInBackground(Void... params) {
	        try {
	            URL urlConnection = new URL(url);
	            HttpURLConnection connection = (HttpURLConnection) urlConnection
	                    .openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
	            return myBitmap;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Bitmap result) {
	        super.onPostExecute(result);
	        imageView.setImageBitmap(result);
	    }

	}
}
