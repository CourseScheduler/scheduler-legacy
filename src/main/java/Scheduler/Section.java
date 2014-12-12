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
 * File: Section.java
 * 
 * Contains classes:
 * 
 * 		Section:
 * 
 * 			Purpose: To store section information for 
 * 				databasing the courses
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;									//define the package for this class


/********************************************************
 * Import Serializable class to allow for class to be 
 * 		serialized for storage 
 * Import LinkedList class for storage of section corequisites
 * Import Iterator class for walking through LinkedList with ease
*********************************************************/
import java.io.Serializable;						//import Serializable class
import java.util.ArrayList;
import java.util.List;


/********************************************************
 * Class: Section
 * 
 * 		@purpose To store Section information for databasing
 * 			the Sections
 * 		
 * 		@see Cloneable, Serializable, Comparable
*********************************************************/
public class Section implements Cloneable, Serializable, Comparable<Section>{
							//implements clone, compareTo, serialize, and deserialize methods


	private static final String[] invalidTimes = new String[]{"TBA", "", " ", "12:00-12:00am"};
	private static ArrayList<String> invalid;	{
		invalid = new ArrayList<String>();
		
		for(String item: invalidTimes){
			invalid.add(item);
		}
	}
	
	
	/********************************************************
	 * The following are private fields for the Section Instance
	*********************************************************/
	private int crn;								//course request number of section
	private String section;							//section identifier
	private Prof instructor;						//section instructor
	private Period period;							//section meeting period
	private String location;						//section meeting location
	private int seats;								//section open seats
	private boolean isClosed;						//section is closed
	private String courseID;						//course identifier
	private String perceivedCourse;					//perceived course
	private String credit;							//course credit value
	private String title;							//course title
	private String notes;							//course special notes
	private double rating;							//section rating
	CourseType type;								//type of the section
	
	private boolean secondary;						//secondary signal
	private Period secPeriod;						//secondary period
	private String secLocation;						//secondary location
	
	private List<String> instructorList;			//list of instructor names, used to retrieve the actual instructor
	
	
	/********************************************************
	 * The following are public static constants in the Section
	 * 		class
	*********************************************************/	
	

	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2013010900044L;//serial ID
	protected static final long serialVersionUID =1L + 
			Version.section.id;//serial ID
	
	
	/********************************************************
	 * @purpose Sets Section defaults and creates space for the fields		
	*********************************************************/
	public Section(){
		crn = 0;										//set the default values for a new section
		courseID = new String("");						//to the logical vaues, 0 for crn, empty
		section = new String("");						//strings for the courseID and section id.
		credit = "4";									//Most courses are 4 credit, the title and
		title = new String("");							//instructor should be empty, the days by
		instructor = new Prof();						//are false, the time Period is also 
		period = new Period();							//there are no open seats, the section is 
		location = new String("");						//closed
		perceivedCourse = new String("");
		seats = 0;
		isClosed = true;
		notes = new String();
		rating = 0;
		
		secondary = false;
		secPeriod = new Period();
		secLocation = new String();
		
		instructorList = new ArrayList<>();
	}
	
	
	/*********************************************************
	 * @purpose Create another Section that is the same as this
	 * 		Section
	 * 
	 @param Section other: the section to clone
	*********************************************************/
	public Section(Section other){	
		this.setCRN(other.getCRN());						//build a new Section based on the 
		this.setCourseID(new String(other.getCourseID()));
		this.setPeriod(other.getPeriodPer().clone());		//other section by setting the fields
		this.setSection(new String(other.getSection()));	//of the new instance to the fields of
		this.setCredit(other.getCredit());					//the other instance. This uses the set 
		this.setTitle(new String(other.getTitle()));		//and get methods of each item.
		this.setInstructor(new Prof(other.getInstructor()));
		this.setLocation(new String(other.getLocation()));
		this.setSeats(other.getSeats());
		this.setNotes(new String(other.getNotes()));
		this.setRating(other.getRating());
		this.setType(other.getType());
		
		this.setSecondary(other.hasSecondary());
		this.setSecPeriod(other.getSecPeriodPer().clone());
		this.setSecLocation(new String(other.getSecLocation()));
		
		this.setInstructorList(new ArrayList<>(other.getInstructorList()));
	}
		
	
	/*********************************************************
	 * @purpose Clone the instance of Section
	 * 
	 * @return Section: a deep clone of this instance
	*********************************************************/
	@Override
	public Section clone(){
		return new Section(this);						//deep clone this instance and return
	}
		
	
	/*********************************************************
	 * @purpose return this Section as a string
	 * 
	 * @return String: the section as a string
	*********************************************************/
	@Override
	public String toString(){
		return new String(this.perceivedCourse + " " + this.section + 
				" [" + this.instructor + "]");			//return section as a string
	}
	
	
	/**
	 * @return the instructorList
	 */
	public List<String> getInstructorList() {
		return instructorList;
	}

