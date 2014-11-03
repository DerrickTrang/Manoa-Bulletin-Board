package com.example.manoabulletinboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class Server extends AsyncTask<Object, Integer, ResultSet>{

	private static final String IP = "128.171.61.142";		//School: 128.171.61.142
	private static final int MySQL_Port = 3808;				
	private static final String Username = "attkk2014";	//subject to change after testing
	private static final String Password = "Attkkee396";	//subject to change with the account
	
	private static final String DATABASE_NAME = "Manoa_Bulletin_Board";
	private static final String DATABASE_TABLE = "EventTable";
	
	private final Context ourContext;
	private ProgressDialog nDialog;
	
	private Connection conn;
	private Statement stmt;
	
	public Server(Context c)
	{
		ourContext = c;
	}
	
	//onPreExecute display a dialog while the phone is updating info with the server
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog = new ProgressDialog(ourContext);
		nDialog.setMessage("Loading..");
		nDialog.setIndeterminate(true);
		nDialog.setCancelable(false);
		nDialog.show();
	}
	
	//After info have been updated, dialog close
	protected void onPostExecute(ResultSet result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		nDialog.dismiss();
	}
	
	//Based on the input parameters, perform the following:
	//ADD an event to the Database
	//SYNC events from the Database
	//DELETE an event from the Database
	@Override
	protected ResultSet doInBackground(Object... params) {
		// TODO Auto-generated method stub
		ResultSet DataFromServer = null;
		conn = EstablishConnection();
		if(conn == null)
			return DataFromServer;
		
		String action = (String) params[0];
		switch(action)
		{
			case "ADD":
				Log.i("Server","Adding Event");
				//1.IMEI 2.Title 3.PostDate 4.EventDate 5.StartTIme 6.EndTime 7.Co_x 8.Co_y 9.Location 10.Contents
				if(AddEvent((String)params[1],
							(String)params[2],
							(String)params[3],
							(String)params[4],
							(String)params[5],
							(String)params[6],
							(Double)params[7],
							(Double)params[8],
							(String)params[9],
							(String)params[10]) != 0) 
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
				DeleteEvent((String)params[1],(String)params[2],(String)params[3],(String)params[4]);
				Log.i("Server","Done Deleting Event");
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
		}
		return conn;
	}
	
	// Add an event with its info into database
	private int AddEvent(String IMEI,
						 String Title,
						 String PostDate,
						 String EventDate,
						 String StartTime,
						 String EndTime,
						 Double Co_x,
						 Double Co_y,
						 String Location,
						 String Contents)
	{
		int updated_rows = 0;
		try {
//			updated_rows = stmt.executeUpdate("INSERT INTO `" + DATABASE_NAME + "`.`" + DATABASE_TABLE + "` " +
//											"(`IMEI`, `Title`, `PostDate`, `EventDate`, `StartTime`, `EndTime`, `Co-X`, `Co-Y`, `Location`, `Contents`) " +
//									 "VALUES ('123456789012345', 'Testing', '2014-10-28', '2014-10-29', '00:17:00', '03:17:00', '1.3', '2.4', 'POST 127', 'Testing 1');");
			updated_rows = stmt.executeUpdate("INSERT INTO `" + DATABASE_NAME + "`.`" + DATABASE_TABLE + "` " +
											"(`IMEI`, `Title`, `PostDate`, `EventDate`, `StartTime`, `EndTime`, `Co-X`, `Co-Y`, `Location`, `Contents`) " +
									 "VALUES ('"+IMEI+"', '"+Title+"', '"+PostDate+"', '"+EventDate+"', '"+StartTime+"', '"+EndTime+"', '"+Co_x+"', '"+Co_y+"', '"+Location+"', '"+Contents+"');");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updated_rows;
	}
	
	// Synchronize events from server to cell phone
	private ResultSet SyncEvent()
	{
		ResultSet DataFromServer = null;
		try {
			DataFromServer = stmt.executeQuery("SELECT * FROM " + DATABASE_NAME + "." + DATABASE_TABLE + " ORDER BY ID DESC;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DataFromServer;
	}
	
	private int DeleteEvent(String IMEI,
							String Title,
							String PostDate, 
							String Contents)
	{
		
//		IMEI = "123456789012345";
//		Title = " Testing";
//		PostDate = "2014-10-28";
//		Contents = "Testing 1";
		
		int updated_rows = 0;
		
		try {
			stmt.executeUpdate("DELETE FROM `"+ DATABASE_NAME + "`.`"+ DATABASE_TABLE + "` " +
							   "WHERE `IMEI`='"+IMEI+"' AND `Title` = '"+Title+"' AND `PostDate` = '"+PostDate+"' AND `Contents` = '"+Contents+"';");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updated_rows;
	}
}
