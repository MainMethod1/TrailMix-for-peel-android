package com.mainmethod.trailmix1;

import java.util.ArrayList;
import java.util.List;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	protected static final String ARG_ACTIVITY = "activity";
	SwipeListView swipelistview;
	SwipeListItemAdapter adapter;
	List<SwipeListItemRow> itemData;

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
		swipelistview=(SwipeListView)v.findViewById(R.id.example_swipe_lv_list);
		itemData=new ArrayList<SwipeListItemRow>();
		adapter=new SwipeListItemAdapter(getActivity(),R.layout.swipe_row_layout,itemData);
		
		swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
	         @Override
	         public void onOpened(int position, boolean toRight) {
	        	 DatabaseHelper db = new DatabaseHelper(getActivity());
	        	 if(position == 0){
	        		
	        		 db.createActivityReportItem("bike");
	        		 db.closeDB();
	        		 Intent intent = new Intent(getActivity(), MapActivity.class);
	 				intent.putExtra(ARG_ACTIVITY,"bike");
	 				startActivity(intent);
	        	 }else if(position == 1){
	        		 db.createActivityReportItem("run");
	        		 db.closeDB();
	        		 Intent intent = new Intent(getActivity(), MapActivity.class);
		 				intent.putExtra(ARG_ACTIVITY,"run");
		 				startActivity(intent);
	        	 }else{
	        		 db.createActivityReportItem("hike");
	        		 db.closeDB();
	        		 Intent intent = new Intent(getActivity(), MapActivity.class);
		 				intent.putExtra(ARG_ACTIVITY,"hike");
		 				startActivity(intent);
	        	 }
	         }
	 
	         @Override
	         public void onClosed(int position, boolean fromRight) {
	         }
	 
	         @Override
	         public void onListChanged() {
	         }
	 
	         @Override
	         public void onMove(int position, float x) {
	         }
	 
	         @Override
	         public void onStartOpen(int position, int action, boolean right) {
	             Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
	         }
	 
	         @Override
	         public void onStartClose(int position, boolean right) {
	             Log.d("swipe", String.format("onStartClose %d", position));
	         }
	 
	         @Override
	         public void onClickFrontView(int position) {
	             Log.d("swipe", String.format("onClickFrontView %d", position));
	 
	             //swipelistview.openAnimate(position); //when you touch front view it will open
	 
	         }
	 
	         @Override
	         public void onClickBackView(int position) {
	             Log.d("swipe", String.format("onClickBackView %d", position));
	 
	            //  swipelistview.closeAnimate(position);//when you touch back view it will close
	         }
	 
	         @Override
	         public void onDismiss(int[] reverseSortedPositions) {
	 
	         }
	 
	     });
			swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_RIGHT); // there are five swiping modes
		// swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions
		 swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
		 swipelistview.setOffsetLeft(convertDpToPixel(260f)); // left side offset
		 swipelistview.setOffsetRight(convertDpToPixel(0f)); // right side offset
		 swipelistview.setAnimationTime(50); // animarion time
		 swipelistview.setSwipeOpenOnLongPress(true); // enable or disable SwipeOpenOnLongPress
         
		 swipelistview.setAdapter(adapter);
		 
		
		 ArrayList<Integer> bikeImgIds = new ArrayList<Integer>();
			ArrayList<Integer> walkImgIds = new ArrayList<Integer>();
			ArrayList<Integer> hikeImgIds = new ArrayList<Integer>();

			bikeImgIds.add(R.drawable.bike_img_1);
			bikeImgIds.add(R.drawable.bike_img_2);
			bikeImgIds.add(R.drawable.bike_img_3);
			bikeImgIds.add(R.drawable.bike_img_4);

			walkImgIds.add(R.drawable.walk_img_1);
			walkImgIds.add(R.drawable.walk_img_2);
			walkImgIds.add(R.drawable.walk_img_3);

			hikeImgIds.add(R.drawable.hike_img_1);
			hikeImgIds.add(R.drawable.hike_img_2);
			hikeImgIds.add(R.drawable.hike_img_3);

			itemData.add(new SwipeListItemRow("Bike", getResources().getDrawable(
					R.drawable.act_biking), bikeImgIds));
			itemData.add(new SwipeListItemRow("Walk", getResources().getDrawable(
					R.drawable.act_run), walkImgIds));
			itemData.add(new SwipeListItemRow("Hike", getResources().getDrawable(
					R.drawable.act_hike), hikeImgIds));
		
		  
		 adapter.notifyDataSetChanged();
//		Button buttonRun = (Button) v.findViewById(R.id.button_run);
//		buttonRun.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				
//				Intent intent = new Intent(getActivity(), MapActivity.class);
//				intent.putExtra(ARG_ACTIVITY,"run");
//				startActivity(intent);
//			}
//		});
//		
//		Button buttonHike = (Button) v.findViewById(R.id.button_hike);
//		buttonHike.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				
//				Intent intent = new Intent(getActivity(), MapActivity.class);
//				intent.putExtra(ARG_ACTIVITY,"hike");
//				startActivity(intent);
//			}
//		});
//		Button buttonBike = (Button) v.findViewById(R.id.button_bike);
//		buttonBike.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				
//				Intent intent = new Intent(getActivity(), MapActivity.class);
//				intent.putExtra(ARG_ACTIVITY,"bike");
//				startActivity(intent);
//			}
//		});
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
	
	public int convertDpToPixel(float dp) {
	       DisplayMetrics metrics = getResources().getDisplayMetrics();
	       float px = dp * (metrics.densityDpi / 160f);
	       return (int) px;
	   }
}
