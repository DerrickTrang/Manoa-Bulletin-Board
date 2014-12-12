package com.example.manoabulletinboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class CreatePostScreen extends ActionBarActivity implements OnMapClickListener, OnCameraChangeListener {
	/*Attribute declaration*/
	LatLngBounds border;
	boolean markerPresent;
	Marker postLocation;
	TextView Title;
	TextView Description;
	TextView Email;
	TextView Number;
	Spinner Category;
	Spinner StartDateMonth;
	Spinner StartDateDay;
	Spinner StartDateYear;
	Spinner EndDateMonth;
	Spinner EndDateDay;
	Spinner EndDateYear;
	Spinner StartHour;
	Spinner StartMinute;
	Spinner StartAMPM;
	Spinner EndHour;
	Spinner EndMinute;
	Spinner EndAMPM;
	String StartDate;	// Strings for dates to be stored into database
	String EndDate;
	String StartTime;	// Strings for start/end time
	String EndTime;
	Button Submit;
	Button Cancel;
	double Location_x;
	double Location_y;
	String Contact_Number;
	private GoogleMap map;
	ScrollView mainScrollView;
	Post UserPost;
	
	/*Instantiate dbHelper and a database*/
	DbHelper dbHelper;
	android.database.sqlite.SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ManoaBulletinBoard","Got into create post screen");
		/*Parse newpost_screen XML*/
		setContentView(R.layout.create_post_screen);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.ManoaGreen));
		
		// set default values for location
        border = new LatLngBounds(new LatLng(21.29058395, -157.82444), new LatLng(21.3059279, -157.81058));
		Location_x = 0;
		Location_y = 0;
		markerPresent = false;
		postLocation = null;
		
		/*Assign view variables to xml objects*/
		Title = (TextView)findViewById(R.id.create_post_screen_title);
		Description = (TextView)findViewById(R.id.create_post_screen_description);
		Email = (TextView)findViewById(R.id.create_post_screen_email);
		Number = (TextView)findViewById(R.id.create_post_screen_number);
		StartDateMonth = (Spinner)findViewById(R.id.create_post_screen_start_month);
		StartDateDay = (Spinner)findViewById(R.id.create_post_screen_start_day);
		StartDateYear = (Spinner)findViewById(R.id.create_post_screen_start_year);
		EndDateMonth = (Spinner)findViewById(R.id.create_post_screen_end_month);
		EndDateDay = (Spinner)findViewById(R.id.create_post_screen_end_day);
		EndDateYear = (Spinner)findViewById(R.id.create_post_screen_end_year);
		StartHour = (Spinner)findViewById(R.id.create_post_screen_start_hour);
		StartMinute = (Spinner)findViewById(R.id.create_post_screen_start_minute);
		StartAMPM = (Spinner)findViewById(R.id.create_post_screen_start_ampm);
		EndHour = (Spinner)findViewById(R.id.create_post_screen_end_hour);
		EndMinute = (Spinner)findViewById(R.id.create_post_screen_end_minute);
		EndAMPM = (Spinner)findViewById(R.id.create_post_screen_end_ampm);
		Category = (Spinner)findViewById(R.id.create_post_screen_categories);
		
//		MapFragment tempFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.createScreenMap);
//		map = tempFrag.getMap();
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.createScreenMap)).getMap();
		
		if(map != null) {
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.29981552257571,-157.8175474)));
			map.moveCamera(CameraUpdateFactory.zoomTo(16));
			map.setOnMapClickListener(this);
			map.setOnCameraChangeListener(this);
			map.setMyLocationEnabled(true);
