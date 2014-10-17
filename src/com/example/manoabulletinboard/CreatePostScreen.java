package com.example.manoabulletinboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CreatePostScreen extends Activity{
	/*Attribute declaration*/
	int test;
	TextView Title;
	TextView Description;
	TextView Email;
	Spinner Category;
	Button Submit;
	Button Cancel;
	float Location_x;
	float Location_y;
	int Contact_Number;
	
	/*Instantiate dbHelper and a database*/
	DbHelper dbHelper;
	android.database.sqlite.SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ManoaBulletinBoard","Got into create post screen");
		/*Parse newpost_screen XML*/
		setContentView(R.layout.create_post_screen);
		
		/*Assign view variables to xml objects*/
		Title = (TextView)findViewById(R.id.create_post_screen_title);
		Description = (TextView)findViewById(R.id.create_post_screen_description);
		Email = (TextView)findViewById(R.id.create_post_screen_email);
		Category = (Spinner)findViewById(R.id.create_post_screen_categories);
		Submit = (Button)findViewById(R.id.create_post_screen_submit);
		Cancel = (Button)findViewById(R.id.create_post_screen_cancel);
		
		// Enable buttons and set listeners
		Submit.setEnabled(true);
		Cancel.setEnabled(true);
		
        Submit.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Post UserPost = storeViewToObject();
				// Post to server
				// For now, send it back to the main activity for development
				
				
				/*insert into the database*/
				((PostApp)getApplication()).postdata.insert(UserPost);
				
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result", "created");
				Log.d("ManoaBulletinBoard","Created post, sending intent 'created'");
				setResult(RESULT_OK, returnIntent);
				finish();
				
				/*Log is a Debugging tool, error detection etc.*/
				Log.d("create_edit","onClicked");
				Log.d("create_edit","onClicked with text:" + Title.getText());
				
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
		Location_x = 0;
		Location_y = 0;
		Contact_Number = 0;
	}
	
	public Post storeViewToObject(){
		Post UserPost = new Post(Title.getText().toString(),
				Description.getText().toString(),Email.getText().toString(),
				Category.getSelectedItem().toString(),Location_x,Location_y,Contact_Number);
		
		/*Log is a Debugging tool, error detection etc.*/
		Log.d("create_edit","store_view_to_object");
		
		return UserPost;
	
	}
	
	
	
}