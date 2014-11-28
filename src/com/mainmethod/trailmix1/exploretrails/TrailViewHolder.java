package com.mainmethod.trailmix1.exploretrails;

import com.mainmethod.trailmix1.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TrailViewHolder extends RecyclerView.ViewHolder {

	protected TextView vName;
    protected TextView vSurface;
    protected TextView vLength;
    protected ImageView vImage;


    public TrailViewHolder(View v) {
         super(v);
         vName =  (TextView) v.findViewById(R.id.trailName);
         vSurface = (TextView)  v.findViewById(R.id.trailSurface);
         vLength = (TextView)  v.findViewById(R.id.trailLength);
         vImage = (ImageView) v.findViewById(R.id.img_trail);
     }
}
