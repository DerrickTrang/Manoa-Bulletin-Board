package com.example.manoabulletinboard;


import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PostData {

	public static final String DB_NAME = "bulletine.db";
	public static final String Table = "board";
	public static final int DB_VERSION = 1;
	

	public static final String C_ID = "ID";
	public static final String C_IMEI = "IMEI";
	public static final String C_Title = "Title";
	public static final String C_PostDate = "PostDate";
	public static final String C_StartDate = "StartDate";
	public static final String C_EndDate = "EndDate";
	public static final String C_StartTime = "StartTime";
	public static final String C_EndTime = "EndTime";
	public static final String C_Location_X = "Co_X";
	public static final String C_Location_Y = "Co_Y";
	public static final String C_Location = "Location";
	public static final String C_Description = "Description";
	public static final String C_Email = "Contact_email";
	public static final String C_Number = "Contact_number";
	public static final String C_Category = "Category";
	/*Important?*/
	
	
	
	Context context;
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	public PostData(Context context) {
		this.context = context;
		dbHelper = new DbHelper(context);
	}

	public void insert(Post post){
		
		ContentValues values = new ContentValues();
		values.put(C_ID,post.getID());
		values.put(C_IMEI,post.getIMEI());
		Log.d("ManoaBulletinBoard","IMEI in syncevent/postdata = " + post.getIMEI());
		values.put(C_Title,post.getName());
		values.put(C_PostDate, post.getPostDate());
		values.put(C_StartDate,post.getStartDate());
		values.put(C_EndDate,post.getEndDate());
		values.put(C_StartTime,post.getStartTime());
		values.put(C_EndTime,post.getEndTime());
		values.put(C_Location_X,post.getLocationX());
		values.put(C_Location_Y,post.getLocationY());
		values.put(C_Location,post.getLocation());
		values.put(C_Description, post.getDescription());
		values.put(C_Email,post.getContactEmail());
		values.put(C_Number,post.getContactNumber());
		values.put(C_Category,post.getCategory());
		;
		
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
				null,C_ID + " DESC"); // SELECT * FROM status
		
		return cursor;
		
		
	}
	
	//To clear local database.
	public boolean FlushAll()
	{
		return db.delete(Table, null, null) > 0;
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
		String sql = String.format("create table %s "+ "( %s INTEGER,%s text, %s text, %s text, " +
									"%s text, %s text, %s text, %s text, %s double, %s double, " +
									"%s text, %s text, %s text, %s text, %s text)", 
				PostData.Table, PostData.C_ID, PostData.C_IMEI, PostData.C_Title, PostData.C_PostDate,
				PostData.C_StartDate, PostData.C_EndDate, PostData.C_StartTime, PostData.C_EndTime, PostData.C_Location_X, PostData.C_Location_Y, 
				PostData.C_Location, PostData.C_Description,PostData.C_Email, PostData.C_Number, PostData.C_Category);

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