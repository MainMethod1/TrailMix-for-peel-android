package com.mainmethod.trailmix1;

import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Trail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TrailDetailFragment extends Fragment {
	public static final String ARG_TRAIL_NAME = "name";
	private Trail trailItem;
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
	}

	private Trail getTrail(String argTrailName) {
		DatabaseHelper db = new DatabaseHelper(getActivity());
		Trail trail = db.getTrailByName(argTrailName);
		db.close();
		return trail;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_trail_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (trailItem != null) {
			((TextView) rootView.findViewById(R.id.txt_trailName))
					.setText(trailItem.getTrailName());
			((TextView) rootView.findViewById(R.id.txt_trailType))
			.setText(trailItem.getTrailClass());
			((TextView) rootView.findViewById(R.id.txt_trailLength))
			.setText(String.valueOf(trailItem.getLength()));
			((TextView) rootView.findViewById(R.id.txt_trailSurface))
			.setText(trailItem.getSurface());
			((TextView) rootView.findViewById(R.id.txt_amenities))
			.setText(trailItem.getAmenities());
			((TextView) rootView.findViewById(R.id.txt_parking))
			.setText(trailItem.getParking());
			((TextView) rootView.findViewById(R.id.txt_seasonHours))
			.setText(trailItem.getSeasonHours());
			((TextView) rootView.findViewById(R.id.txt_lighting))
			.setText(trailItem.getLighting());
			((TextView) rootView.findViewById(R.id.txt_maintenance))
			.setText(trailItem.getWinterMaintenance());
			((TextView) rootView.findViewById(R.id.txt_pets))
			.setText(trailItem.getPets());
			((TextView) rootView.findViewById(R.id.txt_notes))
			.setText(trailItem.getNotes());
	
		}

		return rootView;
	}
}
