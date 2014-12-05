package com.example.manoabulletinboard;

import java.sql.ResultSet;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;


public class MainScreen extends ActionBarActivity {

	Button SearchButton;
	Button Refresh;
	
	//for testing Server only
	Button ServerB;
	
	ArrayList<Post> post_list = new ArrayList<Post>();
	CustomAdapter adapter;
	
	android.database.Cursor cursor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        SearchButton = (Button) findViewById(R.id.search_button);
        Refresh = (Button)findViewById(R.id.refresh_button);

        //pasing context into the application for showing dialog. (turns out only the serachbutton context works)
		((PostApp)getApplication()).setContext(SearchButton.getContext());
        
        //for testing Server only ******************* ADDED IMPLEMENTATION TO REFRESHLIST METHOD
//        ServerB = (Button)findViewById(R.id.button1);
//        ServerB.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				((PostApp)getApplication()).SyncEvent();
//			}
//		});
        //********************************************
        
        Refresh.setEnabled(true);
        Refresh.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// Refresh the list
				refreshList();
			}
        	
        });
        
        /*List View initiation*/
        final ListView list = (ListView)findViewById(R.id.main_screen_scroll_view);
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
		        Intent intent = new Intent(MainScreen.this, ViewPostScreen.class);
		        intent.putExtra("Title", p.getName());
		        Log.d("ManoaBulletinBoard","Added title to intent");
		        intent.putExtra("Email", p.getContactEmail());
		        Log.d("ManoaBulletinBoard","Added email to intent");
		        intent.putExtra("Description", p.getDescription());
		        Log.d("ManoaBulletinBoard","Added description to intent");
		        intent.putExtra("Location_x", p.getLocationX());
		        intent.putExtra("Location_y",p.getLocationY());
		        startActivity(intent);
		    	
		    }
		});
		
		cursor = ((PostApp)getApplication()).postdata.query();
		// Add something to make it refresh the list on create
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
        case R.id.categories_button:
        	sortByCategories();
        	return true;
        case R.id.create_post_button:
        	createPost();
        	return true;
        case R.id.action_settings:
            return true;
        default:
        return super.onOptionsItemSelected(item);
        }
    }
    
    public void sortByCategories() {
    	Intent intent = new Intent(this, CategoriesScreen.class);
    	startActivityForResult(intent, 2); // "2" is the request code for category screen
    }
    
    public void createPost() {
    	Intent intent = new Intent(this, CreatePostScreen.class);
    	startActivityForResult(intent, 1); 	// Use startactivityforresult if we need to send something back here 
    										// "1" is the requestCode that will tell us where "OnActivityResult" will go
    							// otherwise, use startActivity
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
            	if(data.getStringExtra("result").equals("created") == true) {
            		// refreshList(); broken atm, add something that refreshes whole list after creating event
            		Toast toast = Toast.makeText(getApplicationContext(), "Event created", Toast.LENGTH_SHORT);
                	toast.show();
                }
                if(data.getStringExtra("result").equals("abort") == true) {
                	Toast toast = Toast.makeText(getApplicationContext(), "Event not created", Toast.LENGTH_SHORT);
                	toast.show();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            	Toast toast = Toast.makeText(getApplicationContext(), "Event not created", Toast.LENGTH_SHORT);
            	toast.show();
            }
        }
    }
    
    public void refreshList() {
    	Log.d("ManoaBulletinBoard","Refreshing...");
    	try {
    	// Sync internal database
		((PostApp)getApplication()).SyncEvent();
    	}
    	catch (NullPointerException e) {
        	Toast toast = Toast.makeText(getApplicationContext(), "Can't refresh", Toast.LENGTH_SHORT);
        	toast.show();
        }
    	// Refresh screen
		cursor = ((PostApp)getApplication()).postdata.query();

		// Fill arraylist with all the posts in data, and display it
		if(!post_list.isEmpty()) {
			Log.d("ManoaBulletBoard","Clearing post list");
			post_list.clear();
		}
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Log.i("Database",cursor.getString(cursor.getColumnIndex(PostData.C_ID)));
			Post temppost = new Post(cursor.getString(cursor.getColumnIndex(PostData.C_Title)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Description)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Email)),
									 cursor.getString(cursor.getColumnIndex(PostData.C_Category)),
									 cursor.getDouble(cursor.getColumnIndex(PostData.C_Location_X)),
									 cursor.getDouble(cursor.getColumnIndex(PostData.C_Location_Y)),
									 cursor.getInt(cursor.getColumnIndex(PostData.C_Number)));
			post_list.add(temppost);
			cursor.moveToNext();
		}
    	adapter.notifyDataSetChanged();
    	Log.d("ManoaBulletinBoard","Done refreshing");
    }
    
}