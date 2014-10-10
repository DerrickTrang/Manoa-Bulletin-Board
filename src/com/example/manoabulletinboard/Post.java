package com.example.manoabulletinboard;

public class Post {
	String name, description, contact_email, category;
	float location_x, location_y;
	int contact_number;
	// Needs something for time/date - create new Time class or use ints?
	
	// Constructors
	public Post() {
		name = "";
		description = "";
		contact_email = "";
		contact_number = 0;
		category = "";
		location_x = 0;
		location_y = 0;
	}

	public Post(String Name, String Description, String Contact_email, 
			String Category, float Location_x, float Location_y, int Contact_number) {
		name = Name;
		description = Description;
		contact_email = Contact_email;
		contact_number = Contact_number;
		category = Category;
		location_x = Location_x;
		location_y = Location_y;
	}
	
	// Accessors
	boolean isEmpty(){
		boolean empty = true;
		if(name != "") empty = false;
		if(description != "") empty = false;
		if(contact_email != "")empty = false;
		if(category != "")empty = false;
		if(location_x != 0)empty = false;
		if(location_y != 0)empty = false;
		return empty;
		
	}
	String getName() {
		return name;
	}
	
	String getDescription() {
		return description;
	}

	String getContactEmail() {
		return contact_email;
	}
	
	String getCategory() {
		return category;
	}
	
	float getLocationX() {
		return location_x;
	}
	
	float getLocationY() {
		return location_y;
	}
	
	int getContactNumber() {
		return contact_number;
	}
	
	// Mutators
	void setName(String Name) {
		name = Name;
	}
	
	void setDescription(String Description) {
		description = Description;
	}
	
	void setContactEmail(String Contact_email) {
		contact_email = Contact_email;
	}
	
	void setCategory(String Category) {
		category = Category;
	}
	
	void setLocationX(float Location_x) {
		location_x = Location_x;
	}
	
	void setLocationY(float Location_y) {
		location_y = Location_y;
	}
	
	void setContactNumber(int Contact_number) {
		contact_number = Contact_number;
	}
	
}