package com.example.manoabulletinboard;

import android.app.Application;

/*Application Class to be reused*/
public class PostApp extends Application {

	PostData postdata;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		postdata = new PostData(this);
	}
	
	
	/*TODOs*/
	/*Fetch data from server method*/
}
