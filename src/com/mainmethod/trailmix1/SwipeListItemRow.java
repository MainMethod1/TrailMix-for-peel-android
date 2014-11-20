package com.mainmethod.trailmix1;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class SwipeListItemRow {
    String itemName;
    Drawable icon;
    ArrayList<Integer> imageCollection = new ArrayList<Integer>();

 
	
    public ArrayList<Integer> getImageCollection() {
		return imageCollection;
	}
	public void setImageCollection(ArrayList<Integer> imageCollection) {
		this.imageCollection = imageCollection;
	}
	public SwipeListItemRow(String itemName, Drawable icon,
			ArrayList<Integer> imageCollection) {
		super();
		this.itemName = itemName;
		this.icon = icon;
		this.imageCollection = imageCollection;
	}
	public String getItemName() {
          return itemName;
    }
    public void setItemName(String itemName) {
          this.itemName = itemName;
    }
    public Drawable getIcon() {
          return icon;
    }
    public void setIcon(Drawable icon) {
          this.icon = icon;
    }
}
