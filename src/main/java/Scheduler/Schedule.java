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
 * File Schedule.java
 * 
 * Contains classes:
 * 		
 * 		Schedule:
 * 
 * 			Purpose: Provides a class for storing course 
 * 				schedules
 * 
 * @author Mike Reinhold
********************************************************/
package Scheduler;						//declare as member of scheduler package


/********************************************************
 * Import ArrayList for storing courses
 * Import Iterator for operations on the array List
 * Import Serializable for saving schedules
********************************************************/
import java.util.ArrayList;				//import arrayList
import java.util.Iterator;				//import iterator
import java.io.Serializable;			//import Serializable


/********************************************************
 * Class: Schedule:
 * 
 * @purpose Stores schedule information as a series of 
 * 		sections
 * 
 * @see Serializable
********************************************************/
public class Schedule implements Serializable, Comparable<Schedule>, Cloneable {
	
	
	/********************************************************
	 * The following are private fields
	********************************************************/
	private ArrayList<Section> classes;				//classes in this schedule
	private double rating;							//the schedule's rating
	private String term;							//the schedule's term
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008102400050L;//object version id
	protected static final long serialVersionUID = 1L +
			Version.schedule.id;//serial id
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Creates a new schedule object
	********************************************************/
	public Schedule(String term){
		classes = new ArrayList<Section>();			//create space for the arraylist
		rating = 0;									//set default rating
		this.term = term;							//set term string
	}
	
	
	@Override
	public Schedule clone(){
		Schedule clone = new Schedule(this.term);
		
		for(Section one: classes){
			clone.add(one, one.isClosed());
		}
		
		return clone;
	}
	
	
	/********************************************************
	 * @purpose Adds a section to the schedule based on if it 
	 * 		conflicts with the schedule and if closed courses
	 * 		are allowed.
	 * 
	 * @param Section section: the section to add to the database
	 * @param boolean allowClosed: if closed courses should be allowed
	 * 
	 * @return boolean: if the course was added to the Schedule
	********************************************************/
	public boolean add(Section section, boolean allowClosed){
		if (classes.isEmpty()){						//check if the classes list is empty,
			if (allowClosed || !section.isClosed()){
				classes.add(section);				//if so add the section and return true
				this.rating = section.getRating();	//set the rating
				return true;
			}
			return false;
		}
		if(allowClosed || !section.isClosed()){//check if section is open or if ignore closed
												//otherwise, get an iterator over the sections
			Iterator<Section> used = classes.iterator();//and iterate through the sections checking
												//if the section to add conflicts with any of 
			while(used.hasNext()){				//the sections in the schedule already
				if (used.next().conflictsWith(section)){//if there is a conflict, return false
					return false;
				}
			}
			
			int num = classes.size();
			classes.add(section);				//otherwise add the section to the schedule
			this.rating = (this.rating * num + section.getRating())/(num + 1);//set rating 
			return true;						//and return true
		}										//at this point section is closed
		return false;							//return false
	}
	
	
	/********************************************************
	 * @purpose Returns the number of sections in the schedule
	 * 
	 * @return int: the number of sections in the int
	*********************************************************/
	public int numberSections(){
		return classes.size();						//return the number of sections
	}
	
	
	/********************************************************
	 * @purpose Returns if one schedule is equal to another,
	 * 		overrides Object's equals method so that it is used
	 * 		in the default array contains method
	 * 
	 * @param Object other: the object to check for equality
	 * 
	 * @return boolean: if the two schedules are the same
	*********************************************************/
	@Override
	public boolean equals(Object other){
		Schedule trick = (Schedule)other;			//cast to schedule
		Iterator<Section> these = this.classes.iterator();//get iterator
		
		while(these.hasNext()){						//while there are more sections to check
			if (!trick.contains(these.next())){		//check for containment
				return false;						//return false since mismatch found
			}
		}
		
		Iterator<Section> oth = trick.getClassesObj().iterator();//get other iterator
		
		while(oth.hasNext()){						//while there are more sectios to check
			if (!this.contains(oth.next())){		//check for containment
				return false;						//return false since mismatch found
			}
		}
		
		return true;								//no mismatches found return true
	}
	
	
	/********************************************************
	 * @purpose Check if the schedule contains the specified section
	 * 
	 * @param Section item: the section to check for containment
	 * 
	 * @return boolean: if the section is in this schedule
	*********************************************************/
	public boolean contains(Section item){
		Iterator<Section> these = this.classes.iterator();//get iterator
		
		while(these.hasNext()){						//while more items to check
			if (these.next().compareTo(item) == Compare.equal.value()){	//check if equal
				return true;						//item found return true
			}
		}
		
		return false;								//item not found return false
	}
	
	
	/********************************************************
	 * @purpose Returns the arraylist which contains the sections
	 * 		that are part of the schedule
	 * 
	 * @return ArrayList<Section>: the sections in the schedule
	*********************************************************/
	public ArrayList<Section> getClassesObj(){
		return this.classes;						//return the array of classes
	}
	
	
	/********************************************************
	 * @purpose rerates the schedule
	*********************************************************/
	public void reRate(){
		Rate.reRate(this);							//reRate the schedule
	}


	/********************************************************
	 * @purpose returns the schedule's rating
	 * 
	 * @return double: the rating
	********************************************************/
	public double getRating() {
		return rating;								//return rating
	}


