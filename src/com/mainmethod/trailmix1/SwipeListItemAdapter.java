package com.mainmethod.trailmix1;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SwipeListItemAdapter extends ArrayAdapter {
	 
    List   data;
    Context context;
    int layoutResID;

public SwipeListItemAdapter(Context context, int layoutResourceId,List data) {
    super(context, layoutResourceId, data);

    this.data=data;
    this.context=context;
    this.layoutResID=layoutResourceId;

    // TODO Auto-generated constructor stub
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {

    NewsHolder holder = null;
       View row = convertView;
        holder = null;

      if(row == null)
      {
          LayoutInflater inflater = ((Activity) context).getLayoutInflater();
          row = inflater.inflate(layoutResID, parent, false);

          holder = new NewsHolder();

          holder.itemName = (TextView)row.findViewById(R.id.example_itemname);
          holder.icon=(ImageView)row.findViewById(R.id.example_image);
          holder.backLayout = (LinearLayout)row.findViewById(R.id.back);
          
//          holder.button1=(Button)row.findViewById(R.id.swipe_button1);
//          holder.button2=(Button)row.findViewById(R.id.swipe_button2);
//          holder.button3=(Button)row.findViewById(R.id.swipe_button3);
          row.setTag(holder);
      }
      else
      {
          holder = (NewsHolder)row.getTag();
      }

      SwipeListItemRow itemdata = (SwipeListItemRow) data.get(position);
      holder.itemName.setText(itemdata.getItemName());
      holder.icon.setImageDrawable(itemdata.getIcon());
      holder.backLayout.setBackgroundColor(itemdata.getC());
     
      return row;

}

static class NewsHolder{

    TextView itemName;
    ImageView icon;
    LinearLayout backLayout;
    }

}