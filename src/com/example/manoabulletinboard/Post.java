package com.example.manoabulletinboard;

import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;

public class Post {
	private String IMEI, Title, PostDate, StartDate, EndDate, StartTime, EndTime, description, contact_email, contact_number, category,Location;
	private double location_x, location_y;
	private int ID;
	// Needs something for time/date - create new Time class or use ints?
	
	// Constructors
	public Post() {
		ID = -1;
		IMEI = "";
		Title = "";
		PostDate = "0000-00-00";
		StartDate = "0000-00-00";
		EndDate = "0000-00-00";
		StartTime = "00:00:00";
		EndTime = "00:00:00";
		description = "";
		contact_email = "";
		contact_number = "000-000-0000";
		category = "";
		Location = "";
		location_x = 0;
		location_y = 0;
	}

	//generate postdate, imei while inserting the post into database, not here.
	public Post(Context c, int id, String title, String startdate, String enddate, String starttime, String endtime, double locationx, double locationy, String location, String Description, String email, String number, String Category)
	{
		Calendar Ca = Calendar.getInstance(Locale.US); 
		String year = String.valueOf(Ca.get(Calendar.YEAR));
		String month = String.valueOf(Ca.get(Calendar.MONTH)+1);
		String date = String.valueOf(Ca.get(Calendar.DATE));
		String hour = String.valueOf(Ca.get(Calendar.HOUR_OF_DAY));
		String minute = String.valueOf(Ca.get(Calendar.MINUTE));
		String second = String.valueOf(Ca.get(Calendar.SECOND));

		ID = id; 
		//Unique Android ID
		IMEI = Secure.getString(c.getContentResolver(),Secure.ANDROID_ID);
		Title = title;
		//MYSQL date datatype format: YYYY-MM-DD
		PostDate = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
		StartDate = startdate;
		EndDate = enddate;
		StartTime = starttime;
		EndTime = endtime;
		location_x = locationx;
		location_y = locationy;
		Location = location;
		description = Description;
		contact_email = email;
		contact_number = number;
		category = Category;
	}
	
	
	// Accessors
	boolean isEmpty(){
		boolean empty = true;
		if(Title != "") empty = false;
		if(description != "") empty = false;
		if(contact_email != "")empty = false;
		if(category != "")empty = false;
		if(location_x != 0)empty = false;
		if(location_y != 0)empty = false;
		return empty;
		
	}
	
	int getID(){
		return ID;
	}
	
	String getIMEI(){
		return IMEI;
	}
	
	String getName() {
		return Title;
	}
	
	String getPostDate(){
		return PostDate;
	}
	
	String getStartDate(){
		return StartDate;
	}
	
	String getEndDate(){
		return EndDate;
	}
	
	String getStartTime(){
		return StartTime;
	}
	
	String getEndTime(){
		return EndTime;
	}
	
	double getLocationX() {
		return location_x;
	}
	
	double getLocationY() {
		return location_y;
	}
	
	String getLocation() {
		return Location;
	}
	
	String getDescription() {
		return description;
	}

	String getContactEmail() {
		return contact_email;
	}

	String getContactNumber() {
		return contact_number;
	}
	
	String getCategory() {
		return category;
	}
	
	
	// Mutators
	void setID(int id){
		ID = id;
	}
	
	void setName(String Name) {
		Title = Name;
	}
	
	void setStartDate(String Date){
		StartDate = Date;
	}
	
	void setEndDate(String Date){
		EndDate = Date;
	}
	
	void setStartTime(String Time){
		StartTime = Time;
	}	
	
	void setEndTime(String Time){
		EndTime = Time;
	}

	void setLocationX(double Location_x) {
		location_x = Location_x;
	}
	
	void setLocationY(double Location_y) {
		location_y = Location_y;
	}
	
	void setLocation(String location){
		Location = location;
	}
	
	void setDescription(String Description) {
		description = Description;
	}
	
	void setContactEmail(String Contact_email) {
		contact_email = Contact_email;
	}

	void setContactNumber(String Contact_number) {
		contact_number = Contact_number;
	}
	
	void setCategory(String Category) {
		category = Category;
	}
	
	
}