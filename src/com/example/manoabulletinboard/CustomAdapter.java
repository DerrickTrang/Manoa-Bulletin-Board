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

        /** Set data to your Views. */
        TextView text_user = (TextView)convertView.findViewById(R.id.text_user);
        TextView text_text = (TextView)convertView.findViewById(R.id.text_text);

        text_user.setText(item.getName());
        
        // Extract the month, day, and year from the post date string
        String postdate = item.getPostDate();
        String justthedate = postdate.split("[ ]")[0];
        String []p = justthedate.split("[-]");
        int monthnum = Integer.parseInt(p[1]);
        String displayeddate = (new DateFormatSymbols().getMonths()[monthnum-1]) + " " + p[2];
        text_text.setText(displayeddate);

        return convertView;
    }
}