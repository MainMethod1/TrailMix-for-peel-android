package com.mainmethod.trailmix1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

	protected static final String ARG_ACTIVITY = "activity";

	public HomeFragment() {
		// TODO Auto-generated constructor stub
	}

	public static Fragment newInstance() {
		HomeFragment myFragment = new HomeFragment();
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.home_fragment, container, false);
		Button buttonRun = (Button) v.findViewById(R.id.button_run);
		buttonRun.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(), MapActivity.class);
				intent.putExtra(ARG_ACTIVITY,"run");
				startActivity(intent);
			}
		});
		
		Button buttonHike = (Button) v.findViewById(R.id.button_hike);
		buttonHike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(), MapActivity.class);
				intent.putExtra(ARG_ACTIVITY,"hike");
				startActivity(intent);
			}
		});
		Button buttonBike = (Button) v.findViewById(R.id.button_bike);
		buttonBike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(), MapActivity.class);
				intent.putExtra(ARG_ACTIVITY,"bike");
				startActivity(intent);
			}
		});
		return v;

	}

	public void openMap(View v) {

		Intent intent = new Intent(getActivity(), MapActivity.class);
		startActivity(intent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView();
	}
}
