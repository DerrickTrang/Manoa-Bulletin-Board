package com.example.manoabulletinboard;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MapScreen extends ActionBarActivity implements OnMapClickListener, OnCameraChangeListener {
	private GoogleMap map;
	android.database.Cursor cursor;
	double locationx, locationy;
	String category;
	LatLng post_position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_screen);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.ManoaGreen));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		cursor = ((PostApp)getApplication()).postdata.query();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapScreenMap)).getMap();
		
		if(map != null) {
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(21.29981552257571,-157.8175474)));
			map.moveCamera(CameraUpdateFactory.zoomTo(16));
			map.setOnMapClickListener(this);
			map.setOnCameraChangeListener(this);
			map.setMyLocationEnabled(true);
		}
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			// Add icons to map for every post in data
			locationx = cursor.getDouble(cursor.getColumnIndex(PostData.C_Location_X));
			locationy = cursor.getDouble(cursor.getColumnIndex(PostData.C_Location_Y));
			post_position = new LatLng(locationy, locationx);
			category = cursor.getString(cursor.getColumnIndex(PostData.C_Category));
			Log.d("ManoaBulletinBoard","category in map = " + category);
			if(category.matches("Food Truck"))
				map.addMarker(new MarkerOptions().position(post_position).icon(BitmapDescriptorFactory.fromResource(R.drawable.dining_room32)));
			else if(category.matches("For Sale"))
				map.addMarker(new MarkerOptions().position(post_position).icon(BitmapDescriptorFactory.fromResource(R.drawable.price_tag32)));
			else if(category.matches("Event"))
				map.addMarker(new MarkerOptions().position(post_position).icon(BitmapDescriptorFactory.fromResource(R.drawable.dancing32)));
			else if(category.matches("Club"))
				map.addMarker(new MarkerOptions().position(post_position).icon(BitmapDescriptorFactory.fromResource(R.drawable.users32)));
			else
				map.addMarker(new MarkerOptions().position(post_position).icon(BitmapDescriptorFactory.fromResource(R.drawable.bookmark32)));

			cursor.moveToNext();
		}
		// declare LatLng position later
		// Marker markername = map.addMarker(new MarkerOptions().postion(position).icon(BitmapDescriptorFactory.fromFile("icon.png"));
		// ORRR
		// marker 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		
	}
}
