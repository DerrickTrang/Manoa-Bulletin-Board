package com.example.manoabulletinboard;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class CreatePostScreen extends ActionBarActivity implements OnMapClickListener, OnCameraChangeListener {
	/*Attribute declaration*/
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
	Spinner StartHour;
	Spinner StartMinute;
	Spinner StartAMPM;
	Spinner EndHour;
	Spinner EndMinute;
	Spinner EndAMPM;
	String StartDate;	// Strings for dates to be stored into database
	String StartTime;	// Strings for start/end time
	String EndTime;
	Button Submit;
	Button Cancel;
	double Location_x;
	double Location_y;
	int Contact_Number;
	private GoogleMap map;
	ScrollView mainScrollView;
	
	/*Instantiate dbHelper and a database*/
	DbHelper dbHelper;
	android.database.sqlite.SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ManoaBulletinBoard","Got into create post screen");
		/*Parse newpost_screen XML*/
		setContentView(R.layout.create_post_screen);
		
		// set default values for location
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
						convertMonth(StartDateMonth.getSelectedItem().toString()) + "-" + 
						StartDateDay.getSelectedItem().toString();
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
				if(Title.getText().toString().matches("") || Description.getText().toString().matches("") || Email.getText().toString().matches("") || Location_x == 0) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Missing some fields/location", Toast.LENGTH_SHORT);
                	toast.show();
				}
				// Check for valid phone number
				else if(length != 0 && length != 7 && length != 10) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT);
                	toast.show();
                }
				// Check for valid email
				else if(!Email.getText().toString().contains("@")) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT);
                	toast.show();
				}
				// Check for valid start date
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
				StartTime += ":" + StartMinute.getSelectedItem().toString() + ":00"; 
				EndTime += ":" + EndMinute.getSelectedItem().toString() + ":00"; 
				Log.d("ManoaBulletinBoard","Title is (" + Title.getText().toString() + ")");
				Post UserPost = storeViewToObject();
				
				/*insert into the database*/
				((PostApp)getApplication()).postdata.insert(UserPost);
				((PostApp)getApplication()).AddEvent(UserPost);
				
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
		Contact_Number = 0;
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
	}
	
	@Override
	public void onMapClick(LatLng position) {
		Log.d("ManoaBulletinBoard","Map was tapped");
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
	
	public Post storeViewToObject(){
		if(Number.getText().toString().matches(""))
			Contact_Number = 0;
		else
			Contact_Number = Integer.parseInt(Number.getText().toString());
		
		Post UserPost = new Post(
				Title.getContext(), 			// Context
				1, 								// ID
				Title.getText().toString(),		// Title
				StartDate,					// Event date
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
	
	public String convertMonth(String month) {
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
	
	@Override
	public void onCameraChange(CameraPosition C) {
		if(C.zoom < 16) {
			map.moveCamera(CameraUpdateFactory.zoomTo(16));
		}
		if(C.zoom > 18) {
			map.moveCamera(CameraUpdateFactory.zoomTo(18));
		}
		if(C.target.latitude < 21.29058395) {
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.29058395,C.target.longitude)));
		}
		if(C.target.latitude > 21.3059279) {
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.3059279,C.target.longitude)));
		}
		if(C.target.longitude > -157.81058) {
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(C.target.latitude,-157.81058)));
		}
		if(C.target.longitude < -157.82444) {
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(C.target.latitude,-157.82444)));
		}
	}
}