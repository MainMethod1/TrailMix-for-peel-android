package com.mainmethod.trailmix1;

import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Event;
// Android Library
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

	// This method 
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
			//(TextView) rootView.findViewById(R.id.eventPoster_URL))
			//.setText(eventItem.getPoster_url());
		}

		return rootView;
	}
}
