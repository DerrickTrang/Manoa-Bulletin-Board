package com.example.manoabulletinboard;

import java.sql.ResultSet;

import com.example.manoabulletinboard.Server.ServerProgreeDialog;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/*Application Class to be reused*/
public class PostApp extends Application implements ServerProgreeDialog{

	private ProgressDialog nDialog;
	private static Context context;
	PostData postdata;
	ResultSet ServerData;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		postdata = new PostData(this);
	}
	
	public void setContext(Context c){
		context = c;
		new Server(this).execute("SYNC");
	}
	
	
	/*TODOs*/
	/*Fetch data from server method*/
	public boolean AddEvent(Post post)
	{
		new Server(this).execute("ADDSYNC",post);
		//Delete local Data before adding new data from server
		
		postdata.FlushAll();
		try 
		{
			Post Newpost;
			
			while(ServerData.next())
			{
				int ID = ServerData.getInt(PostData.C_ID);
				String Title = ServerData.getString(PostData.C_Title);
				String EventDate = ServerData.getString(PostData.C_EventDate);
				String StartTime = ServerData.getString(PostData.C_StartTime);
				String EndTime = ServerData.getString(PostData.C_EndTime);
				float LocationX = ServerData.getFloat(PostData.C_Location_X);
				float LocationY = ServerData.getFloat(PostData.C_Location_Y);
				String Location = ServerData.getString(PostData.C_Location);
				String Description = ServerData.getString(PostData.C_Description);
				String Email = ServerData.getString(PostData.C_Email);
				int Number = ServerData.getInt(PostData.C_Number);
				String Category = ServerData.getString(PostData.C_Category);
				
				Newpost = new Post(context,ID,Title,EventDate,StartTime,EndTime,LocationX,LocationY,Location,Description,Email,Number,Category);

				postdata.insert(Newpost);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//Sync event mean synchronize events from the server to local, NEVER does the opposite.
	public boolean SyncEvent()
	{
		Log.i("d","h1");
		//Delete local Data before adding new data from server
		postdata.FlushAll();
		Log.i("d","h2");
		try 
		{
			Post Newpost;
			Log.i("d","h3");
			new Server(this).execute("SYNC");

			Log.i("d","h4");
			while(ServerData.next())
			{
				Log.i("d","h5");
				int ID = ServerData.getInt(PostData.C_ID);
				String Title = ServerData.getString(PostData.C_Title);
				String EventDate = ServerData.getString(PostData.C_EventDate);
				String StartTime = ServerData.getString(PostData.C_StartTime);
				String EndTime = ServerData.getString(PostData.C_EndTime);
				float LocationX = ServerData.getFloat(PostData.C_Location_X);
				float LocationY = ServerData.getFloat(PostData.C_Location_Y);
				String Location = ServerData.getString(PostData.C_Location);
				String Description = ServerData.getString(PostData.C_Description);
				String Email = ServerData.getString(PostData.C_Email);
				int Number = ServerData.getInt(PostData.C_Number);
				String Category = ServerData.getString(PostData.C_Category);

				Newpost = new Post(context,ID,Title,EventDate,StartTime,EndTime,LocationX,LocationY,Location,Description,Email,Number,Category);

				postdata.insert(Newpost);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean DeleteEvent(Post post)
	{
		new Server(this).execute("DELETE", post.getID(), post.getIMEI());
		return false;
	}


	@Override
	public void onPreExecute() {
		// TODO Auto-generated method stub
		nDialog = new ProgressDialog(context);
		nDialog.setMessage("Loading..");
		nDialog.setIndeterminate(true);
		nDialog.setCancelable(false);
		nDialog.show();
	}

	@Override
	public void onPostExecute(ResultSet result) {
		// TODO Auto-generated method stub
		nDialog.dismiss();
		ServerData = result;
	}

}
