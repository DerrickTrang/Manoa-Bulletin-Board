package com.example.manoabulletinboard;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewPostScreen extends ActionBarActivity {

	private Intent intent;
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_post_screen);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.ManoaGreen));

		// Initialize strings and views
		String ViewPostTitle = getIntent().getExtras().getString(PostData.C_Title);
		String ViewPostEmail = getIntent().getExtras().getString(PostData.C_Email);
		String ViewPostDescription = getIntent().getExtras().getString(PostData.C_Description);
		String ViewPostContactNumber = getIntent().getExtras().getString(PostData.C_Number);
		String ViewPostStartDate = getIntent().getExtras().getString(PostData.C_StartDate);
		String ViewPostEndDate = getIntent().getExtras().getString(PostData.C_EndDate);
		String ViewPostStartTime = getIntent().getExtras().getString(PostData.C_StartTime);
		String ViewPostEndTime = getIntent().getExtras().getString(PostData.C_EndTime);
		double locationx = getIntent().getExtras().getDouble(PostData.C_Location_X);
		double locationy = getIntent().getExtras().getDouble(PostData.C_Location_Y);
		Log.d("ManoaBulletinBoard","X = " + locationx);
		Log.d("ManoaBulletinBoard","Y = " + locationy);
		TextView view_post_screen_title = (TextView) findViewById(R.id.view_post_screen_title);
		TextView view_post_screen_email = (TextView) findViewById(R.id.view_post_screen_email);
		TextView view_post_screen_description = (TextView) findViewById(R.id.view_post_screen_description);
		TextView view_post_screen_date = (TextView) findViewById(R.id.view_post_screen_date);
		TextView view_post_screen_time = (TextView) findViewById(R.id.view_post_screen_time);
		TextView view_post_screen_contact_number = (TextView) findViewById(R.id.view_post_screen_contact_number);
		
		// Set content of views
		view_post_screen_title.setText(ViewPostTitle);
		view_post_screen_email.setText("Contact Email: " + ViewPostEmail);
		view_post_screen_description.setText(ViewPostDescription);
		if(!ViewPostContactNumber.matches(""))
			view_post_screen_contact_number.setText("Contact Number: " + ViewPostContactNumber);
		String []startdate = ViewPostStartDate.split("[-]");
		String []enddate = ViewPostEndDate.split("[-]");
		// If the day matches, dont display the end date, just the start and end time
		Log.d("ManoaBulletinBoard","start[2] = " + startdate[2] + ", end[2] = " + enddate[2]);
		if(startdate[2].matches(enddate[2])) {
			int monthnum = Integer.parseInt(startdate[1]);
			view_post_screen_date.setText((new DateFormatSymbols().getMonths()[monthnum-1]) + " " + startdate[2] + ", " + startdate[0]);
		}
		// otherwise, display both with a " - " between
		else {
			int monthnum1 = Integer.parseInt(startdate[1]);
			int monthnum2 = Integer.parseInt(enddate[1]);
			view_post_screen_date.setText((new DateFormatSymbols().getMonths()[monthnum1-1]) + " " + startdate[2] + ", " + startdate[0] + " - " +
											(new DateFormatSymbols().getMonths()[monthnum2-1]) + " " + enddate[2] + ", " + enddate[0]);			
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
		try {
			Date start = sdf.parse(ViewPostStartTime);
			Date end = sdf.parse(ViewPostEndTime);
			view_post_screen_time.setText((new SimpleDateFormat("hh:mm a").format(start)) + " - " + (new SimpleDateFormat("hh:mm a").format(end)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Set up map
		MapFragment tempFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		map = tempFrag.getMap();
		
		if(map != null) {
			LatLng post_position = new LatLng(locationy, locationx);
			map.moveCamera(CameraUpdateFactory.newLatLng(post_position));
			map.moveCamera(CameraUpdateFactory.zoomTo(16));
			map.addMarker(new MarkerOptions().position(post_position));
		}
		
		//save intent for editing and deleting.
		intent = getIntent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_post_screen, menu);
		if(!getIntent().getExtras().getString("IMEI").matches(Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID))) {
			menu.getItem(0).setVisible(false);
			menu.getItem(1).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if(item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		else if(item.getItemId() == R.id.delete_post_button) {
			// This should open a dialogue asking for confirmation, delete post, and go back to previous activity
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	 
				// set title
				alertDialogBuilder.setTitle("Delete post");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Are you sure you want to delete your post?")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, delete the post from the database, return to the previous screen
							Log.i("Deleting post: ID: ",String.valueOf(intent.getExtras().getInt(PostData.C_ID)));
							Log.i("Deleting post: IMEI: ",intent.getExtras().getString(PostData.C_IMEI));
							((PostApp)getApplication()).DeleteEvent(intent.getExtras().getInt("ID"),intent.getExtras().getString("IMEI"));
							finish();
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close the dialog box and do nothing
							dialog.cancel();
						}
					});
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					// show it
					alertDialog.show();
		}
		else if(item.getItemId() == R.id.edit_post_button) {
			// This should go to create post activity with intent with all the info, delete old post and push new one up to server
        	intent.setClass(this, CreatePostScreen.class);
        	intent.putExtra("isEdit", true);
        	startActivity(intent);
        	finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
