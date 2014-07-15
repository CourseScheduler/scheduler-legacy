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
 * File: Compare.java
 * 
 * Contains: enumerator:
 * 
 * 		Compare:
 * 
 * 			Purpose: Defines typesafe values for returning 
 * 				from compareTo
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;								//define package


/*********************************************************
 * Compare
 * 
 * @purpose Defines typesafe values for returning from 
 * 		compareTo and methods for accessing those values
*********************************************************/
public enum Compare {
	
	
	/*********************************************************
	 * The following are the values for the enumerator and their
	 * 		constant values 
	*********************************************************/
	equal (0),									//equal value
	less (-1),									//less value
	more (1);									//more value
	
	
	/*********************************************************
	 * The following are the private field of the enumerator
	*********************************************************/
	private final int value;					//sole field
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008043000012L;//object ID
		
	
	/*********************************************************
	 * Constructor
	 *  
	 * @purpose Creates a new instance of the enumerator
	 * 
	 * @param	int value: The value of the enumerator
	*********************************************************/
	Compare(int value){
		this.value = value;						//set enum value
	}
	
	
	/*********************************************************
	 * @purpose Returns the value of the given enumerator
	 * 
	 * @return int: the value of the compare enumerator
	*********************************************************/
	public int value(){
		return value;							//return enum value
	}
}