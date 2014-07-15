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
 * File: Term.java
 * 
 * Contains enumerator:
 * 
 * 		Term:
 * 
 * 			Purpose: To store term information for 
 * 				the database class
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;								//define as member of the Scheduler package

import java.util.Calendar;


/*********************************************************
 * Enumerator: Term:
 * 
 *  @purpose  Manages Database term information to typesafe 
 *  	term info
*********************************************************/
public enum Term{
	winter (1, "Winter"),						//winter term
	spring (2, "Spring"),						//spring term
	summer (3, "Summer"),						//summer term
	fall   (4, "Fall");							//fall term
	
	
	/*********************************************************
	 * The following are the fields of the enumerator
	*********************************************************/
	private final int value;					//the ordinal value
	private final String str;					//the term string
	
	
	/*********************************************************
	 * The following are the static globals of the class
	*********************************************************/
	protected final static long versionID = 2008052100005L;	//file version ID
	protected final static long serialVersionUID = 1L + 	//serial version ID
			Version.term.id;
	
	
	/*********************************************************
	 * @purpose Create a new term instance with the default values
	 * 
	 * @param int value: the ordinal value of the Term
	 * @param String str: the Term as a string
	*********************************************************/
	Term(int value, String str){
		this.value = value;						//assign the ordinal value
		this.str = str;							//assign the string
	}
	
	
	/*********************************************************
	 * @purpose return the int value of the Term
	 * 
	 * @return int: the ordinal value of the Term
	*********************************************************/
	public int value(){
		return this.value;						//return the ordinal
	}
	
	
	/*********************************************************
	 * @purpose return the term as a string
	 * 
	 * @return String: this term as a string (cloned)
	*********************************************************/
	@Override
	public String toString(){
		return new String(this.str);			//return the clone of this string
	}
	
	
	/*********************************************************
	 * @purpose return the term as a query string
	 * 
	 * @return String: the term in query form
	*********************************************************/
	public String toQueryStr(){
		return new String("0" + Integer.toString(this.value));
	}											//return the term in query form
	
	
	/*********************************************************
	 * @purpose return the term associated with an integer value
	 * 
	 * @param int val: the value correlating to a Term
	 * 
	 * @return Term: the Term associated with the input value
	*********************************************************/
	public static Term getTerm(int val){
		for(Term each: Term.values()){			//check if the ordinal value matches
			if (val == each.value){				//if so return that term
				return each;
			}
		}
		
		return Term.fall;						//else default to fall
	}
	

	/*********************************************************
	 * @purpose return the term associated with a calendar
	 * 
	 * @param Calendar calendar: the calendar to return the term of
	 * 
	 * @return Term: the next Term after the input value
	*********************************************************/
	public static Term getNextTerm(Calendar calendar){
		Term result;							//create term for result
		int month = calendar.get(Calendar.MONTH);//get the month as int
		
		if (month >= Calendar.FEBRUARY && month <= Calendar.APRIL){
			result = Term.summer;			//if during winter & schedulin
		}									//set to summer (A section)
		else if(month >= Calendar.MAY && month <= Calendar.JULY){
			result = Term.fall;				//if during spring && scheduling
		}									//set to fall (B section)
		else if(month >= Calendar.AUGUST && month <= Calendar.OCTOBER){
			result = Term.winter;			//if during summer && scheduling
		}									//set to winter (A section)
		else{
			result = Term.spring;			//else during fall && scheduling
		}									//set to spring (B section)
		return result;						//return the selected term
	}
	
	
	/*********************************************************
	 * @purpose return the term associated with a calendar
	 * 
	 * @param Calendar calendar: the calendar to return the term of
	 * 
	 * @return Term: the Term associated with the input value
	*********************************************************/
	public static Term getThisTerm(Calendar calendar){
		Term result;							//create term for result
		int month = calendar.get(Calendar.MONTH);//get the month as int
		
		if (month >= Calendar.JANUARY && month <= Calendar.MARCH){
			result = Term.winter;			//if during winter
		}									
		else if(month >= Calendar.APRIL && month <= Calendar.JUNE){
			result = Term.spring;			//if during spring
		}									
		else if(month >= Calendar.JULY && month <= Calendar.SEPTEMBER){
			result = Term.summer;			//if during summer
		}									
		else{
			result = Term.fall;				//else during fall
		}									
		return result;						//return the selected term
	}
	
	
	/*********************************************************
	 * @purpose return the query string as a term string
	 * 
	 * @param String queryForm: the term as a query string
	 * 
	 * @return String: the term in the form "Semester Year" eg "Summer 2008"
	*********************************************************/
	public static String getTermString(String queryForm){
		Term item = Term.getTerm(Integer.parseInt(queryForm.substring(4, 6)));
											//get the semester info and add to the year
		return new String(item.toString() + " " + queryForm.substring(0, 4));
	}
	
	
	/*********************************************************
	 * @purpose return the next term
	 * 
	 * @return String: the next term to register for in query string form
	*********************************************************/
	public static String nextTerm(){
		Calendar calendar = Calendar.getInstance();//get the current date
		int year = calendar.get(Calendar.YEAR);	//get the current year
		Term term;								//new term to store the current term
		
		term = Term.getNextTerm(calendar);		//get the next term for the calendar date
		
		if (term == Term.winter){				//if term is winter
			year++;								//then increment the current year
		}
		
		return new String(Integer.toString(year) + term.toQueryStr());//return term string
	}

	
	/*********************************************************
	 * @purpose return the current term
	 * 
	 * @return String: the current term in query string form
	*********************************************************/
	public static String thisTerm(){
		Calendar calendar = Calendar.getInstance();//get the current date
		int year = calendar.get(Calendar.YEAR);	//get the current year
		Term term;								//new term to store the current term
		
		term = Term.getThisTerm(calendar);		//get the next term for the calendar date
		
		return new String(Integer.toString(year) + term.toQueryStr());//return term string
	}
}