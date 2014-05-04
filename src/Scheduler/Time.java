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

/*********************************************************
 * Course Scheduler
 * File: Time.java
 * 
 * Contains classes:
 * 
 * 		Time:
 * 
 * 			Purpose: To store the hour and date for a course
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;										//define the package as scheduler


/********************************************************
 * Import Serializable class to allow for class to be 
 * 		serialized for storage 
*********************************************************/
import java.io.Serializable;							//imports Serializable class
import java.util.Scanner;


/********************************************************
 * Class: Time
 * 
 * 		@purpose  To store time information for a course
 * 		
 * 		@see Cloneable, Serializeable, Comparable
*********************************************************/
public class Time implements Cloneable, Serializable, Comparable<Time> {
											//defines class as implementing clone, compareTo,
											// and the serialize, deserialize methods
	
	/********************************************************
	 * The following are the instance fields of Time
	*********************************************************/
	private int hour;											//the hour of the instance
	private int minute;											//the minute of the instance
	private boolean am;											//whether the instance is am or pm
	
	
	/********************************************************
	 * The following are private static constants for use within Time
	*********************************************************/
	
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2013010900016L;//object ID
	protected static final long serialVersionUID = 1L + Version.time.id;//serial ID
	

	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Creates a new Time instance with default values
	*********************************************************/
	public Time(){
		hour = 0;										//initialize the Time instance
		minute = 0;										//to default values of hour = 0,
		am = true;										//minute = 0, am = 0
	}
	
	
	/********************************************************
	 * @purpose To clone this Time instance
	 * 
	 * @return Time: a clone of this instance
	*********************************************************/
	@Override
	public Time clone(){
		Time item = new Time();									//create new Time instance
																//to return as the clone
		item.setAm(this.am);									//and set the values as appropriate
		item.setHour(this.hour);								//for the new clone based on this
		item.setMinute(this.minute);							//instance, ie copy value from
																//this instance to the clone, then
		return item;											//return the clone
	}
	
		
	/********************************************************
	 * @purpose to compare two Time instances
	 * 
	 * @param Object other: the object to use for comparison to
	 * 				this instance
	 * 
	 * @return int: whether this instance is LESS, EQUAL, or MORE
	 * 		than the other instance, based first on AM/PM, the hour
	 * 		and then the minutes portion of the time.
	*********************************************************/
	public int compareTo(Time item){
																	//comparison by am, hour
		if (item.getAm() == this.am) {								//then minute. Check am/pm
			int itemHour = (item.getHour() == 12 ? 0 : item.getHour());//fix for 12
			int thisHour = (this.hour == 12 ? 0 : this.hour);		//fix hour for 12
			
			if (itemHour == thisHour){								//check hour, and minute
				if (item.getMinute() == this.minute){				//if all equal then return 
					return Compare.equal.value();									//EQUAL, otherwise
				}													//not equal by minutes											//so compare minutes to find
				return (item.getMinute() > this.minute) ?
						Compare.less.value() : Compare.more.value();//ordering, if item's											//minutes are greater, then this
			}														//is LESS than other, return LESS												//otherwise return MORE, if not equal 
			return (itemHour > thisHour) ? 
					Compare.less.value() : Compare.more.value();//by hours the return LESS if 												//item's hours are greater than other's
		}															//else return MORE, finally if 													//not unequal by hours, then unequal by
		return this.am ? Compare.less.value() : Compare.more.value();//part of day, so return LESS if this
																//is AM (other must be PM) or MORE if 															//this is PM (other must be AM)
	}																
		
	
	/********************************************************
	 * @purpose Sets the hour of the time instance
	 * 
	 * @param int hour: the hour to set this instance's hour to
	 * 
	 * @return boolean: if the hour is valid
	*********************************************************/
	public boolean setHour(int hour){							
		if (hour < 0 || hour > 12)								//check for valid hour of
			return false;										//between 0 and 12, if invalid
		this.hour = hour;										//return false, otherwise set
		return true;											//the hour and return true that
	}															//the hour was set to a valid value
	
	
	/********************************************************
	 * @purpose Returns the hour in the current time instance
	 * 
	 * @return int: hour value of instance
	*********************************************************/
	public int getHour(){
		return hour;											//returns this instances hour
	}
	
	
	/********************************************************
	 * @purpose Set the minute of the current time instance
	 * 
	 * @param int minute: the minute value to update the instance with
	 * 
	 * @return boolean: if the minute is valid
	*********************************************************/
	public boolean setMinute(int minute){
		if (minute < 0 || hour > 59)							//check if the minute value is
			return false;										//valid (between 0 and 59), if invalid
		this.minute = minute;									//return false, otherwise set the minute
		return true;											//value and return true that the instance
	}															//minute value was set
	
	
	/********************************************************
	 * @purpose Returns the minute value of the current instance
	 * 
	 * @return int: minute value of instance
	*********************************************************/
	public int getMinute(){
		return minute;											//return the minute value
	}
	
	
	/********************************************************
	 * @purpose Sets if the time is AM
	 * 
	 * @param boolean am: if the time is AM
	*********************************************************/
	public void setAm(boolean am){
		this.am = am;											//set the AM/PM orientation
	}
	

