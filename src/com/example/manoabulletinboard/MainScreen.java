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
	
	ArrayList<Post> post_list;

	static final String[] FROM = {PostData.C_Title, PostData.C_Description, PostData.C_CREATED_AT};
	static final int[] TO = {R.id.text_user,R.id.text_text,R.id.text_time};
	
	ListView list;
	android.database.Cursor cursor;
	SimpleCursorAdapter adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        SearchButton = (Button) findViewById(R.id.search_button);
        Refresh = (Button)findViewById(R.id.refresh_button);
        
        //for testing Server only *******************
        ServerB = (Button)findViewById(R.id.button1);
        ServerB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Server a;
				ResultSet rs;
				a = new Server(list.getContext());
				double c,b;
				c=b=3.5;
				a.execute("ADD","1","2","3","4","5","6",b,c,"9","10");
//				a.execute("DELETE","a","b","c","d");
//				try{
//					a.execute("SYNC");
//					rs=a.get(5000, TimeUnit.MILLISECONDS);
//					while(rs.next())
//					{
//						Log.i("Server Return",String.valueOf(rs.getInt(1)));
//					}
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
			}
		});
        //********************************************
        
        Refresh.setEnabled(true);
        Refresh.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				cursor = ((PostApp)getApplication()).postdata.query();
				/*Update cursor*/
				adapter.changeCursor(cursor);
			}
        	
        });
        
        /*List View initiation*/
        list = (ListView)findViewById(R.id.main_screen_scroll_view);
        adapter = new SimpleCursorAdapter(this, R.layout.post_view, cursor, FROM, TO);
		
		/*Set ViewBinder*/
		adapter.setViewBinder(View_BINDER);
		
		/*Set adapter*/
		list.setAdapter(adapter);
		
		// Set listener for list clicks (go to ViewPostScreen.java)
		list.setOnItemClickListener(new OnItemClickListener()
		{
		    @Override 
		    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
		    { 	Cursor c = (Cursor) list.getItemAtPosition(position);
		        Intent intent = new Intent(MainScreen.this, ViewPostScreen.class);
		        intent.putExtra("Title", c.getString(c.getColumnIndex("post_title")));
		        Log.d("ManoaBulletinBoard","Added title to intent");
		        intent.putExtra("Email", c.getString(c.getColumnIndex("post_email")));
		        Log.d("ManoaBulletinBoard","Added email to intent");
		        intent.putExtra("Description", c.getString(c.getColumnIndex("post_description")));
		        Log.d("ManoaBulletinBoard","Added description to intent");
		        startActivity(intent);
		    }
		});
		
		// Refresh screen
		cursor = ((PostApp)getApplication()).postdata.query();
		adapter.changeCursor(cursor);
    }

static final ViewBinder View_BINDER = new ViewBinder(){

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			/*If the text from view is not the same 
			 * as created at, return false */
			if(view.getId() !=  R.id.text_time)
				return false;
			
			long time = cursor.getLong(cursor.getColumnIndex(PostData.C_CREATED_AT));
			CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(time);
			((TextView)view).setText(relativeTime);
			
			return true;
		}
		};
    
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
    				cursor = ((PostApp)getApplication()).postdata.query();
    				/*Update cursor*/
    				adapter.changeCursor(cursor);
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
            }
        }
    }
    
}