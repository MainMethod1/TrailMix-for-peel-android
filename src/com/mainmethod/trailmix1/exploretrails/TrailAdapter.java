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
		
        	if(trail.getTrailName().equals("Culham Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_culham_trail);
        	} else if(trail.getTrailName().equals("Bovaird Drive Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_bovaird_drive_trail);
        	}else if(trail.getTrailName().equals("Chinguacousy Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_chinguacousy_trail);
        	}else if(trail.getTrailName().equals("Etobicoke Creek Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_etobicoke_creek_trail);
        	}else if(trail.getTrailName().equals("Caledon Trailway")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_caledon_trailway);
        	}else if(trail.getTrailName().equals("Humber Valley Heritage Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_humber_valley_heritage_trail);
        	}else if(trail.getTrailName().equals("Sheridan Creek Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_sheridan_creek_trail);
        	}else if(trail.getTrailName().equals("Don Doan Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_don_doan_trail);
        	}else if(trail.getTrailName().equals("Burnhamthorpe Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_burnhamthorpe_trail);
        	}else if(trail.getTrailName().equals("Applewood Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_applewood_trail);
        	}else if(trail.getTrailName().equals("Bruce Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_bruce_trail);
        	}else if(trail.getTrailName().equals("Fletchers Creek Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_fletchers_creek_trail);
        	}else if(trail.getTrailName().equals("Waterfront Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_waterfront_trail);
        	}else if(trail.getTrailName().equals("Grand Valley Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_grand_valley_trail);
        	}else if(trail.getTrailName().equals("Lisgar Meadow Brook Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_lisgar_meadow_brook_trail);
        	}else if(trail.getTrailName().equals("Lake Wabukayne Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_lake_wabukayne_trail);
        	}else if(trail.getTrailName().equals("Elora Cataract Trailway")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_elora_cataract_trailway);
        	}else if(trail.getTrailName().equals("Esker Lake Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_esker_lake_trail);
        	}else if(trail.getTrailName().equals("Sawmill Creek Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_sawmill_creek_trail);
        	}else if(trail.getTrailName().equals("Oak Ridges Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_oak_ridges_trail);
        	}else if(trail.getTrailName().equals("Lorrie Mitoff Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_lorrie_mitoff_trail);
        	}else if(trail.getTrailName().equals("Lake Aquitaine Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_lake_aquitaine_trail);
        	}else if(trail.getTrailName().equals("Bruce Side Trail")){
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail_bruce_side_trail);
        	}else {
        		trailViewHolder.vImage.setBackgroundResource(R.drawable.trail1);
        	}
        	
       
		trailViewHolder.vSurface.setText(trail.getSurface());
		//trailViewHolder.vImage.setBackgroundResource();
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
