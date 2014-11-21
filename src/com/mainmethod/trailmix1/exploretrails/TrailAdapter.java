package com.mainmethod.trailmix1.exploretrails;

import java.util.List;

import com.mainmethod.trailmix1.R;
import com.mainmethod.trailmix1.sqlite.model.Trail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class TrailAdapter extends RecyclerView.Adapter<TrailViewHolder>  {

	private List<Trail> trailList;
    
	public TrailAdapter(List<Trail> trailList) {
		super();
		this.trailList = trailList;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return trailList.size();
	}

	@Override
	public void onBindViewHolder(TrailViewHolder trailViewHolder, int arg1) {
		// TODO Auto-generated method stub
		Trail trail = trailList.get(arg1);
		trailViewHolder.vName.setText(trail.getTrailName());
		trailViewHolder.vLength.setText(String.format("%.2f",trail.getLength()/1000) +" km" );
		trailViewHolder.vSurface.setText(trail.getSurface());
		
	}

	@Override
	public TrailViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		// TODO Auto-generated method stub
		View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.explore_trails_cardview, viewGroup, false);
    
		return new TrailViewHolder(itemView);
	}
	

}