	/********************************************************
	 * @purpose Return if the time is AM
	 * 
	 * @return boolean: if the time is AM
	*********************************************************/
	public boolean getAm(){
		return am;												//return value of AM/PM orientation
	}
	
	
	/********************************************************
	 * @purpose Return if the time is between (inclusively)
	 * 		start and end
	 * 
	 * @return boolean: if the instance is between start and end
	*********************************************************/
	public boolean isBetween(Time start, Time end){
		int startComp = start.compareTo(this);					//compare this instance to the start time
		int endComp = end.compareTo(this);						//and this to the end time and cache values
																//returned check if either start or end EQUALS 
		if (startComp == Compare.equal.value() || 
				endComp == Compare.equal.value()){				//this, if so return true for isBetween. 
			return true;										//Otherwise check if this is strictly
		}														//between by seeing if start is LESS than this
		else if (startComp == Compare.less.value() && 
				endComp == Compare.more.value()){				//and if end is MORE than this, if so then return
			return true;										//true, otherwise return false, this Time is not
		}														//between the start Time and the end Time
		
		return false;
	}
	
	
	/********************************************************
	 * @purpose Return if the time difference between the 
	 * 		instance and the parameter
	 * 
	 * @param Time finalTime: the final time for abs(finalTime - initial)
	 * 
	 * @return Time: the amount of time between the final time and 
	 * 		the instance time
	*********************************************************/
	public Time timeDifference(Time finalTime){
		Time result = new Time();								//create a new Time for the 
																//result, set the starting hour
		int startHour = this.am ? this.getHour() : this.getHour() + 12;//to this time's hour if am
																//or this time's hour + 12 if pm
		if (startHour == 24)									//check for the 12pm case and adjust
			startHour -= 12;									//for the 12 hour difference, set 
																//the end hour to final time's hour
		int endHour = finalTime.am ? finalTime.getHour() : finalTime.getHour() + 12;//if final is am
																//else set to final time hour + 12
		if (endHour == 24)										//check for 12pm case and adjust
			endHour -= 12;										//for the 12 hour difference
																//find the number of hours difference
		int hourDiff = Math.abs(endHour - startHour);			//and take the absolute value in case
		int minDiff = finalTime.getMinute() - this.minute;		//of reverse order, find the minute 
																//difference, and set the hour difference
		result.setHour((minDiff < 0) ? hourDiff - 1 : hourDiff);//to hourDiff if minute diff is negative
		result.setMinute((minDiff < 0) ? 60 + minDiff : minDiff);//else set to hour diff -1, if min diff 
																//is negative add sixty, else use min diff
		return result;											//store both values and return the result Time
	}
	
	
	/********************************************************
	 * @purpose Return if the time as a string without the am/pm
	 * 		designator
	 * 
	 * @return String: the Time object as a string without the am/pm 
	 * 		designator
	*********************************************************/
	@Override
	public String toString(){
		String min = Integer.toString(this.minute);				//create new string from minute int
		min = (min.length() == 0 ? "0" : "") + min;				//add a zero to the end if necessary
		min = (min.length() == 1 ? "0" : "") + min;
		return new String(this.hour + ":" + min);				//combine the minute and hour strings
	}															//and return the new string
	
	
	/********************************************************
	 * @purpose converts a string of hh:mm to minutes
	 * 
	 * @param String time: the time in hh:mm
	 * 
	 * @return int: the number of minutes in time
	*********************************************************/
	public static int toMinutes(String time){
		int result = 0;
		
		Scanner check = new Scanner(time);						//create scanner on the time string
		check.useDelimiter(":");								//set the proper delimiter
		result += check.nextInt() * 60;							//get the hours and find the minutes
		result += check.nextInt();								//get the minutes
		check.close();
		
		return result;											//return result
	}
}
