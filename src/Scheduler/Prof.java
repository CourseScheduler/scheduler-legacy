/********************************************************
 * Copyright (C) 2008 Course Scheduler Team
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of 
 * 	the GNU General Public License as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * 	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * 	if not, write to:
 * 		Free Software Foundation, Inc.
 * 		59 Temple Place, Suite 330, 
 * 		Boston, MA 02111-1307 USA
********************************************************/

/********************************************************
 * Course Scheduler
 * File: Prof.java
 * 
 * Contains classes:
 * 
 * 		Prof:
 * 
 * 			Purpose: To store Professor information for 
 * 				databasing the courses
 *  
 * @author Mike Reinhold
********************************************************/
package Scheduler;							//declare as member of scheduler package


/*********************************************************
 * Import Serializable class to make this class serializable
*********************************************************/
import java.io.Serializable;				//import Serializable


/*********************************************************
 * Class Prof:
 * 
 * @purpose Stores professor name and rating for the section
 * 		class
 * 
 * @see Comparable, Serializable, Cloneable
*********************************************************/
public class Prof implements Comparable<String>, Serializable, Cloneable{
					//declare the interfaces
	
	/*********************************************************
	 * The following are private fields of the Prof
	*********************************************************/
	private String name;					//the prof's name
	private double rating;					//the prof's rating
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008051700016L;//object ID
	protected static final long serialVersionUID =  1L 
				+ Version.database.id;//serial ID
	
	
	/*********************************************************
	 * The following are private static constants in Prof
	*********************************************************/
	private static final String staff = new String("STAFF");
	private static final int min = 0;
	
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose Constructs the default prof "STAFF", rated 0.0
	*********************************************************/
	public Prof(){
		name = new String(staff);			//set name to "STAFF"
		rating = min;						//set rating to the min
	}
	
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose constructs a new prof based on the other prof
	 * 
	 * @param Prof other: the prof to base this Prof off of
	*********************************************************/
	public Prof(Prof other){
		this.setName(new String(other.name));//set name
		this.setRating(other.getRating());	//set rating
	}
	
	
	/*********************************************************
	 * @purpose clone this prof
	 * 
	 * @return Prof: a clone of this prof
	*********************************************************/
	@Override
	public Prof clone(){
		return new Prof(this);				//return a new prof based on this prof
	}
	
	
	/*********************************************************
	 * @purpose return the Prof's name
	 * 
	 * @return String: the prof's name
	*********************************************************/
	public String getName() {
		return name;						//return the prof's name
	}
	
	
	/*********************************************************
	 * @purpose set the prof's name
	 * 
	 * @param String name: the prof's name
	*********************************************************/
	public void setName(String name) {
		this.name = name;					//set the name
	}
	
	
	/*********************************************************
	 * @purpose get the prof's rating
	 * 
	 * @return double: the prof's rating
	*********************************************************/
	public double getRating() {
		return rating;						//return the rating
	}
	
	
	/*********************************************************
	 * @purpose sets the prof's rating
	 * 
	 * @param double rating: the rating to set
	*********************************************************/
	public void setRating(double rating) {
		this.rating = rating;				//set the prof
	}
	
	
	/*********************************************************
	 * @purpose compare this prof to a string key
	 * 
	 * @param String other: the string key to compare this prof's 
	 * 		name with
	 * 
	 * @return int: the result of the comparison
	*********************************************************/
	public int compareTo(String other){
		return this.name.compareTo(other);	//return comparison result
	}
	
	/*********************************************************
	 * @purpose returns if another object is equal to this one
	 * 
	 * @param Object other: the object to check for equality
	 * 
	 * @return boolean: if the objects are equal
	 * 
	 * @see Override Object.equals(Object other)
	*********************************************************/
	@Override
	public boolean equals(Object other){
		try{
			return (compareTo(((Prof)other).name) == 0);
		}									//return prof comparison
		catch(Exception ex){
			return false;					//or false
		}
	}
	
	
	/*********************************************************
	 * @purpose returns the professor as an object
	 * 
	 * @return String: the professor as a String
	 * 
	 * @see Override Object.toString()
	*********************************************************/
	@Override
	public String toString(){
		return new String(this.name);		//return prof's name as a string
	}
}
