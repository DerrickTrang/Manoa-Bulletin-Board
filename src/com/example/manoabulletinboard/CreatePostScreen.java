package com.example.manoabulletinboard;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class CreatePostScreen extends ActionBarActivity implements OnMapClickListener {
	/*Attribute declaration*/
	int test;
	TextView Title;
	TextView Description;
	TextView Email;
	TextView Number;
	Spinner Category;
	Button Submit;
	Button Cancel;
	double Location_x;
	double Location_y;
	int Contact_Number;
	private GoogleMap map;
	
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
		
		/*Assign view variables to xml objects*/
		Title = (TextView)findViewById(R.id.create_post_screen_title);
		Description = (TextView)findViewById(R.id.create_post_screen_description);
		Email = (TextView)findViewById(R.id.create_post_screen_email);
		Number = (TextView)findViewById(R.id.create_post_screen_number);
		Category = (Spinner)findViewById(R.id.create_post_screen_categories);
		
//		MapFragment tempFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.createScreenMap);
//		map = tempFrag.getMap();
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.createScreenMap)).getMap();
		
		if(map != null) {
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.29981552257571,-157.8175474)));
			map.moveCamera(CameraUpdateFactory.zoomTo(16));
			map.setOnMapClickListener(this);
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
				if(Title.getText().toString().matches("") || Description.getText().toString().matches("") || Email.getText().toString().matches("") || Location_x == 0) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Missing some fields/location", Toast.LENGTH_SHORT);
                	toast.show();
				}
				else {
				Log.d("ManoaBulletinBoard","Title is (" + Title.getText().toString() + ")");
				Post UserPost = storeViewToObject();
				// Post to server
				// For now, send it back to the main activity for development
				
				
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
	}
	
	@Override
	public void onMapClick(LatLng position) {
		Log.d("ManoaBulletinBoard","Map was tapped");
		map.addMarker(new MarkerOptions().position(position).title("Post here!"));
		Location_x = position.longitude;
		Location_y = position.latitude;
	}
	
	public Post storeViewToObject(){
		
		
		Post UserPost = new Post(Title.getContext(), 1, Title.getText().toString(),
				"2014-11-15","02:55:00","03:55:00",Location_x,Location_y,"Post 208",Description.getText().toString(),Email.getText().toString(),Integer.parseInt(Number.getText().toString()),Category.getSelectedItem().toString());
		
//		Post UserPost = new Post(Title.getText().toString(),
//				Description.getText().toString(),Email.getText().toString(),
//				Category.getSelectedItem().toString(),Location_x,Location_y,Contact_Number);
		
		/*Log is a Debugging tool, error detection etc.*/
		Log.d("ManoaBulletinBoard","store_view_to_object");
		
		return UserPost;
	
	}
	
}