	/**
	 * @param instructorList the instructorList to set
	 */
	public void setInstructorList(List<String> instructorList) {
		this.instructorList = instructorList;
	}
	
	/*********************************************************
	 * @purpose Returns if the section is closed
	 * 
	 * @return boolean: if the section is closed
	*********************************************************/
	public boolean isClosed(){
		return this.isClosed;							//return if closed
	}
	
	
	/*********************************************************
	 * @purpose Sets the section CRN
	 * 
	 * @param int requestNum: the CRN to associate with the section
	*********************************************************/
	public void setCRN(int requestNum){
		this.crn = requestNum;							//set crn
	}
	
	
	/*********************************************************
	 * @purpose Set course ID for the instance
	 * 
	 * @param String courseID: the course ID as a string
	*********************************************************/
	public void setCourseID(String courseID){
		this.courseID = courseID;						//set to courseID
	}
	
	
	/*********************************************************
	 * @purpose Set the section ID for the section
	 * 
	 * @param String section: the section ID as a string
	*********************************************************/
	@SuppressWarnings("fallthrough")
	public void setSection(String section){
		this.section = section;							//set to section
		char end = section.charAt(section.length()-1); 
		
		switch(end){
			case 'L':{}
			case 'D':{}
			case 'W':{}
			case 'C':{this.perceivedCourse = new String(this.courseID + end + ""); break;}
			default:{this.perceivedCourse = new String(this.courseID);}
		}
	}
	
	
	/*********************************************************
	 * @purpose Set the instance's credit value
	 * 
	 * @param int credits: the number of credits for the section
	*********************************************************/
	public void setCredit(String credits){
		this.credit = credits;						//set credit value
	}
	
	
	/*********************************************************
	 * @purpose set the section's title
	 * 
	 * @param String title: the title of the section
	*********************************************************/
	public void setTitle(String title){
		this.title = title;							//set to title
	}
	
	
	/*********************************************************
	 * @purpose Set the section instance's instructor
	 * 
	 * @param Prof prof: the instructor for the course
	*********************************************************/
	public void setInstructor(Prof prof){
		this.instructor = prof;				//set to prof
	}
	
	
	/*********************************************************
	 * @purpose set the days the section uses
	 * 
	 * @param Boolean[] days: a string array where days[monday] is monday
	 * 			etc. and days[monday] is true when the section meets on 
	 * 			Monday
	 * 
	 * @return boolean: if the days were set
	*********************************************************/
	public boolean setDays(boolean[] days){
		if (days.length != Day.friday.value()+1){				//verify that the days array is of valid size
			return false;										//if invalid return false for changes made
		}
		this.period.setDays(days);							//boolean array of days
		
		return true;
	}
	
	
	/*********************************************************
	 * @purpose set the section instance's period
	 * 
	 * @param String period: the period as a string in the from of
	 * 			HH:MM-HH:MMam or HH:MM-HH:MMpm
	 * 
	 * @return boolean: if the period was successfully set
	*********************************************************/
	public boolean setPeriod(String period){
		this.period = new Period();						//get new period
		return this.period.setPeriod(period);			//set to new period based on string
	}
	
	
	/*********************************************************
	 * @purpose set the section instance's period
	 * 
	 * @param Period period: the period to set to
	*********************************************************/
	public void setPeriod(Period period){
		this.period = period;							//set the period
	}
	
	
	/*********************************************************
	 * @purpose Set the location of the section
	 * 
	 * @param String locale: the section location
	*********************************************************/
	public void setLocation(String locale){
		this.location = locale;							//set location for the section to string
	}													//for the location
	
	
	/*********************************************************
	 * @purpose set the number of open seats in the section
	 * 
	 * @param int open: the number of open seats in the section
	 * 
	 * @return boolean: if the int open is valid
	*********************************************************/
	public boolean setSeats(int open){
		if (open >= 0){									//validate the number of open seats as positive
			this.seats = open;							//set number of seats
			this.isClosed = open == 0 ? true : false;	//set the isClosed bool based on the number of seats
			
			return true;								//return valid change
		}
		return false;								//return invalid change
	}
	
	
	/*********************************************************
	 * @purpose Return the course request number
	 * 
	 * @return int: the instance crn
	*********************************************************/
	public int getCRN(){
		return this.crn;								//return the value of the crn
	}
	
	
	/*********************************************************
	 * @purpose Returns the section course ID
	 * 
	 * @return String: the course ID of the section
	*********************************************************/
	public String getCourseID(){
		return this.courseID;				//get the course id
	}
	
	
	/*********************************************************
	 * @purpose Returns the section ID of the current section
	 * 
	 * @return String: the section ID of the instance
	*********************************************************/
	public String getSection(){
		return this.section;			//return section id
	}
	
	
	/*********************************************************
	 * @purpose Returns the section's credit value
	 * 
	 * @return int: the section's credit value
	*********************************************************/	
	public String getCredit(){
		return this.credit;					//return value of credit
	}

	
	/*********************************************************
	 * @purpose Return the section's title
	 * 
	 * @return String: the title of the section
	*********************************************************/
	public String getTitle(){
		return this.title;							//return title
	}
	
	
	/*********************************************************
	 * @purpose return the section's instructor
	 * 
	 * @return Prof: the instructor of the section
	*********************************************************/
	public Prof getInstructor(){
		return this.instructor;						//return instructor
	}
	
	
	/*********************************************************
	 * @purpose Returns the days that the section meets as a 
	 * 		boolean array
	 * 
	 * @return boolean[]: the days that the class meets where
	 * 		days[monday] is true if the section meets on monday
	*********************************************************/
	public boolean[] getDaysBool(){
		return this.period.getDays();				//return clone of days bool array
	}
	
	
	/*********************************************************
	 * @purpose Returns if the section meets on the specified day
	 * 
	 * @param int day: the day to return if meeting
	 * 
	 * @return boolean: if the section meets on that day
	*********************************************************/
	public boolean sectionMeetsOnDay(Day day){
		if (day.value() < Day.monday.value() || day.value() > Day.friday.value()){//return if the section meets on a specific
			return false;								//day is invalid return false
		}
		return this.period.getDays()[day.value()];	//day is valid return if section meets
	}
	
	
	/*********************************************************
	 * @purpose Returns the days that the section meets as a
	 * 		string array where if the class meets in monday then
	 * 		result[Section.monday] = "M" and if not is " "
	 * 		etc
	 * 
	 * @return String[]: Containing which days the section meets
	*********************************************************/
	public String[] getDaysStrArr(){
		String[] result = new String[Day.values().length];//return the days that the section meets
		result[Day.monday.value()] = this.period.getDays()[Day.monday.value()] ? "M" : " ";	//as a string arrawy where " " means the
		result[Day.tuesday.value()] = this.period.getDays()[Day.tuesday.value()] ? "T" : " ";//section does not meet, and a character
		result[Day.wednesday.value()] = this.period.getDays()[Day.wednesday.value()] ? "W" : " ";//representing the day means that the
		result[Day.thursday.value()] = this.period.getDays()[Day.thursday.value()] ? "R" : " ";//section does meet. Returned as a new
		result[Day.friday.value()] = this.period.getDays()[Day.friday.value()] ? "F" : " ";	//string array
		
		return result;
	}
	
	
	/*********************************************************
	 * @purpose Returns the days that the section meets as a 
	 * 		single string. Example for monday, thursday, friday
	 * 		returns "M  RF".
	 * 
	 * @return String: The days of the week that the section meets
	*********************************************************/
	public String getDaysStr(){
		String[] temp = getDaysStrArr();				//return the days the section meets as
		String res = new String();						//a single string by concat-ing the
		res = temp[Day.monday.value()] + temp[Day.tuesday.value()] +//chars that represent the days 
			temp[Day.wednesday.value()] + temp[Day.thursday.value()] + 
			temp[Day.friday.value()];					//together
		return res;
	}
	
	
	/*********************************************************
	 * @purpose Return a the section's period
	 * 
	 * @return Period: This section's period
	*********************************************************/
	public Period getPeriodPer(){						//returns the section's period
		return this.period;
	}
	
	
	/*********************************************************
	 * @purpose Return the period as a string
	 * 
	 * @return String: the period as a string
	*********************************************************/
	public String getPeriodStr(){
		return this.period.toString();					//returns the period's string
	}													//representation
	
	
	/*********************************************************
	 * @purpose Return the location of the section
	 * 
	 * @return String: The location of the section
	*********************************************************/
	public String getLocation(){
		return this.location;							//return the section's location
	}
	
	
	/*********************************************************
	 * @purpose Returns the number of open seats in a section
	 * 
	 * @return int: The number of open seats
	*********************************************************/
	public int getSeats(){
		return this.seats;								//return the number of open seats
	}
	
	
	/*********************************************************
	 * @purpose Returns the number of open seats in a section
	 * 
	 * @return String: The number of open seats
	*********************************************************/
	public String getSeatsStr(){
		return (this.seats == 0) ? "Closed" : Integer.toString(this.seats);								//return the number of open seats
	}											//return the number of open seats as a string
	
	
	/*********************************************************
	 * @purpose Returns the comparison between this section 
	 * 		and the item based first on course id and then second
	 * 		on section id.
	 * 
	 * @param Section item: the sectionto compare this item to
	 * 
	 * @return int: the result of the comparison, LESS, EQUAL, MORE
	*********************************************************/
	public int compareTo(Section item){
		int courseComp = this.getCourseID().compareTo(item.getCourseID());
		int sectionComp = this.getSection().compareTo(item.getSection());
		return (courseComp == Compare.equal.value()) ? sectionComp : courseComp;
	}		//compare Sections by course ID then by section id
		
	
	/*********************************************************
	 * @purpose Sets the special section notes
	 * 
	 * @param String notes: The special notes for the section
	*********************************************************/
	public void setNotes(String notes){
		this.notes = notes;					//setnotes
	}
	
	
	/*********************************************************
	 * @purpose Returns the special notes for the section
	 * 
	 * @return String: the special notes
	*********************************************************/
	public String getNotes(){
		return this.notes;					//return the notes
	}
	
	
	/*********************************************************
	 * @purpose Return the perceived course ID
	 * 
	 * @return String: The perceived courseID
	*********************************************************/
	public String getPerceivedCourse(){
		return this.perceivedCourse;		//return the perceived course id
	}
	
	
	/*********************************************************
	 * @purpose Set the perceived course id
	 * 
	 * @param String perceived course: The perceived courseID
	*********************************************************/
	public void setPerceivedCourse(String perceivedCourse) {
		this.perceivedCourse = perceivedCourse;
	}


