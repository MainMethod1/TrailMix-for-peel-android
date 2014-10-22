package com.mainmethod.trailmix1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mainmethod.trailmix1.sqlite.model.*;

public class EventListAdapter extends BaseAdapter  {
	private Context context;
	ArrayList<Event> eList = new ArrayList<Event>();

	public EventListAdapter(Context context, ArrayList<Event> eventCollection) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.eList = eventCollection;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return eList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater)
	                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.events_list_item, null);
	        }
		 TextView txtTitle = (TextView) convertView.findViewById(R.id.eventName);
		 txtTitle.setText(eList.get(position).getTitle());
		 
		 TextView txtLocation = (TextView) convertView.findViewById(R.id.location);
		 txtLocation.setText(eList.get(position).getLocation());
		 
		 TextView txtDate = (TextView) convertView.findViewById(R.id.date);
		 txtDate.setText(eList.get(position).getDate());
		 
		return convertView;
	}

	

}
