package com.mainmethod.trailmix1;

import java.util.ArrayList;

// Android Library
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> This class extends the BaseAdapter class from the Android API 
 * which accepts a collection of NavDrawerItems to be displayed
 * into a ListView on the FragmentNavigationDrawer </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {       
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.activity_main_navigationdrawerlistitem, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.drawer_list_item);

        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        return convertView;
    }

}
