package com.example.manoabulletinboard;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchScreen extends ActionBarActivity {
	String search_text;
	android.database.Cursor cursor;
	ArrayList<Post> post_list;
	CustomAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_screen);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.ManoaGreen));
        post_list = new ArrayList<Post>();
        
        // Get search string from intent
        search_text = getIntent().getExtras().getString("search_text");
        getSupportActionBar().setTitle("Results for '" + search_text + "'");
        
        // List View initiation
        final ListView list = (ListView)findViewById(R.id.search_screen_scroll_view);
        adapter = new CustomAdapter(this, post_list);
		
		/*Set adapter*/
		list.setAdapter(adapter);
		
		// Set listener for list clicks (go to ViewPostScreen.java)
		list.setOnItemClickListener(new OnItemClickListener()
		{
		    @Override 
		    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
		    { 	
		    	Post p = (Post) list.getItemAtPosition(position);
		        Intent intent = new Intent(SearchScreen.this, ViewPostScreen.class);
		        Log.i("Main/listAdapater ID:", String.valueOf(p.getID()));
		        intent.putExtra(PostData.C_ID, p.getID());
		        intent.putExtra(PostData.C_IMEI, p.getIMEI());
				Log.d("ManoaBulletinBoard","IMEI in syncevent = " + p.getIMEI());
		        intent.putExtra(PostData.C_Title, p.getName());
		        Log.d("ManoaBulletinBoard","Added title to intent");
		        intent.putExtra(PostData.C_Email, p.getContactEmail());
		        Log.d("ManoaBulletinBoard","Added email to intent");
		        intent.putExtra(PostData.C_Description, p.getDescription());
		        Log.d("ManoaBulletinBoard","Added description to intent");
		        intent.putExtra(PostData.C_Location_X, p.getLocationX());
		        intent.putExtra(PostData.C_Location_Y,p.getLocationY());
				Log.d("ManoaBulletinBoard","passed X = " + p.getLocationX());
				Log.d("ManoaBulletinBoard","passed Y = " + p.getLocationY());
		        intent.putExtra(PostData.C_Number, p.getContactNumber());
		        intent.putExtra(PostData.C_StartDate, p.getStartDate());
		        intent.putExtra(PostData.C_EndDate, p.getEndDate());
		        intent.putExtra(PostData.C_StartTime, p.getStartTime());
		        intent.putExtra(PostData.C_EndTime, p.getEndTime());
		        intent.putExtra(PostData.C_Category, p.getCategory());
		        startActivity(intent);
		    	
		    }
		});
        
        // go through data and add it to the post list if the title/description contains the search string
		cursor = ((PostApp)getApplication()).postdata.query();
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			if(cursor.getString(cursor.getColumnIndex(PostData.C_Title)).contains(search_text) || 
				cursor.getString(cursor.getColumnIndex(PostData.C_Description)).contains(search_text)) {
				Post temppost = new Post(getApplicationContext(),
									 cursor.getInt(cursor.getColumnIndex(PostData.C_ID)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_IMEI)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Title)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_PostDate)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_StartDate)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_EndDate)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_StartTime)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_EndTime)),
									 cursor.getDouble(cursor.getColumnIndex(PostData.C_Location_X)),
									 cursor.getDouble(cursor.getColumnIndex(PostData.C_Location_Y)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Location)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Description)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Email)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Number)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Category)));
				post_list.add(temppost);
			}
			cursor.moveToNext();
		}
    	adapter.notifyDataSetChanged();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_screen, menu);
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
		if(id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
