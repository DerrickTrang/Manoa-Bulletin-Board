package com.example.manoabulletinboard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
       
        if(convertView == null)
        {
            // Get a new instance of the row layout view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_view, parent, false);
        }

        /** Set data to your Views. */
        TextView text_user = (TextView)convertView.findViewById(R.id.text_user);
        TextView text_text = (TextView)convertView.findViewById(R.id.text_text);

        text_user.setText(item.getName());
        text_text.setText(item.getDescription());

        return convertView;
    }
}