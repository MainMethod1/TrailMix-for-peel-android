package com.mainmethod.trailmix1;

import java.util.ArrayList;

import com.mainmethod.trailmix1.sqlite.model.Event;
import com.mainmethod.trailmix1.sqlite.model.Trail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TrailListAdapter extends BaseAdapter {
	private Context context;
	ArrayList<Trail> tList = new ArrayList<Trail>();
	
	public TrailListAdapter(Context context, ArrayList<Trail> trailCollection) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.tList = trailCollection;

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tList.get(position);
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
	            convertView = mInflater.inflate(R.layout.explore_trail_list_item, null);
	        }
		 TextView txtName = (TextView) convertView.findViewById(R.id.trailName);
		 txtName.setText(tList.get(position).getTrailName());
		 
		 TextView txtLength = (TextView) convertView.findViewById(R.id.trailLength);
		 txtLength.setText(String.valueOf(tList.get(position).getLength()));
		 
		 TextView txtSurface = (TextView) convertView.findViewById(R.id.trailSurface);
		 txtSurface.setText(tList.get(position).getSurface());
		 
		// TextView txtCounter = (TextView) convertView.findViewById(R.id.list_item_counter);
		 //txtCounter.setText(String.valueOf(getCount()));
		return convertView;
	}


}
