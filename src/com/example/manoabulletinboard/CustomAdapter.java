package com.example.manoabulletinboard;

import java.util.ArrayList;
import java.util.List;
import java.text.DateFormatSymbols;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Post> {
    
    public CustomAdapter(Context context, ArrayList<Post> list) {
        super(context,0,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data for item
        Post item = getItem(position);
		Log.d("ManoaBulletinBoard","IMEI in adapter = " + item.getIMEI());
		Log.d("ManoaBulletinBoard","NAME in adapter = " + item.getName());


       
        if(convertView == null)
        {
            // Get a new instance of the row layout view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_view, parent, false);
        }

        // Put appropriate icon (icons from icons8.com)
        String category = item.getCategory();
        ImageView image = (ImageView)convertView.findViewById(R.id.image);
        switch(category) {
        case "Food Truck": 
        	image.setImageResource(R.drawable.dining_room96);
        	break;
        case "For Sale":
        	image.setImageResource(R.drawable.price_tag96);
        	break;
        case "Event":
        	image.setImageResource(R.drawable.dancing96);
        	break;
        case "Club":
        	image.setImageResource(R.drawable.group96);
        	break;
        case "Other":
        	image.setImageResource(R.drawable.bookmark96);
        	break;
        default:
        	image.setImageResource(R.drawable.bookmark96);
        	break;
        }
        
        /** Set data to your Views. */
        TextView text_user = (TextView)convertView.findViewById(R.id.text_user);
        TextView text_text = (TextView)convertView.findViewById(R.id.text_text);
        text_user.setText(item.getName());
        // Extract the month, day, and year from the post date string
        String postdate = item.getPostDate();
        String justthedate = postdate.split("[ ]")[0];
        String []p = justthedate.split("[-]");
        int monthnum = Integer.parseInt(p[1]);
        String displayeddate = "Posted " + (new DateFormatSymbols().getMonths()[monthnum-1]) + " " + p[2];
        text_text.setText(displayeddate);

        return convertView;
    }
}