package com.mainmethod.trailmix1;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class SwipeListItemRow {
    String itemName;
    Drawable icon;
    int c;

    public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	public SwipeListItemRow(String itemName, Drawable icon, int color) {
          super();
          this.itemName = itemName;
          this.icon = icon;
          this.c = color;
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
