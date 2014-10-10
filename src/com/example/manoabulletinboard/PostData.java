package com.example.manoabulletinboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.VoicemailContract.Status;

public class PostData {

	public static final String C_ID = "_id";
	public static final String DB_NAME = "bulletine.db";
	public static final int DB_VERSION = 1;
	
	public static final String C_Created_at = "created_at";
	public static final String Table = "Bulletin board";
	public static final String C_Title = "post_title";
	public static final String C_Email = "post_email";
	public static final String C_Category = "post_category";
	public static final String C_Number = "post_number";
	public static final String C_Description = "post_description";
	
	
	
	Context context;
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	public PostData(Context context){
		this.context = context;
		dbHelper = new DbHelper();
	}
	public void insert(Post post){
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(C_Title,post.getName());
		values.put(C_Email,post.getContactEmail());
		values.put(C_Category,post.getCategory());
		values.put(C_Number,post.getContactNumber());
		values.put(C_Description, post.getDescription());
		db.insert(Table, null, values);
	}
	class DbHelper extends SQLiteOpenHelper{

		public DbHelper() {
			super(context, DB_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		/*onCreate happens only once; when the App is installed*/
		public void onCreate(SQLiteDatabase db) {
			/*Create a table with given parameters*/
			String sql = String.format("create table %s"+
			"(%s varchar(50) primary key, %s varchar(40), %s varchar(40), %s int, %s text)",
			Table,C_Title,C_Email,C_Category,C_Number,C_Description);
		}
		/*onUpgrade is called every time*/
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			/*Alter Table statement*/
			db.execSQL("drop if exists" +Table);
			onCreate(db);
			
		}
		
		
	}


}