	/*********************************************************
	 * @purpose Determine if one section conflicts with another
	 * 		on a time basis
	 * 
	 * @param Section other: the section to compare this section to
	 * 
	 * @return boolean: if the sections conflict
	*********************************************************/
	public boolean conflictsWith(Section other){
		boolean conflict = false;						//boolean for if conflict
		
		if(this.hasSecondary() && other.hasSecondary()){//if secondary sessions
			conflict |= this.getSecPeriodPer().conflictsWith(other.getSecPeriodPer());//set conflict
		}
		if(this.hasSecondary()){						//for secondary vs regular
			conflict |= this.getSecPeriodPer().conflictsWith(other.getPeriodPer());//set conflict
		}
		if(other.hasSecondary()){						//for regular vs secondary
			conflict |= this.getPeriodPer().conflictsWith(other.getSecPeriodPer());//set conflict
		}
														//for conflict regular vs regular || other
		return conflict || this.getPeriodPer().conflictsWith(other.getPeriodPer()); //return period confliction
	}

	
	/*********************************************************
	 * @purpose returns the rating for the section
	 * 
	 * @return double: the course rating
	*********************************************************/
	public double getRating() {
		return this.rating;							//return value of the rating
	}

	
	/*********************************************************
	 * @purpose sets the section rating to the specified value
	 * 
	 * @param double rating: the double to set this sections rating to
	*********************************************************/
	public void setRating(double rating) {
		this.rating = rating;						//sets the value of the rating
	}
	
	
	/********************************************************
	 * @purpose rerates the section
	*********************************************************/
	public void reRate(){
		Rate.reRate(this);							//rerate the section
	}
	
