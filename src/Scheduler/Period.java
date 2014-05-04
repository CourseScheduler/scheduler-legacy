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
 * File: Period.java
 * 
 * Contains classes:
 * 
 * 		Course:
 * 
 * 			Purpose: To store course time periods
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;							//Define class as member of Scheduler package


/********************************************************
 * Import Serializable class to allow for class to be 
 * 		serialized for storage
 * Import Scanner for parsing strings
*********************************************************/
import java.io.Serializable;				//Import Serializable class, Scanner, and Pattern
import java.util.Scanner;					//to allow serializing the data, scanning and parsing text


/********************************************************
 * Class: Period
 * 
 * 		@purpose To store the time period that the course uses
 * 
 * 		@see Cloneable, Serializeable, Comparable
*********************************************************/
public class Period implements Cloneable, Serializable, Comparable<Period> {
						//define Period to use clone, compareTo, serialize, and deserialize
	
	
	/********************************************************
	 * The following are private fields for use within Period
	*********************************************************/
	private Time startTime;									//the start time of the period
	private Time endTime;									//the end time of the period
	private String period;									//the period as a string
	private String duration;								//the duration of the period
	private boolean[] days;									//period days
	
	
	/********************************************************
	 * The following are private static constants for use within Period
	*********************************************************/
	private static Time nullTime = new Time();
	private static int hour = 60;
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2013010900051L;//object ID
	protected static final long serialVersionUID = 1L + 
			Version.period.id;//serial ID
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Creates a new Period and initializes its fields
	*********************************************************/
	public Period(){
		startTime = new Time();								//initializes and sets the  
		endTime = new Time();								//fields to default values
		period = new String("");							//for the new Period
		duration = new String("");	
		nullTime.setAm(true);
		nullTime.setHour(12);
		nullTime.setMinute(0);
		days = new boolean[Day.values().length];
	}
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose  Create a new string and initializes the fields
	 * 		based on a string in the format HH:MM-HH:MMam or 
	 * 		HH:MM-HH:MMpm
	 * 
	 * @param String period: tje period in the format HH:MM-HH:MMam or 
	 * 		HH:MM-HH:MMpm
	*********************************************************/
	public Period(String period){
		this.period = new String(period);					//create new string with same
		if (days == null){
			days = new boolean[Day.values().length];		//text and set string value
		}
		
		setTimes(period);									//to new string, set other
	}														//fields
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose  Create a new string and initializes the fields
	 * 		based on a string in the format HH:MM-HH:MMam or 
	 * 		HH:MM-HH:MMpm
	 * 
	 * @param int minutes: the duration of the period in minutes
	*********************************************************/
	public Period(int minutes){
		String duration = new String();						//new string for the duration
		duration = Integer.toString(minutes/hour);			//get the hours
		String min = Integer.toString(minutes%hour);		//get the minutes
		duration += (":" + ((min.length() == 1) ? ("0" + min) : min));//build the period string
		
		this.period = new String("0:00-" + duration + "am");
		
		days = new boolean[Day.values().length];
		setTimes(period);		
	}
	
	
	/*********************************************************
	 * @purpose Returns an exact copy of this instance at a 
	 * 		separate memory location
	 * 
	 * @return Period: the copy of the period
	*********************************************************/
	@Override
	public Period clone(){
		Period item = new Period();							//return a clone of this instance
		item.setDays(this.getDays());
		item.setStartTime(startTime);
		item.setEndTime(endTime);
		item.setDuration(duration);
		item.period = (startTime.toString() + "-" + endTime.toString() + (endTime.getAm() ? "am" : "pm"));
		return item;
	}
	
	
	/*********************************************************
	 * @purpose Compares two Periods and returns if this instance
	 * 		is LESS than, EQUAL to, or MORE than the other object
	 * 
	 * @param Object other: the object to compare this instance to
	 * 
	 * @return int: the result of the comparison, standard compareTo
	*********************************************************/
	public int compareTo(Period item){						//if start times are equal, return
		if (this.startTime.compareTo(item.getStartTime()) == Compare.equal.value()) {//comparison of end times
			return this.endTime.compareTo(item.getEndTime());//else return the comparison of
		}													//the start times as the comparison of the periods. If object cannot be cast
		return this.startTime.compareTo(item.getStartTime());//return LESS to make valid period LESS than the invalid Period
	}
	
	
	/*********************************************************
	 * @purpose Returns the Period as a string
	 * 
	 * @return String: The period as a string
	*********************************************************/
	@Override
	public String toString(){
		return new String(this.period);						//return this Period as a new string
	}
	
	
	/*********************************************************
	 * @purpose sets the fields of the period to the correct values
	 * 		based on the parameter String in the form HH:MM-HH:MMam
	 * 		or HH:MM-HH:MMpm
	 * 
	 * @return boolean: If the change was successful
	*********************************************************/
	public boolean setPeriod(String period){
		this.period = new String(period);					//set period string to new string
															//based on the input string
		return setTimes(period);							//return the result of attempting 
	}														//to set the times based on the input
	
	
	/*********************************************************
	 * @purpose Determines if this period conflicts with another
	 * 		period
	 * 
	 * @param Period other: the period to check for a conflict
	 * 
	 * @return boolean: if the two periods conflict
	*********************************************************/
	public boolean conflictsWith(Period other){
		Time oStart = other.getStartTime();					//get other's start time and end time
		Time oEnd = other.getEndTime();						//check if this instance's start or 
		
		boolean[] otherDays = other.getDays();
		
		if((this.days[Day.monday.value()] && otherDays[Day.monday.value()]) ||
			(this.days[Day.tuesday.value()] && otherDays[Day.tuesday.value()]) ||
			(this.days[Day.wednesday.value()] && otherDays[Day.wednesday.value()]) ||
			(this.days[Day.thursday.value()] && otherDays[Day.thursday.value()]) ||
			(this.days[Day.friday.value()] && otherDays[Day.friday.value()])){
				
																//end is between other's start and end
			if (startTime.isBetween(oStart, oEnd) || 			//if either case then return true for
					endTime.isBetween(oStart, oEnd))			//conflicting. Then check if other's 
				return true;									//start or end is between is between 
			else if(oStart.isBetween(startTime, endTime) ||		//this instance's start and end, if either
					oEnd.isBetween(startTime, endTime))			//case return true for conflicting. O
				return true;									//Otherwise periods do not overlap,
			return false;										//return false
		}
		return false;
	}
	
		
	/*********************************************************
	 * @purpose Determines if this period contains another
	 * 		period
	 * 
	 * @param Period other: the period to check for containment
	 * 
	 * @return boolean: if this period contains the other
	*********************************************************/
	public boolean contains(Period other){
		Time oStart = other.getStartTime();					//get the other period's start and end time
		Time oEnd = other.getEndTime();						
				
		if(oStart.isBetween(startTime, endTime)){			//check if the other's start is inside this
			if(oEnd.isBetween(startTime, endTime)){			//period and if the other's end is inside this
				return true;								//period. if so return true, else return false
			}
		}
		return false;
	}
	
	
	/*********************************************************
	 * @purpose Return the start Time of the period
	 * 
	 * @return Time: the start Time of the Period
	*********************************************************/
	protected Time getStartTime(){
		return startTime.clone();							//return a startTime clone
	}
	
	
	/*********************************************************
	 * @purpose Return the end Time of the Period
	 * 
	 * @return Time: the end Time of the Period
	*********************************************************/
	protected Time getEndTime(){
		return endTime.clone();								//return an endTime clone
	}
	
	
	/*********************************************************
	 * @purpose Returns the duration of the Period
	 * 
	 * @return String: the duration as a string
	*********************************************************/
	public String getDuration(){
		return new String(duration);						//return a duration clone	
	}
	
	
	/*********************************************************
	 * @purpose Returns the days for the period
	 * 
	 * @return boolean[]: the days for the period
	*********************************************************/
	public boolean[] getDays(){
		return this.days.clone();							//return the days for the period
	}
	
	
	/*********************************************************
	 * @purpose Sets the fields of this Period to the correct 
	 * 		values based on the input string in the format
	 * 		HH:MM-HH:MMam or HH:MM-HH:MMpm
	 * 
	 * @param String period: the input string to set the fields based on
	 * 
	 * @return boolean: if the field setting was successful
	*********************************************************/
	private boolean setTimes(String period){
		try{
			Scanner times = new Scanner(period);			//create a scanner to parse the input
			times.useDelimiter(" - ");						//with a delimiter of "-" to separate 
															//the two times. Store the two times
			String first = times.next();					//to two string, first and second
			String second = times.next();					//Create two new Time instances to 
															//hold the start and end times.
			startTime = new Time();
			endTime = new Time();
			
			times.close();
			times = new Scanner(first);						//reset scanner to read from the first
			times.useDelimiter(":");						//time and parse by ":" to separate
															//hours from minutes. Store the hours 
			startTime.setHour(times.nextInt());				//and minutes to the hour and minute
			String temp = times.next();
			times.close();
			times = new Scanner(temp);
			startTime.setMinute(times.nextInt());			//fields of the start time
			
			String startAm = first.substring(first.length() - 2);
			
			times.close();
			times = new Scanner(second);					//substringing, create a new scanner
			times.useDelimiter(":");						//on the new string parsing by ":"
															//get the hours and the minutes from 
			endTime.setHour(times.nextInt());				//the substring and store to the end
			temp = times.next();
			times.close();
			times = new Scanner(temp);
			endTime.setMinute(times.nextInt());				//time's hour and minute fields.
															//get the am/pm from the second time
			String endAm = second.substring(second.length() - 2);//set the end Time's am field to
															//the text read from the string
			times.close();
			
			startTime.setAm((startAm.compareTo("am") == Compare.equal.value()) ? true : false);
			endTime.setAm((endAm.compareTo("am") == Compare.equal.value()) ? true : false);
			
			duration = startTime.timeDifference(endTime).toString();//duration as the difference
															//final - initial as a string
			return true;									//return successful
		}
		catch(Exception ex){
			duration = new String("0:00");					//set appropriate null time for
			startTime = nullTime.clone();					//TBA class times
			endTime = nullTime.clone();
			return false;									//return false if unable to parse
		}
	}
	
	
	/*********************************************************
	 * @purpose Returns the percent outside the specified period
	 * 		this period is
	 * 
	 * @param Period other: the period that this is outside of
	 * 
	 * @return double: the percent outside the other period
	 * 		this period is
	*********************************************************/
	public double percentOutside(Period other){
		final int percent = 100;						//define percent constant
		final int none = 0;								//define none constant
		
		Time oStart = other.getStartTime();				//get other start time
		Time oEnd = other.getEndTime();					//get other end time
		
		if (oStart == null || oEnd == null){
			return none;
		}
		
		double thisVal = Time.toMinutes(this.duration);	//get minute duration
		
		if (startTime.isBetween(oStart, oEnd)){			//check start time containment
			if (endTime.isBetween(oStart, oEnd)){		//check end time containment
				return none;							//no percent outside
			}
			Period overFlow = new Period();			//get new period
			overFlow.setTimes(oEnd.toString() + "-" +//set new period to the time outside this period is
					endTime.toString() + (endTime.getAm() ? "am" : "pm"));
			double overTime = Time.toMinutes(overFlow.duration);//convert the outside time to a double
			return overTime/thisVal * percent; 		//calculate and return overflow	
		}										
		if (endTime.isBetween(oStart, oEnd)){		//check end time containment
			Period overFlow = new Period();			//get new period and set to the time outside this period
			overFlow.setTimes(startTime.toString() + "-" +
					oStart.toString() + (startTime.getAm() ? "am" : "pm"));
			double overTime = Time.toMinutes(overFlow.duration);//get outside time as a double
			return overTime/thisVal * percent; 		//calculate and return overflow
		}
		return percent;							//return 100%	
	}
	
	
	/*********************************************************
	 * @purpose Returns the duration of the period in minutes as
	 * 		a double
	 * 
	 * @return double: the number of minutes this period lasts
	*********************************************************/
	public double getDurationMin(){
		double result = 0;							//create result placeholder
		
		Scanner parse = new Scanner(this.duration); //create scanner to parse the string
		parse.useDelimiter(":");					//use the ":" delimiter for "8:00" etc
		result = 60 * parse.nextDouble();			//get hour and mult by 60 minutes
		result += parse.nextDouble();				//get the minutes and adds to result
		parse.close();
		
		return result;								//return the result
	}
	
	
	/*********************************************************
	 * @purpose Sets the periods days field
	 * 
	 * @param boolean[] days: sets the period's days field
	*********************************************************/
	public void setDays(boolean[] days){
		this.days = days;							//set the days
	}


	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}


	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}


	public void setDuration(String duration) {
		this.duration = duration;
	}
}
