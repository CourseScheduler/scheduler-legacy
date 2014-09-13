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
 * File: Course.java
 * 
 * Contains classes:
 * 
 * 		Course:
 * 
 * 			Purpose: To store course information for 
 * 				databasing the courses
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;								//define Scheduler package

/********************************************************
 * Import Serializable class to allow for class to be 
 * 		serialized for storage
 * Import ArrayList for holding the Sections
 * Import Iterator for use with the Arraylist 
*********************************************************/
import java.io.Serializable;					//Allow class to be serialized
import java.util.ArrayList;						//Allow use of Array list
import java.util.Iterator;						//For use with Array lists


/********************************************************
 * Course
 * 
 *  To store course information for databasing
 * 			the courses
 * 		
 * @see Cloneable, Serializable, Comparable, Comparable
*********************************************************/
public class Course implements Cloneable, Serializable, Comparable<Course>{
	
	
	/********************************************************
	 * The following are private fields for the Course Instance
	*********************************************************/
	private String courseID;						//courseID field
	private String perceivedCourse;					//courseID used for equality
	private String credit;							//credit of course
	private String title;							//course title
	private ArrayList<Section> sections;			//available sections
	private boolean allClosed;						//if all courses closed
	
	
	/********************************************************
	 * The following are private static constants in the
	 * 		class
	*********************************************************/
		
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008093000070L;//object ID
	protected static final long serialVersionUID = 1L + 
			Version.course.id;//serial id
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Sets Course defaults and creates space for the fields		
	*********************************************************/
	public Course(){
		this.courseID = new String("");					//create new string for course ID
		this.credit = "4";								//set credit to default value of "4"
		this.title = new String("");					//create new string for title
		this.sections = new ArrayList<Section>();		//create new Array list for sections
		this.perceivedCourse = new String("");			//create new string for perceived course
	}
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Creates a new Course with the Section specified
	 * 
	 * @param Section newSection: The section to add to the Course
	*********************************************************/
	public Course(Section newSection){
		this.courseID = new String("");					//create new string for course ID
		this.credit = "4";								//set credit to default value of "4"
		this.title = new String("");					//create new string for title
		this.sections = new ArrayList<Section>();		//create new Array list for sections
		this.perceivedCourse = new String("");			//create new string for perceived course
		this.addSection(newSection);					//add the first section to the course
	}
	
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose Create a new Course based on the specified Course
	 * 
	 * @param Course other: The course to base this course off of
	*********************************************************/	
	public Course(Course other){
		this.setCourseID(new String(other.getCourseID()));	//set course ID from other
		this.setCredit(new String(other.getCredit()));		//set credit from other
		this.setTitle(new String(other.getTitle()));		//set title from other
		this.setSections(other.getSectionsS());				//set section list from other
		this.setPerceivedCourse(new String(other.getPerceivedCourse()));//set perceived course
	}
	
	
	/*********************************************************
	 * @purpose Returna clone of the current instance
	 * 
	 * @return Course: A clone of this course
	*********************************************************/
	@Override
	public Course clone(){
		return new Course(this);						//clone this and return clone
	}
		
	
	/*********************************************************
	 * @purpose Returns this course as a string in the form:
	 * 		courseID (title)
	 * 
	 * @return String: this course as a string
	*********************************************************/
	@Override
	public String toString(){
		return new String(this.perceivedCourse + " (" + this.title + ")");
	}													//return courseID and title as a string
	
	
	/*********************************************************
	 * @purpose Compares this course to another course
	 * 
	 * @param Course other: course to compare to
	 * 
	 * @return int: the result of the comparison
	*********************************************************/
	public int compareTo(Course other){
		return this.courseID.compareToIgnoreCase(
				other.getCourseID());					//compare courses based on courseID
	}
	
	
	/*********************************************************
	 * 
	 * @purpose Set the credit for the course
	 *
	 * @param String credit: the string to set the credit to
	*********************************************************/
	public void setCredit(String credit){
		this.credit = credit;							//set the credit for the course
	}
	
	
	/*********************************************************
	 * @purpose set the courseID object to the specified string object
	 * 
	 * @param String course: the string object to set the course ID to
	*********************************************************/
	protected void setCourseID(String course){
		this.courseID = course;							//set courseID object
	}
	
	
		
	/*********************************************************
	 * @purpose set the title object to the specified string object
	 * 
	 * @param String title: the string object to set the title to
	*********************************************************/
	protected void setTitle(String title){
		this.title = title;								//set title object
	}
	
	
	/*********************************************************
	 * @purpose set the sections to the specified section []
	 * 
	 * @param Section[] sections: the section[] to set for this instance
	*********************************************************/
	public void setSections(Section[] sections){
		for (int item = 0; item < sections.length; item++){//add the sections in the input
			if (sections[item] != null){				//to the Array list of sections if 
				this.addSection(new Section(sections[item]));//the section is not null
			}
		}
	}
	
	
	/*********************************************************
	 * @purpose Returns the reference to this courseID
	 * 
	 * @return String: this instance's courseID
	*********************************************************/
	protected String getCourseID(){
		return this.courseID;							//return the courseID string obj
	}
	
	
		
