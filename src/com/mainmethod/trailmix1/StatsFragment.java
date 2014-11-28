package com.mainmethod.trailmix1;



import java.util.ArrayList;

import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatsFragment extends Fragment {
	
	int maxTime = 0;
	double maxDistance = 0;
	double maxSpeed = 0;
	int totalTime = 0;
	double totalDistance = 0;
	double avgSpeed = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
	    View v = inflater.inflate(R.layout.stats_fragment, null, false);
	    calculateIndivStatistics();
	    calculateOverallStatistics();
	    TextView txt_maxTime = (TextView) v.findViewById(R.id.stats_best_time);
	    txt_maxTime.setText(MapUtil.formatTime(maxTime) + " minutes");
	    TextView txt_maxDistance = (TextView) v.findViewById(R.id.stats_best_distance);
	    txt_maxDistance.setText(String.format("%.2f",maxDistance/1000) + " km");
	    TextView txt_maxSpeed = (TextView) v.findViewById(R.id.stats_best_speed);
	    txt_maxSpeed.setText(String.format("%.2f",maxSpeed) + " km/h");
	    TextView txt_totalTime = (TextView) v.findViewById(R.id.stats_total_duration);
	    txt_totalTime.setText(MapUtil.formatTime(totalTime) + " minutes");
	    TextView txt_totalDistance = (TextView) v.findViewById(R.id.stats_total_distance);
	    txt_totalDistance.setText(String.format("%.2f",totalDistance/1000) + " km");
	    TextView txt_avgSpeed = (TextView) v.findViewById(R.id.stats_avg_speed);
	    txt_avgSpeed.setText(String.format("%.2f", avgSpeed) + " km/h");
	    
	    
	    
		return v;
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
	
	private void calculateIndivStatistics() {
		DatabaseHelper db = new DatabaseHelper(getActivity());
		ArrayList<Session> sessions = db.getAllSessions();
		if(sessions.size() > 0){
		maxTime = sessions.get(0).getTime();
		maxDistance = sessions.get(0).getDistance();
		maxSpeed = sessions.get(0).getSpeed();
		
		for(Session s: sessions)
		{
			if(s.getTime() > maxTime)
			{
				maxTime = s.getTime();
			}
			else if(s.getDistance() > maxDistance)
			{
				maxDistance = s.getDistance();
			}
			else if(s.getSpeed() > maxSpeed)
			{
				maxSpeed = s.getSpeed();
			}
		}}
	}
	
	private void calculateOverallStatistics() {
		DatabaseHelper db = new DatabaseHelper(getActivity());
		ArrayList<Session> sessions = db.getAllSessions();
		if(sessions.size()>0){
		
		double totalSpeed = 0;
		
		for(Session s: sessions)
		{
				totalTime += s.getTime();
				totalDistance += s.getDistance();
				totalSpeed += s.getSpeed();
		}
		
		avgSpeed = totalSpeed/sessions.size();
		}
	}
}
