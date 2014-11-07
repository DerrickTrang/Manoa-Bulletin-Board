package com.example.manoabulletinboard;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewPostScreen extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_post_screen);
		String ViewPostTitle = getIntent().getExtras().getString("Title");
		String ViewPostEmail = getIntent().getExtras().getString("Email");
		String ViewPostDescription = getIntent().getExtras().getString("Description");
		TextView view_post_screen_title = (TextView) findViewById(R.id.view_post_screen_title);
		TextView view_post_screen_email = (TextView) findViewById(R.id.view_post_screen_email);
		TextView view_post_screen_description = (TextView) findViewById(R.id.view_post_screen_description);
		view_post_screen_title.setText(ViewPostTitle);
		view_post_screen_email.setText("Contact Email: " + ViewPostEmail);
		view_post_screen_description.setText(ViewPostDescription);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_post_screen, menu);
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
