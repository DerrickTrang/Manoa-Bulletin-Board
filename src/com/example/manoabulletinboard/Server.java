package com.example.manoabulletinboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.os.AsyncTask;
import android.util.Log;


public class Server extends AsyncTask<Object, Integer, ResultSet>{

	private static final String IP = "128.171.61.142";		//School: 128.171.61.142
	private static final int MySQL_Port = 3808;				
	private static final String Username = "attkk2014";	//subject to change after testing
	private static final String Password = "Attkkee396";	//subject to change with the account
	
	private static final String DATABASE_NAME = "Manoa_Bulletin_Board";
	private static final String DATABASE_TABLE = "EventTable";
	
	private Connection conn;
	private Statement stmt;

	private ResultSet DataFromServer = null;
	
	interface ServerProgreeDialog {
		void onPreExecute();
		void onPostExecute(ResultSet result);
	}

	ServerProgreeDialog myDialog;
	
	public Server(ServerProgreeDialog dialog)
	{
		myDialog = dialog;
	}
	
	//onPreExecute display a dialog while the phone is updating info with the server
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		myDialog.onPreExecute();
		Log.i("Server","C2");
	}
	
	//After info have been updated, dialog close
	protected void onPostExecute(ResultSet result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		myDialog.onPostExecute(result);
	}
	
	//Based on the input parameters, perform the following:
	//ADD an event to the Database
	//SYNC events from the Database
	//DELETE an event from the Database
	@Override
	protected ResultSet doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Log.i("Server","C-1");
		ResultSet DataFromServer = null;
		Log.i("Server","C0");
		conn = EstablishConnection();
		Log.i("Server","C1");
		if(conn == null)
		{
			Log.e("Server","Connection is null");
			return DataFromServer;
		}
		
		String action = (String) params[0];
		switch(action)
		{
			//initialize connection.
			case "INI":
			break;
			
			case "ADD":
				Log.i("Server","Adding Event");
				//1.IMEI 2.Title 3.PostDate 4.EventDate 5.StartTIme 6.EndTime 7.Co_x 8.Co_y 9.Location 10.Description 11.Contact_email 12. Contact_number 13.category
				if(AddEvent((Post)params[1]) != 0) 
				Log.i("Server","Done Adding Event");
				else 
				Log.e("Server","Error occurred during adding event");
				
				break;
			
			case "SYNC":
				Log.i("Server","Synchronizing Event");
				DataFromServer = SyncEvent();
				break;
				
			case "DELETE":
				Log.i("Server","Deleting Event");
				DeleteEvent((int)params[1],(String)params[2]);
				Log.i("Server","Done Deleting Event");
				break;
				
			case "ADDSYNC":
				Log.i("Server","Adding Event");
				//1.IMEI 2.Title 3.PostDate 4.EventDate 5.StartTIme 6.EndTime 7.Co_x 8.Co_y 9.Location 10.Description 11.Contact_email 12. Contact_number 13.category
				if(AddEvent((Post)params[1]) != 0) 
				Log.i("Server","Done Adding Event");
				else 
				Log.e("Server","Error occurred during adding event");
				Log.i("Server","Synchronizing Event");
				DataFromServer = SyncEvent();
				break;
				
			default:
				Log.w("Server","Invalid request parameter");
		}
		return DataFromServer;
	}
	
	// Perform connection to the Server
	private Connection EstablishConnection()
	{
		Log.i("Server","MySQL Establishing Connection");
		Connection conn = null;
		try{
			String driver = "com.mysql.jdbc.Driver";
			Class.forName(driver).newInstance();
			String connString = "jdbc:mysql://" + IP + ":" + MySQL_Port + "/" + DATABASE_NAME;
			conn = DriverManager.getConnection(connString,Username,Password);
			stmt = conn.createStatement();
			Log.i("Server","Connection Established");
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return conn;
	}
	
	// Add an event with its info into database
	private int AddEvent(Post post)
	{
		String IMEI = post.getIMEI();
		String Title = post.getName();
		String PostDate = post.getPostDate();
		String EventDate = post.getEventDate();
		String StartTime = post.getStartTime();
		String EndTime = post.getEndTime();
		double Co_x = post.getLocationX();
		double Co_y = post.getLocationY();
		String Location = post.getLocation();
		String Description = post.getDescription();
		String Contact_email = post.getContactEmail();
		int Contact_number = post.getContactNumber();
		String Category = post.getCategory();
		 
		int updated_rows = 0;
		try {
			updated_rows = stmt.executeUpdate("INSERT INTO `" + DATABASE_NAME + "`.`" + DATABASE_TABLE + "` " +
											"(`IMEI`, `Title`, `PostDate`, `EventDate`, `StartTime`, `EndTime`, `Co_X`, `Co_Y`, `Location`, `Description`, `Contact_email`, `Contact_number`, `Category`) " +
									 "VALUES ('"+IMEI+"', '"+Title+"', '"+PostDate+"', '"+EventDate+"', '"+StartTime+"', '"+EndTime+"', '"+Co_x+"', '"+Co_y+"', '"+Location+"', '"+Description+"', '"+Contact_email+"', '"+Contact_number+"', '"+Category+"');");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updated_rows;
	}
	
	// Synchronize events from server to cell phone
	private ResultSet SyncEvent()
	{
		try {
			DataFromServer = stmt.executeQuery("SELECT * FROM " + DATABASE_NAME + "." + DATABASE_TABLE + " ORDER BY ID DESC;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DataFromServer;
	}
	
	private int DeleteEvent(int ID, String imei)
	{
		int updated_rows = 0;
		
		try {
			stmt.executeUpdate("DELETE FROM `"+ DATABASE_NAME + "`.`"+ DATABASE_TABLE + "` " +"WHERE `ID`='"+ID+"' AND `IMEI` = '"+imei+"';");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updated_rows;
	}
}
