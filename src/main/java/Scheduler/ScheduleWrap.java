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
 * File: ScheduleWrap.java
 * 
 * Contains class:
 * 
 * 		ScheduleWrap:
 * 
 * 			Purpose: To provide a wrapper for a sthe data for building a schedule
 * 				for serialization purposes with the necessary fields
 * 				to properly store the information
 * 
 * @author Mike Reinhold
********************************************************/
package Scheduler;									//mark as member of scheduler package


/*********************************************************
 * Import the necessary resources for this class
********************************************************/
import java.io.Serializable;						//to make serializeable
import java.util.ArrayList;
import java.util.Calendar;							//to save dates
import java.util.TreeMap;


/*********************************************************
 * Class ScheduleWrap
 * 
 * @purpose Wraps the data necessary for rebuilding the schedules
 * 			 with necessary data for saving
 * 
 * @see Serializeable
********************************************************/
public class ScheduleWrap implements Serializable {
	protected static final long versionID = 2009021600010L;	//object version
	protected static final long serialVersionUID = 2L +
			Version.scheduleWrap.id;				//serial version
	
	protected String[] courses;						//the courses in the schedule
	protected Database data;						//the database used to build the schedule
	protected Calendar date;						//date of creation of the schedule
	protected boolean allowClosed;					//allow closed courses
	protected boolean useAll;						//use all specified courses
	protected boolean autoRefresh;					//if the schedule auto refreshes
	protected int toUse;							//number to use if not specified
	protected ArrayList<String> primary;			//the primary course list
	protected LinkedCourses dependancy;				//the dependancy object
	protected TreeMap<String, boolean[]> allowed;	//the allowed sections for building this schedule
	protected TreeMap<String, Integer> numberSelections;//the number of sections of each course
	protected boolean findConflicts;				//for error reporting
	
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose Constructs a schedule data wrapper
	 * 
	 * @param String[] courses: the courses in the schedule
	 * @param ArrayList<String> primary: the primary courses
	 * @param Database data: the database used to build the schedule
	 * @param boolean allowClosed: if closed courses are allowed
	 * @param boolean useAll: if all the specified courses should be used
	 * @param int toUse: the number of courses to use if not useAll
	********************************************************/
	public ScheduleWrap(String[] courses, ArrayList<String> primary, Database data, boolean allowClosed,
			boolean useAll, boolean autoRefresh, int toUse, LinkedCourses dependancy, TreeMap<String, 
			boolean[]> allowed, TreeMap<String, Integer> numberSelections, boolean findConflicts){
		
		this.courses = courses;						//set the course information
		this.primary = primary;						//sets the primary course list
		this.data = data;							//sets the database information
		this.date = Calendar.getInstance();			//gets the creation instance
		this.allowClosed = allowClosed;				//set if closed courses are allowed
		this.useAll = useAll;						//set if all courses are used
		this.toUse = toUse;							//set number to use
		this.dependancy = dependancy;				//set the dependancy object
		this.autoRefresh = autoRefresh;				//set the auto refresh
		this.allowed = allowed;						//set the allowed section map
		this.numberSelections = numberSelections;	//set the number of sections
		this.findConflicts = findConflicts;			//set if conflicts should be found
	}
	

	/********************************************************
	 * @purpose Saves the schedule wrapper to a file
	 * 		 and return if successful
	 * 
	 * @return boolean: if the save was successful
	*********************************************************/
	public boolean save(String fileName){
		return Serial.save(fileName, this);			//serialize the schedules
	}
	
	
	/********************************************************
	 * @purpose Loads the schedule wrapper from a file name 
	 * 
	 * @return ScheduleWrap: the schedules deserialized from the file
	*********************************************************/
	public static ScheduleWrap load(String fileName){
		return Serial.load(fileName);				//deserialize the schedules
	}


	public TreeMap<String, Integer> getNumberSelections() {
		return numberSelections;
	}


	public void setNumberSelections(TreeMap<String, Integer> numberSelections) {
		this.numberSelections = numberSelections;
	}
}