	/*********************************************************
	 * @purpose Returns this instance's title
	 * 
	 * @return String: the reference to this instance's title
	*********************************************************/
	protected String getTitle(){
		return this.title;								//return the title string object
	}
	
	
	/*********************************************************
	 * @purpose Return this instance's sections
	 * 
	 * @return String[]: The section in this instance
	*********************************************************/
	public String[] getSectionsStr(){
		Iterator<Section> list = this.sections.iterator();//get iterator on list
		String[] result = new String[this.sections.size()];//create result array
		int pos = 0;									//keep track of size
		
		while(list.hasNext()){							//while sections in the list
			result[pos] = new String(list.next().toString());//add as new string to the array
		}
		
		return result;									//return the result array
	}
		
	
	/*********************************************************
	 * @purpose Return this instance's sections
	 * 
	 * @return Section[]: the sections in this instance
	*********************************************************/
	public Section[] getSectionsS(){
		Iterator<Section> list = this.sections.iterator();//create iterator on list
		Section[] result = new Section[this.sections.size()];//create result array
		int pos = 0;									//keep track of size
		
		while(list.hasNext()){							//iterate while sections in list
			result[pos] = new Section(list.next());		//clone the sections, add to array
		}
			
		return result;									//return the result array
	}
	
	
	/*********************************************************
	 * @purpose Returns the sections in this course
	 * 
	 * @return Section[]: returns the reference to the sections
	 * 		in this course
	*********************************************************/
	protected Section[] getSectionsSObj(){
		return this.sections.toArray(new Section[sections.size()]);	//return the Arraylist as a Section[]
	}
	
	
	/*********************************************************
	 * @purpose Returns the sections in this Course
	 * 
	 * @return ArrayList<Section>:  a reference to a Arraylist of
	 * 		the sections in this course
	*********************************************************/
	protected ArrayList<Section> getSectionsLl(){
		return this.sections;							//return the Array listk of sections object
	}
	
	
	/*********************************************************
	 * @purpose Returns if all of the sections in the course are closed
	 * 
	 * @return boolean: If the sections are all closed
	*********************************************************/
	public boolean allClosed(){
		return this.allClosed;							//return if all sections are closed
	}
	
	
	/*********************************************************
	 * @purpose return the credit string
	 * 
	 * @return String: The reference to the credit string
	*********************************************************/
	protected String getCredit(){
		return this.credit;								//return the credit object
	}
	
	
	/*********************************************************
	 * @purpose Add a section to the course
	 * 
	 * @param Section item: the section to add to the course
	 * 
	 * @return boolean: if the section was added to the course
	*********************************************************/
	public boolean addSection(Section item){
		if (this.sections.size() == 0){					//check if no sections in list
			this.setCourseID(item.getCourseID());		//set the course object equal to the 
			this.setCredit(item.getCredit());			//section objects
			this.setTitle(item.getTitle());
			this.setPerceivedCourse(item.getPerceivedCourse());
			this.allClosed = item.isClosed();			
			this.sections.add(item);					//add section to the course
			
			return true;								//return success
		}
		if (item.getPerceivedCourse().compareTo(this.getPerceivedCourse()) == 
				Compare.equal.value() && !this.sections.contains(item)){	//check if correct course and for containment
			
			item.setCourseID(this.getCourseID());
			item.setCredit(this.getCredit());	//set section'n non-independant objects to 
			item.setTitle(this.getTitle());	//the courses objects
			item.setPerceivedCourse(this.getPerceivedCourse());
			this.allClosed = this.allClosed && item.isClosed();
			this.sections.add(item);				//add section to course
			
			return true;							//return success
		}
		return false;							//return failure
	}
	
	
	/********************************************************
	 * @purpose check if the course contains a section
	 * 
	 * @param Section other: the section to check for containment
	 * 
	 * @return boolean: if the course has the section
	*********************************************************/
	public boolean hasSection(Section other){
		Iterator<Section> sections = this.sections.iterator();//get an iterator on the sections
			
		while(sections.hasNext()){						//while more sections
			if (sections.next().compareTo(other) == Compare.equal.value()){//check if equal
				return true;							//if so, hasSection returns true
			}
		}
		return false;									//section not found return false
	}
	
	
	/********************************************************
	 * @purpose Return the perceived courseID
	 * 
	 * @return String: the reference to the perceivedCourseID
	*********************************************************/
	public String getPerceivedCourse(){
		return this.perceivedCourse;					//returns the perceived course object
	}
	
	
	/********************************************************
	 * @purpose Set the perceived course object
	 * 
	 * @param String perCourse: a reference to the perceived 
	 * 			course to use
	*********************************************************/
	public void setPerceivedCourse(String perCourse){
		this.perceivedCourse = perCourse;				//sets the perceived course object
	}
	
	
	/********************************************************
	 * @purpose returns the number of sections in the course
	 * 
	 * @return int: the number of sections in the course
	*********************************************************/
	public int getNumOfSections(){
		return this.sections.size();					//returns the number of sections
	}
	
	
	/********************************************************
	 * @purpose Return the specified section
	 * 
	 * @param int pos: the position in the Arraylist to return
	 * 
	 * @return Section: a clone of the desired section
	*********************************************************/
	public Section getSection(int pos){
			return this.sections.get(pos).clone();			//returns a specific section
	}
	
	
	/********************************************************
	 * @purpose Rerates all of the sections in the course
	*********************************************************/
	public void reRate(){
		Iterator<Section> items = this.sections.iterator();	//get iterator of sections
		
		while(items.hasNext()){
			items.next().reRate();	//for each section, rerate it
		}
	}
	
	
	/********************************************************
	 * @purpose Returns if the course has a section of the specified type
	 * 
	 * @param CourseType type: the type to check against
	 * 
	 * @return boolean: if the course has a section with matching type
	*********************************************************/
	public boolean hasSectionOfType(CourseType type){		
		for(Section item: sections){	//for each section in this course
			if(item.fitsType(type)){	//check if the type fits
				return true;			//and return true if so
			}
		}
		return false;					//if got through the list and get here, then false
	}
}
