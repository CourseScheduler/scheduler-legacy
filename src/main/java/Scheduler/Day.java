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
 * File: Day.java
 * 
 * Contains enumerator:
 * 
 * 		Day:
 * 
 * 			Purpose: To typesafe the Day parameters
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;

import java.util.HashMap;
import java.util.Map;


/*********************************************************
 * Enumerator: Day:
 * 
 * @purpose To simplify and typesafe the Day value for the
 * 				Boolean array index
*********************************************************/
public enum Day {
	
	
	/*********************************************************
	 * The following are the values of the enumerator and their
	 * 		fields
	*********************************************************/	
	monday (0, "Monday", "M"),					//monday index
	tuesday (1, "Tuesday", "T"),				//tuesday index
	wednesday (2, "Wednesday", "W"),			//wednesday index
	thursday (3, "Thursday", "R"),				//thursday index
	friday (4, "Friday", "F");					//friday index
	
	
	/*********************************************************
	 * The following are the fields of the Day enum
	*********************************************************/
	private final int value;				//index field 
	private final String name;				//name field
	private final String letter;			//character field
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2013010900016L;//object ID
	
	/**
	 * Map of the character codes to their enum for easy valuation
	 */
	private static Map<String, Day> dayMap;
	
	/**
	 * static initializer to build the day map
	 */
	static {
		dayMap = new HashMap<String, Day>();
		
		for(Day day: Day.values()){
			dayMap.put(day.letter, day);
		}
	}
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose To construct a new Day enumerator object
	 * 
	 * @param int value: the value to pass to the enumerator
	*********************************************************/
	Day(int value, String name, String letter){
		this.value = value;					//set the instance's value
		this.name = name;					//set the instance's value
		this.letter = letter;				//set the instance's value
	}
	
	
	/*********************************************************
	 * @purpose Returns the value field of the enumerator
	 * 
	 * @return int: the value of the enumerator
	*********************************************************/
	protected int value(){
		return this.value;					//return the instance's value
	}

	
	/*********************************************************
	 * @purpose Returns the name field of the enumerator
	 * 
	 * @return String: the name of the enumerator
	*********************************************************/
	@Override
	public String toString(){
		return this.name;					//return the instance's name
	}
	
	
	/*********************************************************
	 * @purpose Returns the letter field of the enumerator
	 * 
	 * @return String: the letter associated with the day
	*********************************************************/
	public String getCharStr(){
		return this.letter;					//return the letter
	}
	
	/**
	 * Return the day corresponding to the specified string
	 *
	 * @param value the day string
	 * @return the day represented by the string
	 */
	public static Day getDay(String value){
		return dayMap.get(value);
	}
}