	/********************************************************
	 * @purpose compares two sections via their rating
	 * 
	 * @param Section other: the section to compare to this section
	 * 
	 * @return int: the comparison as Compare.(result).value()
	*********************************************************/
	public int compareRating(Section other){
		return (this.rating == other.rating) ? Compare.equal.value() :
			((this.rating < other.rating) ? Compare.less.value() : Compare.more.value());
	}						//return the comparison based on rating
	
	
	/********************************************************
	 * @purpose compares two sections via their time
	 * 
	 * @param Section other: the section to compare to this section
	 * 
	 * @return int: the comparison as Compare.(result).value()
	*********************************************************/
	public int compareTime(Section other){
		return this.getPeriodPer().getStartTime().compareTo(
					other.getPeriodPer().getStartTime());
	}							//return the comparison based on start time


	/********************************************************
	 * @purpose Returns if the section has a secondary time
	 * 
	 * @return boolean: if the section has a secondary time
	*********************************************************/
	public boolean hasSecondary() {
		return secondary;					//return the time
	}


	/********************************************************
	 * @purpose Sets if the section has a secondary time
	 * 
	 * @param boolean secondary: if the section has a secondary time
	*********************************************************/
	public void setSecondary(boolean secondary) {
		this.secondary = secondary;			//set that the section has a second time
	}


