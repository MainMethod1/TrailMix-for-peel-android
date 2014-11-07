/**
 * 
 */
package com.mainmethod.trailmix1;

import java.util.ArrayList;

import com.mainmethod.trailmix1.sqlite.model.Session;
import com.mainmethod.trailmix1.sqlite.model.Trail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author D4RK
 *
 */
public class HistoryListAdapter extends BaseAdapter {

	private Context context;
	ArrayList<Session> sessionList = new ArrayList<Session>();

	public HistoryListAdapter(Context context, ArrayList<Session> sessionCollection) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.sessionList = sessionCollection;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sessionList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sessionList.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.history_list_item, null);
		}
		TextView txtStat = (TextView) convertView.findViewById(R.id.history_stat);
		txtStat.setText("Tracked " + String.valueOf(sessionList.get(position).getDistance() / 1000) + " km in "
				+ MapUtil.formatTime(sessionList.get(position).getTime()) + " minutes");

		// TextView txtDistance = (TextView)
		// convertView.findViewById(R.id.trailLength);
		// txtLength.setText(String.valueOf(sessionList.get(position).getDistance()));

		TextView txtDate = (TextView) convertView.findViewById(R.id.history_date);
		txtDate.setText(sessionList.get(position).getCreated_at());

		// TextView txtCounter = (TextView)
		// convertView.findViewById(R.id.list_item_counter);
		// txtCounter.setText(String.valueOf(getCount()));
		return convertView;
	}

	
}
