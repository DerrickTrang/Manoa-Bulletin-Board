package com.example.manoabulletinboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CategoriesScreen extends ActionBarActivity {

	Button showAll;
	Button foodTruck;
	Button club;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories_screen);
		showAll = (Button) findViewById(R.id.categories_screen_show_all);
		foodTruck = (Button) findViewById(R.id.categories_screen_food_truck);
		club = (Button) findViewById(R.id.categories_screen_clubs);
		
		showAll.setEnabled(true);
		showAll.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("filter","Show_All");
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
		
		foodTruck.setEnabled(true);
		foodTruck.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent returnIntent = new Intent();
						returnIntent.putExtra("filter","Food Truck");
						setResult(RESULT_OK, returnIntent);
						finish();
					}
		        	
		        });			
		
		club.setEnabled(true);
		club.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent returnIntent = new Intent();
						returnIntent.putExtra("filter","Club");
						setResult(RESULT_OK, returnIntent);
						finish();
					}
		        	
		        });		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