	/********************************************************
	 * @purpose Returns the secondary period
	 * 
	 * @return Period: the secondary period
	*********************************************************/
	public Period getSecPeriodPer() {
		return secPeriod;					//return the period
	}
	
	
	/********************************************************
	 * @purpose Returns the second period as a string
	 * 
	 * @return String: the second period's period string
	*********************************************************/
	public String getSecPeriodStr(){
		return secPeriod.toString();		//return the period string
	}


	/********************************************************
	 * @purpose Sets the secondary period
	 * 
	 * @return Period secPeriod: the period to set to
	*********************************************************/
	public void setSecPeriod(Period secPeriod) {
		this.secPeriod = secPeriod;			//set the period
	}	
	
	
	/********************************************************
	 * @purpose Sets the secondary period to the specified string
	 * 
	 * @param String secPeriod: the string to construct the period from
	 * 
	 * @return boolean: if the operation was a success
	*********************************************************/
	public boolean setSecPeriod(String secPeriod) {
		this.secPeriod = new Period();				//create new period
		return this.secPeriod.setPeriod(secPeriod);	//set period and return if success
	}

	
	/********************************************************
	 * @purpose Sets the Secondary days array for the second period
	 * 
	 * @param boolean[] days: sets the days array for the second period
	 * 
	 * @return boolean: if the operation was a success
	*********************************************************/
	public boolean setSecDays(boolean[] days){
		if (days.length != Day.friday.value()+1){				//verify that the days array is of valid size
			return false;										//if invalid return false for changes made
		}
		this.secPeriod.setDays(days);						//boolean array of days
		
		return true;
	}

	
	/********************************************************
	 * @purpose Returns the location for the second period
	 * 
	 * @return String: the location for the second period
	*********************************************************/
	public String getSecLocation() {
		return secLocation;						//return location
	}