//			Location tempLocation = map.getMyLocation();
//			map.addMarker(new MarkerOptions().position(new LatLng(tempLocation.getLatitude(),tempLocation.getLongitude())).icon(
//					BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
		}
		Submit = (Button)findViewById(R.id.create_post_screen_submit);
		Cancel = (Button)findViewById(R.id.create_post_screen_cancel);
		
		// Enable buttons and set listeners
		Submit.setEnabled(true);
		Cancel.setEnabled(true);
		
        Submit.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// Stuff for checking fields
				int length = Number.getText().toString().length();
				StartDate = StartDateYear.getSelectedItem().toString() + "-" + 
						convertMonthToInt(StartDateMonth.getSelectedItem().toString()) + "-" + 
						StartDateDay.getSelectedItem().toString();
				EndDate = EndDateYear.getSelectedItem().toString() + "-" + 
						convertMonthToInt(EndDateMonth.getSelectedItem().toString()) + "-" + 
						EndDateDay.getSelectedItem().toString();
				Log.d("ManoaBulletinBoard", "Start date = " + StartDate);
				int starttimetest = 0;
				if(!StartHour.getSelectedItem().toString().matches("12"))
					starttimetest += Integer.parseInt(StartHour.getSelectedItem().toString());
				if(StartAMPM.getSelectedItem().toString().matches("P.M.")) {
					starttimetest += 12;
				}
				StartTime = Integer.toString(starttimetest);
				starttimetest *= 100;
				starttimetest += Integer.parseInt(StartMinute.getSelectedItem().toString());
				
				int endtimetest = 0;
				if(!EndHour.getSelectedItem().toString().matches("12"))
					endtimetest += Integer.parseInt(EndHour.getSelectedItem().toString());
				if(EndAMPM.getSelectedItem().toString().matches("P.M.")) {
					endtimetest += 12;
				}
				EndTime = Integer.toString(endtimetest);
				endtimetest *= 100;
				endtimetest += Integer.parseInt(EndMinute.getSelectedItem().toString());

				// Check for blank title, description, email, or location
				if(Title.getText().toString().matches("")) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Missing title", Toast.LENGTH_SHORT);
                	toast.show();
				}
				else if(Description.getText().toString().matches("")) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Missing description", Toast.LENGTH_SHORT);
                	toast.show();
				}
				else if(Email.getText().toString().matches("")) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Missing email", Toast.LENGTH_SHORT);
                	toast.show();
				}
				else if(Location_x == 0) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Missing post's location", Toast.LENGTH_SHORT);
                	toast.show();
				}
				// Check for valid phone number
				else if(length != 0 && length != 7 && length != 10 && length != 12 && length != 13 && length != 14) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT);
                	toast.show();
                }
				// Check for valid email
				else if(!Email.getText().toString().contains("@")) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT);
                	toast.show();
				}
				// Check for valid start date (might remove this - do they need to specify a date?)
				else if(StartDateMonth.getSelectedItem().toString().contains("- Month -") || 
						StartDateDay.getSelectedItem().toString().contains("- Day -") || 
						StartDateYear.getSelectedItem().toString().contains("- Year -") ) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Invalid date", Toast.LENGTH_SHORT);
                	toast.show();					
				}
				// check for valid start/end time
				else if(starttimetest > endtimetest) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Start time should be before end time", Toast.LENGTH_SHORT);
                	toast.show();					
				}
				else {
				Contact_Number = Number.getText().toString();
				StartTime += ":" + StartMinute.getSelectedItem().toString() + ":00"; 
				EndTime += ":" + EndMinute.getSelectedItem().toString() + ":00"; 
				Log.d("ManoaBulletinBoard","Title is (" + Title.getText().toString() + ")");
				UserPost = storeViewToObject();

				//if we are editing old post, delete the old one first
				if(getIntent().getExtras() != null)
				{	((PostApp)getApplication()).DeleteEvent(UserPost);
					AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(((PostApp)getApplication()).getContext());                      
				    dlgAlert.setTitle("Edited Post"); 
				    dlgAlert.setMessage("Your Post has been edited"); 
				    dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int whichButton) {
							/*insert into the database*/
							((PostApp)getApplication()).postdata.insert(UserPost);
							((PostApp)getApplication()).AddEvent(UserPost);
				        }
				   });
				    dlgAlert.setCancelable(true);
				    android.os.SystemClock.sleep(1000);
				    dlgAlert.create().show();
				}
				else{
				/*insert into the database*/
				((PostApp)getApplication()).postdata.insert(UserPost);
				((PostApp)getApplication()).AddEvent(UserPost);
				}
				
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", "created");
				setResult(RESULT_OK, returnIntent);
				Log.d("ManoaBulletinBoard","leaving create post");
				finish();
				}
			}
        	
        });
        
        Cancel.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				Log.d("ManoaBulletinBoard","Cancelled post, sending intent 'abort'");
				returnIntent.putExtra("result", "abort");
				setResult(RESULT_OK, returnIntent);
				finish();
			}
        	
        });
		
		/*Initialize coordinate points and phone number to zero*/
		Contact_Number = "";
		mainScrollView = (ScrollView) findViewById(R.id.scrollView1);
		ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        int action = event.getAction();
		        switch (action) {
		           case MotionEvent.ACTION_DOWN:
		                // Disallow ScrollView to intercept touch events.
		                mainScrollView.requestDisallowInterceptTouchEvent(true);
		                // Disable touch on transparent view
		                return false;

		           case MotionEvent.ACTION_UP:
		                // Allow ScrollView to intercept touch events.
		                mainScrollView.requestDisallowInterceptTouchEvent(false);
		                return true;

		           case MotionEvent.ACTION_MOVE:
		                mainScrollView.requestDisallowInterceptTouchEvent(true);
		                return false;

		           default: 
		                return true;
		        }   
		    }
		});
		
		//if there is an Intent sent to here, meanning that we are editting the old post.
		if(getIntent().getExtras() != null)
		{
			EditPost();
		}
	}
	
	@Override
	public void onMapClick(LatLng position) {
		Log.d("ManoaBulletinBoard","Map was tapped");
		if(border.contains(position)) {
			
		if(!markerPresent) {
			markerPresent = true;
			postLocation = map.addMarker(new MarkerOptions().position(position).title("Post here!"));
			Location_x = position.longitude;
			Location_y = position.latitude;			
		}
		else {
			postLocation.remove();
			postLocation = map.addMarker(new MarkerOptions().position(position).title("Post here!"));
			Location_x = position.longitude;
			Location_y = position.latitude;
		}
		}
		else {
        	Toast toast = Toast.makeText(getApplicationContext(), "Location must be on campus", Toast.LENGTH_SHORT);
        	toast.show();
		}
	}
	
	public Post storeViewToObject(){
		int id = 1;
		if(getIntent().getExtras() != null)
		{
			id = getIntent().getExtras().getInt(PostData.C_ID);
		}
		
		Post UserPost = new Post(
				Title.getContext(), 			// Context
				id, 								// ID
				Title.getText().toString(),		// Title
				StartDate,						// Start date
				EndDate,						// End date
				StartTime,						// Start time
				EndTime,						// End time
				Location_x, Location_y,
				"Post 208", 					// String location (implement somehow...)
				Description.getText().toString(),
				Email.getText().toString(),
				Contact_Number,
				Category.getSelectedItem().toString());
		
//		Post UserPost = new Post(Title.getText().toString(),
//				Description.getText().toString(),Email.getText().toString(),
//				Category.getSelectedItem().toString(),Location_x,Location_y,Contact_Number);
		
		/*Log is a Debugging tool, error detection etc.*/
		Log.d("ManoaBulletinBoard","store_view_to_object");
		
		return UserPost;
	
	}
	
	public String convertMonthToInt(String month) {
		String monthint = "";
		switch(month) {
		case "January": monthint = "1";
						break;
		case "February": monthint = "2";
						break;
		case "March": 	monthint = "3";
						break;
		case "April": monthint = "4";
						break;
		case "May": monthint = "5";
						break;
		case "June": monthint = "6";
						break;
		case "July": 	monthint = "7";
						break;
		case "August": monthint = "8";
						break;
		case "September": monthint = "9";
						break;
		case "October": monthint = "10";
						break;
		case "November": monthint = "11";
						break;
		case "December": monthint = "12";
						break;
		}
		return monthint;
	}
	
	public String convertMonthToString(String month) {
		String monthint = "";
		switch(month) {
		case "01": monthint = "January";
						break;
		case "02": monthint = "February";
						break;
		case "03": 	monthint = "March";
						break;
		case "04": monthint = "April";
						break;
		case "05": monthint = "May";
						break;
		case "06": monthint = "June";
						break;
		case "07": 	monthint = "July";
						break;
		case "08": monthint = "August";
						break;
		case "09": monthint = "September";
						break;
		case "10": monthint = "October";
						break;
		case "11": monthint = "November";
						break;
		case "12": monthint = "December";
						break;
		}
		return monthint;
	}
	
	@Override
	public void onCameraChange(CameraPosition C) {
//		if(C.zoom < 16) {
//			map.moveCamera(CameraUpdateFactory.zoomTo(16));
//		}
//		if(C.zoom > 18) {
//			map.moveCamera(CameraUpdateFactory.zoomTo(18));
//		}
//		if(C.target.latitude < 21.29058395) {
//			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.29058395,C.target.longitude)));
//		}
//		if(C.target.latitude > 21.3059279) {
//			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.3059279,C.target.longitude)));
//		}
//		if(C.target.longitude > -157.81058) {
//			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(C.target.latitude,-157.81058)));
//		}
//		if(C.target.longitude < -157.82444) {
//			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(C.target.latitude,-157.82444)));
//		}
	}
	
	private void EditPost()
	{
		Title.setText(getIntent().getExtras().getString(PostData.C_Title));
		Description.setText(getIntent().getExtras().getString(PostData.C_Description));
		Email.setText(getIntent().getExtras().getString(PostData.C_Email));
		Number.setText(getIntent().getExtras().getString(PostData.C_Number));
		Category.setSelection(this.getIndexFromSpinner(Category, getIntent().getExtras().getString(PostData.C_Category)));
		Location_x = getIntent().getExtras().getDouble(PostData.C_Location_X);
		Location_y = getIntent().getExtras().getDouble(PostData.C_Location_Y);
		setSpinnerSet(StartDateYear,this.getPartialInfo(0, getIntent().getExtras().getString(PostData.C_StartDate)),
					  StartDateMonth,this.getPartialInfo(1, getIntent().getExtras().getString(PostData.C_StartDate)),
					  StartDateDay,this.getPartialInfo(2, getIntent().getExtras().getString(PostData.C_StartDate)));
		setSpinnerSet(EndDateYear,this.getPartialInfo(0, getIntent().getExtras().getString(PostData.C_EndDate)),
				  	  EndDateMonth,this.getPartialInfo(1, getIntent().getExtras().getString(PostData.C_EndDate)),
				  	  EndDateDay,this.getPartialInfo(2, getIntent().getExtras().getString(PostData.C_EndDate)));
		setSpinnerSet(StartHour,this.getPartialInfo(0, getIntent().getExtras().getString(PostData.C_StartTime)),
					  StartMinute,this.getPartialInfo(1, getIntent().getExtras().getString(PostData.C_StartTime)),
					  StartAMPM,this.getPartialInfo(2, getIntent().getExtras().getString(PostData.C_StartTime)));
		setSpinnerSet(EndHour,this.getPartialInfo(0, getIntent().getExtras().getString(PostData.C_EndTime)),
					  EndMinute,this.getPartialInfo(1, getIntent().getExtras().getString(PostData.C_EndTime)),
					  EndAMPM,this.getPartialInfo(2, getIntent().getExtras().getString(PostData.C_EndTime)));
		// Add icon to map for post's current position
		if(map != null) {
			LatLng post_position = new LatLng(Location_y, Location_x);
			map.moveCamera(CameraUpdateFactory.newLatLng(post_position));
			postLocation = map.addMarker(new MarkerOptions().position(post_position).title("Post here!"));
			markerPresent = true;			
		}
	}
	
	private String getPartialInfo(int part, String info)
	{
		String[] interested = null;
		//if the info is a date.
		if(info.contains("-"))
		{
			interested = info.split("-");
			//if it month, change back to text
			if(part == 1)
			{
				interested[part] = convertMonthToString(interested[part]);
			}
			return interested[part];
		}else if(info.contains(":"))	// or the info is a time.
		{
			interested = info.split(":");
			if(part == 0)
			{
				if(Integer.parseInt(interested[0]) >= 13)
				interested[part] = String.valueOf(Integer.parseInt(interested[0]) - 12);
			} else if(part == 2)
			{
				if(Integer.parseInt(interested[0]) >= 13)
				interested[part] = "P.M.";
				else
				interested[part] = "A.M.";
			}
			return interested[part];
		} else{
			return info;
		}
	}
	
	private void setSpinnerSet(Spinner s1, String i1, Spinner s2, String i2, Spinner s3, String i3)
	{
		s1.setSelection(this.getIndexFromSpinner(s1, i1));
		s2.setSelection(this.getIndexFromSpinner(s2, i2));
		s3.setSelection(this.getIndexFromSpinner(s3, i3));
	}
	
	private int getIndexFromSpinner(Spinner spinner, String myString)
	{
		int index = 0;
		for (int i=0;i<spinner.getCount();i++)
		{
			if (spinner.getItemAtPosition(i).equals(myString))
		    index = i;
		}
		return index;
	}
}