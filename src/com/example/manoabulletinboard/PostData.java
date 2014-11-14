package com.example.manoabulletinboard;


import java.util.Calendar;




import java.util.Locale;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.VoicemailContract.Status;
import android.text.format.Time;
import android.util.Log;

public class PostData {

	public static final String C_ID = "_id";/*Important?*/
	public static final String DB_NAME = "bulletine.db";
	public static final int DB_VERSION = 1;
	
	//public static final String C_Created_at = "created_at";
	public static final String Table = "board";
	public static final String C_Title = "post_title";
	public static final String C_Email = "post_email";
	public static final String C_Category = "post_category";
	public static final String C_Location_X = "post_location_x";
	public static final String C_Location_Y = "post_location_y";
	public static final String C_Number = "post_number";
	public static final String C_Description = "post_description";
	public static final String C_CREATED_AT = "created_at";
	/*Important?*/
	
	
	
	Context context;
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	public PostData(Context context) {
		this.context = context;
		dbHelper = new DbHelper(context);
	}

	public void insert(Post post){
		Calendar c = Calendar.getInstance(Locale.US); 
		int seconds = c.get(Calendar.SECOND);
		
		ContentValues values = new ContentValues();
		values.put(C_Title,post.getName());
		values.put(C_Email,post.getContactEmail());
		values.put(C_Category,post.getCategory());
		values.put(C_Location_X,post.getLocationX());
		values.put(C_Location_Y, post.getLocationY());
		values.put(C_Number,post.getContactNumber());
		values.put(C_Description, post.getDescription());
		values.put(C_CREATED_AT, seconds);
		Log.d("ManoaBulletinBoard","seconds value: " + Integer.toString(seconds));

		
		
		
		/*Get Writable Database using dbHelper class,
		 * create a database if doesn't exist
		 */
		db = dbHelper.getWritableDatabase();
		/*insert while ignoring conflicts
		 * Ignore duplicate id 
		 */
		db.insertWithOnConflict(Table, null, values, SQLiteDatabase.CONFLICT_IGNORE); 
	
	}
	public Cursor query(){
		/*Get Readable Database*/
		db = dbHelper.getReadableDatabase();
		
		/*null will give all
		 * NOTE: A cursor is an iterator for the database
		 * that iterates through each record in the database
		 */
		Cursor cursor = db.query(Table, null, null, null, null, 
				null,C_CREATED_AT + " DESC"); // SELECT * FROM status
		
		return cursor;
		
		
	}

}

class DbHelper extends SQLiteOpenHelper{
	static final String TAG = "DbHelper"; 
			
	public DbHelper(Context context) {
		super(context, PostData.DB_NAME, null, PostData.DB_VERSION);
	}
	
	/*Will only run once, ever, for this
	 * particular use. Will run again only 
	 * when you uninstall then reinstall the app*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		/*Create the database*/
		/*Create a table with given parameters*/
		String sql = String.format("create table %s "+
				"( _id INTEGER PRIMARY KEY AUTOINCREMENT,%s text,%s int, %s text, %s float, %s float, %s text, %s text, %s int)", PostData.Table,PostData.C_Title,
				PostData.C_CREATED_AT,PostData.C_Description,PostData.C_Location_X,PostData.C_Location_Y,PostData.C_Email,PostData.C_Category,PostData.C_Number);
		
		Log.d(TAG,"onCreate with SQL"+sql);
		
		/*Execute sql commands*/
		db.execSQL(sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG,"onUpgrade from"+oldVersion+"to"+newVersion);
		/*Usually ULTER TABLE statement*/
		db.execSQL("drop if exists"+ PostData.Table);
		onCreate(db);
	}
	
}