	/********************************************************
	 * @purpose Sets the secondary period's locations
	 * 
	 * @param String secLocation: the location to set it to
	*********************************************************/
	public void setSecLocation(String secLocation) {
		this.secLocation = secLocation;			//set location
	}
	
	
	/********************************************************
	 * @purpose Returns the days for the secondary period
	 * 
	 * @param boolean[]: the days for the secoindary period
	*********************************************************/
	public boolean[] getSecDaysBool(){
		return this.secPeriod.getDays();		//return days
	}
	
	
	/*********************************************************
	 * @purpose Returns the days that the section meets as a 
	 * 		single string. Example for monday, thursday, friday
	 * 		returns "M  RF".
	 * 
	 * @return String: The days of the week that the section meets
	*********************************************************/
	public String getSecDaysStr(){
		String[] temp = getSecDaysStrArr();				//return the days the section meets as
		String res = new String();						//a single string by concat-ing the
		res = temp[Day.monday.value()] + temp[Day.tuesday.value()] +//chars that represent the days 
			temp[Day.wednesday.value()] + temp[Day.thursday.value()] + 
			temp[Day.friday.value()];					//together
		return res;
	}
	
	
	/*********************************************************
	 * @purpose Returns the days that the section meets as a
	 * 		string array where if the class meets in monday then
	 * 		result[Section.monday] = "M" and if not is " "
	 * 		etc
	 * 
	 * @return String[]: Containing which days the section meets
	*********************************************************/
	public String[] getSecDaysStrArr(){
		String[] result = new String[Day.values().length];//return the days that the section meets
		result[Day.monday.value()] = this.secPeriod.getDays()[Day.monday.value()] ? "M" : " ";	//as a string arrawy where " " means the
		result[Day.tuesday.value()] = this.secPeriod.getDays()[Day.tuesday.value()] ? "T" : " ";//section does not meet, and a character
		result[Day.wednesday.value()] = this.secPeriod.getDays()[Day.wednesday.value()] ? "W" : " ";//representing the day means that the
		result[Day.thursday.value()] = this.secPeriod.getDays()[Day.thursday.value()] ? "R" : " ";//section does meet. Returned as a new
		result[Day.friday.value()] = this.secPeriod.getDays()[Day.friday.value()] ? "F" : " ";	//string array
		
		return result;
	}
	
	
	public boolean hasValidTime(boolean secondary){
		boolean sec = true;
		if(secondary){
			String secStr = secPeriod.toString();
			sec = !(invalid.contains(secStr));
		}
		String str = period.toString();
		return sec && !(invalid.contains(str)); 
	}


	public CourseType getType() {
		return type;
	}


	public void setType(CourseType type) {
		this.type = type;
	}
	
	public boolean fitsType(CourseType type){
		switch(type){
			case all:{
				return true;
			}
			case campusAndDistance:{
				if(this.type == CourseType.distanceGrad || this.type == CourseType.campusGrad){
					return true;
				}
				break;
			}
			case underAndCampus:{
				if(this.type == CourseType.undergrad || this.type == CourseType.campusGrad){
					return true;
				}
				break;
			}
			case underAndDistance:{
				if(this.type == CourseType.undergrad || this.type == CourseType.distanceGrad){
					return true;
				}
				break;
			}
			default:{
				if(type == this.type){
					return true;
				}
				break;
			}	
		}
		
		return false;
	}
	
	@Override
	public boolean equals(Object o){
		Section other;
		try{
			other = (Section)o;
		}
		catch(ClassCastException ex){
			return false;
		}
		return new String(other.getCourseID() + other.getSection()).equals(getCourseID() + getSection()); 
	}
	
	public String getDescription(){
		return toString() + " (" + getPeriodStr() + ")"; 
	}
	
	public static Section getConflictAsSection(Section one, Section two){
		int startComp = two.getPeriodPer().getStartTime().compareTo(one.getPeriodPer().getStartTime());
		int endComp = two.getPeriodPer().getEndTime().compareTo(one.getPeriodPer().getEndTime());
		String period;
		
		if(startComp == Compare.less.value()){
			period = two.getPeriodPer().getStartTime().toString() + (two.getPeriodPer().getStartTime().getAm() ? " am" : " pm");
		}
		else{
			period = one.getPeriodPer().getStartTime().toString() + (one.getPeriodPer().getStartTime().getAm() ? " am" : " pm");
		}
		
		if(endComp == Compare.more.value()){
			Time end = two.getPeriodPer().getEndTime();
			period += " - " + end.toString() + (end.getAm() ? " am" : " pm");
		}
		else{
			Time end = one.getPeriodPer().getEndTime();
			period += " - " + end.toString() + (end.getAm() ? " am" : " pm");
		}
		
		Section toReturn = new Section();
		toReturn.setPeriod(period);
		toReturn.setSecPeriod("");
		
		boolean[] days = new boolean[Day.values().length];
		
		boolean[] first = one.getDaysBool(), second = two.getDaysBool();
		
		for(Day day: Day.values()){
			days[day.value()] = first[day.value()] && second[day.value()];
		}
		
		toReturn.setDays(days);
		
		return toReturn;
	}
}