	/********************************************************
	 * @purpose set the rating to the double passed in
	 * 
	 * @param double rating: the rating to set
	********************************************************/
	public void setRating(double rating) {
		this.rating = rating;						//set this rating
	}


	/********************************************************
	 * @purpose return the term for the schedule
	 * 
	 * @return String: the term that the schedule is for
	********************************************************/
	public String getTerm() {
		return term;								//return the term
	}


	/********************************************************
	 * @purpose set the term the the specified string
	 * 
	 * @param String term: the string to set the term to
	********************************************************/
	public void setTerm(String term) {
		this.term = term;							//set the term
	}
	
	
	/********************************************************
	 * @purpose Compares the two schedules for sorting
	 * 
	 * @param Schedule other: the schedule to compare this against
	 * 
	 * @return int: the value of the comparison
	********************************************************/
	public int compareTo(Schedule other){		
		if(hasClosedCourse() && !other.hasClosedCourse()) return Compare.more.value();
		if(!hasClosedCourse() && other.hasClosedCourse()) return Compare.less.value();
		if(this.rating == other.rating) return Compare.equal.value();//return equality
		if(this.rating > other.rating) return Compare.less.value();//return the other is less than this
		return Compare.more.value();				//return the other is more than this
	}
	
	
	/********************************************************
	 * @purpose return the schedule as a string for display purposes
	 * 
	 * @return String: the string representation of the schedule
	 * 		in the form "Rating: " schedule.rating(rounded to single decimal place
	 * 
	 * @see Override
	********************************************************/
	@Override
	public String toString(){		
		return "Rating: " + (Math.round(this.rating * 10))/10.0;//return the schedule as a string with the rating
	}												//rounded to one decimal place
	
	
	/********************************************************
	 * @purpose return if the schedule has any closed courses
	 * 
	 * @return boolean: if the schedule has any closed sections
	********************************************************/
	public boolean hasClosedCourse(){
		for(Section item: classes){					//for each section in the schedule
			if(item.isClosed()) return true;		//return true if section is closed
		}
		return false;								//no closed items
	}
	
	
	/********************************************************
	 * @purpose return if the schedule contains the specified perceived course
	 * 
	 * @param String key: the perceived course to check for containment
	 * 
	 * @return boolean: if the schedule contains the course
	********************************************************/
	public boolean contains(String key){
		for(Section item: classes){					//for each section in the list
			if(item.getPerceivedCourse().compareTo(key) == Compare.equal.value()){
				return true;						//if found returns true
			}
		}
		return false;								//else return false
	}
	

	/********************************************************
	 * @purpose return if the schedule contains all the linked 
	 * 		courses based on the LinkedCourses dependancy
	 * 
	 * @param LinkedCourses links: the dependancy lists
	 * 
	 * @return boolean: if the schedule meets the dependancy
	********************************************************/
	public boolean hasAllLinks(LinkedCourses links){
		
		for(Section course: this.classes){
			for(CourseList dependancy: links.links){
				if(dependancy.contains(course.getPerceivedCourse())){
					for(String item: dependancy){
						if(!contains(item)){
							return false;
						}
					}
				}
			}
		}
		
		return true;										
	}
	


	/********************************************************
	 * @purpose return if the schedule contains all the primary courses
	 * 
	 * @param ArrayList<String> primary: the primary courses
	 * 
	 * @return boolean: if the schedule has all the primary courses
	********************************************************/
	public boolean allPrimaryUsed(ArrayList<String> primary){
		boolean returnVal = true;
		
		for(String key: primary){
			returnVal &= contains(key);
		}
		
		return returnVal;
	}
	
	public ArrayList<String> missingPrimary(ArrayList<String> primary){
		ArrayList<String> missing = new ArrayList<String>();
		
		for(String key: primary){
			if(!contains(key)){
				missing.add(key);
			}
		}
		
		return missing;
	}
	
	
	/********************************************************
	 * @purpose return if the schedule contains all correct section type
	 * 
	 * @param CourseType type: the schedule build type
	 * 
	 * @return boolean: if the schedule sections are all the appropriate type
	********************************************************/
	public boolean allFitTypes(CourseType type){
		for(Section item: classes){
			if(!item.fitsType(type)){
				return false;
			}
		}
		return true;
	}
	
	
	/********************************************************
	 * @purpose return if the schedule contains the proper number of sections for each course
	 * 
	 * @param int[] numberSelected: the number of sections for each course
	 * @param Course[] possible: the courses possible to add to the schedule
	 * 
	 * @return boolean: if the correct number of sections for each course is in the schedule
	********************************************************/
	public boolean hasAllSections(int[] numberSelected, Course[] possible){
		
		//rewrite this so that it returns true if for each course, the schedule contains the number of selected
		//sections or none at all
		
		for(int pos = 0; pos < possible.length; pos++){
			int added = 0;
			
			Course curr = possible[pos];
			
			if(contains(curr)){
				for(int sec = 0; sec < curr.getNumOfSections(); sec++){
					if(classes.contains(curr.getSection(sec))){
						added++;
					}
				}
				
				if(added < numberSelected[pos]){
					return false;
				}
			}			
		}
		return true;
	}
	
	
	public boolean contains(Course item){
		for(Section one: classes){
			if(one.getPerceivedCourse().equals(item.getPerceivedCourse())) return true;
		}
		return false;
	}
	
	public Section findConflictingSection(Section notUsed){
		for(Section in: classes){
			if(in.conflictsWith(notUsed)){
				return in;
			}
		}
		
		return null;
	}
}
