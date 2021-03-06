package com.example.manoabulletinboard;

import java.sql.ResultSet;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;


public class MainScreen extends ActionBarActivity {

	Button SearchButton;
	EditText SearchText;
	
	ArrayList<Post> post_list = new ArrayList<Post>();
	CustomAdapter adapter;
	
	android.database.Cursor cursor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.ManoaGreen));
        
        SearchText = (EditText) findViewById(R.id.search_text);
        SearchButton = (Button) findViewById(R.id.search_button);
        SearchButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		searchScreen();
        	}
        	
        });

        //pasing context into the application for showing dialog. (turns out only the serachbutton context works)
		((PostApp)getApplication()).setContext(SearchButton.getContext());
        
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
		
		// Set swiperefreshlayout variables
		final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeView.setEnabled(false);
        swipeView.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                refreshList();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
     
                    }
                }, 0);
            }
        });
     
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
     
            }
     
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem == 0)
                        swipeView.setEnabled(true);
                    else
                        swipeView.setEnabled(false);
            }
        });       	
		
		cursor = ((PostApp)getApplication()).postdata.query();
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
//        case R.id.categories_button:
//        	sortByCategories();
//        	return true;
        case R.id.map_screen_button:
        	mapScreen();
        	return true;
        case R.id.create_post_button:
        	createPost();
        	return true;
//        case R.id.action_settings:
//            return true;
        default:
        return super.onOptionsItemSelected(item);
        }
    }
    
    public void sortByCategories() {
    	Intent intent = new Intent(this, CategoriesScreen.class);
    	startActivityForResult(intent, 2); // "2" is the request code for category screen
    }
    
    public void mapScreen() {
    	Intent intent = new Intent(this, MapScreen.class);
    	startActivity(intent);
    }
    
    public void searchScreen() {
    	if(SearchText.getText().toString().matches("")) {
        	Toast toast = Toast.makeText(getApplicationContext(), "Can't do a blank search!", Toast.LENGTH_SHORT);
        	toast.show();
    	}
    	else {
        	Intent intent = new Intent(this, SearchScreen.class);
        	intent.putExtra("search_text", SearchText.getText().toString());
        	startActivity(intent);
    	}
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
            		// refreshList(); broken atm, add something that refreshes whole list after creating event'
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
			Post temppost = new Post(SearchButton.getContext(),
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
			cursor.moveToNext();
		}
    	adapter.notifyDataSetChanged();
    	Log.d("ManoaBulletinBoard","Done refreshing");
    }
    
}