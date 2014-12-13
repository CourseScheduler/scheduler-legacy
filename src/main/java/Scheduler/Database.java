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
 * File: Database.java
 * 
 * 		Contains class:
 * 		
 * 			Database: 
 * 
 * 				Purpose: To store Prof and Course information
 * 					for the creation of courses
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;									//declare as a member of scheduler package


/********************************************************
 * Import the necessary classes to support the database
*********************************************************/
import io.devyse.scheduler.analytics.keen.KeenEngine;

import java.io.Serializable;						//import serializable interface
import java.util.Arrays;							//import array util class
import java.util.Calendar;							//import java calendar utility
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;							//import tree map for database
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;					//import the progress bar

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;							//import the arrayList utility


/********************************************************
 * Class: Database
 * 
 * @purpose Provides storage and methods for making class schedules
*********************************************************/
public class Database implements Serializable, Cloneable{

	/**
	 * Static logger
	 */
	private static Logger logger = LoggerFactory.getLogger(Database.class);

	/********************************************************
	 * The following are fields of the Database
	*********************************************************/
	private TreeMap<String, Course> database;		//holds the courses for the selected term
	private String term;							//specifies the database's term
	private Calendar creation;						//specifies when the database was created
	private ProfDatabase profs;						//database of profs and ratings
	private boolean undergrad;						//flag for if the database has undergrad info
	private boolean gradCampus;						//flag for if the database has on campus grad info
	private boolean gradDist;						//flag for if the database has off campus grad info
		
	
	/********************************************************
	 * The following are static constants for use within the package
	*********************************************************/
	protected final static int empty = 0;			//value for empty
	protected final static int beginIndex = 0;		//starting index for loops
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2008112500092L;	//object version
	protected static final long serialVersionUID = 10L 
			+ Version.database.id;//serial ID
		
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Creates a new database and initializes storage
	*********************************************************/
	public Database(boolean hasRatings){
		database = new TreeMap<String, Course>();	//create space for the arraylist database
		term = new String();						//create new string for the term
		creation = Calendar.getInstance();			//create new date at current time
		profs = new ProfDatabase(hasRatings);		//create new tree set for profs
		undergrad = false;							//make sure flags are false to start
		gradCampus = false;							//make sure flags are false to start
		gradDist = false;							//make sure flags are false to start
	}
	
	
	/********************************************************
	 * (Constructor
	 * 
	 * @purpose Creates a database based on another database
	 * 
	 * @param Database other: the database to use fro creating a new database
	*********************************************************/
	public Database(Database other){
		this.setTerm(new String(other.getTerm()));	//set the term
		this.setProfs(other.getProfs());			//set the profs
		this.setCreation(other.getCreation());		//set the creation date
		this.setDatabase(other.getDatabase());		//set the course database
	}
	
	
	/********************************************************
	 * @purpose Adds a section to the database
	 * 
	 * @param Section newSection: the section to be added to the
	 * 		database
	*********************************************************/
	public void addSection(Section newSection){
		int end = database.size();					//get the database's size
		
		if (end == empty){							//check if database is empty
			Course newCourse = new Course(newSection);//if so make new course for section
			database.put(newCourse.getPerceivedCourse(), newCourse);//add to database
		}
		else {			
			Course check = database.get(newSection.getPerceivedCourse());//get course if exists
			
			if(check == null){						//check for existing course
				Course newCourse = new Course(newSection);//if not, make new course
				database.put(newCourse.getPerceivedCourse(), newCourse);//add new course
			}
			else{
				check.addSection(newSection);		//else add to course
			}
		}
		
		//TODO handle section instructor lists
		List<String> instructorList = newSection.getInstructorList();
		ProfDatabase profDB = this.getProfs();
		for(String instructor: instructorList){
			Prof prof = profDB.get(instructor);
			if(prof == null) { 
				prof = new Prof();
				prof.setName(instructor);
				profDB.addIfNew(prof);
			}
			newSection.setInstructor(prof);
		}
		
		setDatabaseFlags(newSection);
	}
	

	/********************************************************
	 * @purpose Sets the database flags 
	 * 
	 * @param Section item: the section whose flags should be 'OR'ed 
	 * 		with the database flags
	*********************************************************/
	protected void setDatabaseFlags(Section item){
		undergrad |= item.fitsType(CourseType.undergrad);
		gradCampus |= item.fitsType(CourseType.campusGrad);
		gradDist |= item.fitsType(CourseType.distanceGrad);
	}
	
	
	/********************************************************
	 * @purpose Sets the database directly
	 * 
	 * @param TreeMap<String, Course> data: the data to set to
	*********************************************************/
	protected void setDatabase(TreeMap<String, Course> data){
		this.database = data;						//set the database
	}
	
	
	/********************************************************
	 * @purpose Gets the database in native data structure
	 * 
	 * @return TreeMap<String, Course>: the data to set to
	*********************************************************/
	protected TreeMap<String, Course> getDatabase(){
		return this.database;						//return the course database
	}
	
	/********************************************************
	 * @purpose Returns if the database is empty
	 * 
	 * @return boolean: if the database is empty
	*********************************************************/
	public boolean isEmpty(){
		return (this.database.isEmpty());			//return if the database is empty
	}	
	
	
	/********************************************************
	 * @purpose Calculates the schedules
	 * 
	 * @return Schedule[]: the possible schedules based on parameters
	*********************************************************/
	public Schedule[] makeSchedulesOpt(String[] classes, ArrayList<String> primary, boolean allowClosed, int useMin, ThreadSynch sync, boolean[][] sectionsAllowed, int[] numberSelected, boolean reportingEnabled){
		int[] permOffset = new int[classes.length];	//combinatorial object offset currently 0, may need to be 1; must test to find out if all combinations are found or not
		
		for(int pos = beginIndex; pos < permOffset.length; pos++){
			permOffset[pos] = (primary.contains(classes[pos])) ? 0 : 1;
		}
				
		ScheduleVector result = new ScheduleVector();//create list for schedules
				
		Course[] possible = new Course[classes.length];//space for possible courses
		
		int permute = 1;							//min number of permutations
		int count = 0;
		
		ProgressMonitor tempMon = sync.getWatch();	//get the watch	
		tempMon.setMillisToDecideToPopup(100);		//set to decide to popup right away
		tempMon.setMillisToPopup(100);				//set to popup right away
		
		sync.setWatch(tempMon);						//set the monitor
		
		sync.updateWatch("Calculating Combinations", 1);//update the monitor
		int pos = beginIndex;
		try{
			for(; pos < classes.length && permute > 0; pos++){
				possible[pos] = this.database.get(classes[pos]);//add the course
				permute *= possible[pos].getNumOfSections() + permOffset[pos];//get permutations
			}
		}
		catch(NullPointerException ex){
			JOptionPane.showMessageDialog(sync.getOwner(), 
				"Unable to create schedules for the selected classes because "  + classes[pos] + " does not exist.\n " +
				"Building schedules without " + classes[pos] + ".", 
				"Unable to Build Schedules", JOptionPane.ERROR_MESSAGE);
			sync.getOwner().scheduleClassModel.removeElement(classes[pos]);
			sync.getOwner().scheduleClassList.setModel(sync.getOwner().scheduleClassModel);
			sync.failed = true;
			return new Schedule[0];
		}
		
		if(permute <= 0){							//check if valid permute
			sync.closeWatch();						//close the watch
			return null;							//return null to thread
		}
		
		sync.setPermute(permute);
		
		boolean plural = false;
		
		for(int value: numberSelected){
			plural |= (value > 1);
		}
	
		int codesPerThread = permute/Main.availProcs;//find maximum number of grey codes per thread based on 1 thread/logical core + master thread 
		
		Main.prefs.setGreyCodeLimit((codesPerThread < 20) ? 20 : codesPerThread);//set the number of grey codes to the optimum value over 20
		sync.getWatch().setMaximum(permute + 1);	//set maximum value for progress bar
		
		CombinationGenerator[] generate = new CombinationGenerator[possible.length];
		
		for(int col = 0; col < generate.length; col++){
			generate[col] = new CombinationGenerator(possible[col].getNumOfSections() + 1, numberSelected[col]);
		}
		
		ArrayList<int[][]> greyCodes = new ArrayList<int[][]>();
		int[][] comb = new int[possible.length][];
		boolean[] roll = new boolean[possible.length];
		boolean[] next = roll.clone();
		
		for(int col = 0; col < generate.length; col++){
			comb[col] = generate[col].getNext().clone();
		}
		boolean toStop = false;
		
		major:
		while(true){
			for(int col = 0; col < generate.length; col++){
				if(roll[col]){
					if(!generate[col].hasMore()){
						if(col == generate.length - 1){
							toStop = true;
						}
						else{
							roll[col + 1] = true;
						}
						generate[col].reset();
					}
					next[col] = false;
					comb[col] = generate[col].getNext().clone();
				}
			}
			
			next[0] = true;
			roll = next.clone();
			
			for(int spot = 0; spot < comb.length; spot++){
				for(int sec = 0; sec < comb[spot].length; sec++){
					try{
						if(!sectionsAllowed[spot][comb[spot][sec]]){
							continue major;
						}
					}
					catch(IndexOutOfBoundsException ex){}
					catch(NullPointerException ex1){}
				}
			}
			
			if(greyCodes.size() == Main.prefs.getGreyCodeLimit() - 1 || toStop){
				
				BuildAssistThread helper = new BuildAssistThread();//create thread
				helper.setGreyCodes(greyCodes);			//set the grey codes
				helper.setAllowClosed(allowClosed);		//allow closed courses
				helper.setPermute(permute);				//set total number of threads
				helper.setResult(result);				//set the result object
				helper.setPossible(possible);			//set the possible courses
				helper.setUseMin(useMin);				//set the min use value
				helper.setSync(sync);					//set the sync object
				helper.setReportingEnabled(reportingEnabled);//set reporting enabled
				helper.setNumberSelected(numberSelected);//set the number of each section selected
				
				sync.addHelper(helper);					//add the helper
				
				Main.threadExec.execute(helper);		//execute the helper
				
				count += greyCodes.size();
				if(count > permute){
					sync.setPermute(count);
				}
				
				greyCodes = new ArrayList<int[][]>();
			}
			else{
				greyCodes.add(comb.clone());
			}
			if(toStop){
				break;
			}
		}
		
		while(sync.hasLivingHelpers()){				//check if threads still alive
			if(sync.isCanceled()){					//check if cancelled
				sync.cancel();						//cancel thread via the sync object
				
				sync.finished = permute;			//set finished to permute
			}
		}
		
		if(sync.isCanceled()){						//check if cancelled
			return null;							//return null to master thread
		}
		
		Schedule[] temp = result.toArray(new Schedule[0]);//get the schedule[]
		Arrays.sort(temp);							//sort it
		
		registerScheduleEvent(allowClosed, useMin, reportingEnabled, classes, primary, sectionsAllowed, numberSelected, sync, temp);
				
		sync.updateWatch("Updating List", -1);		//set new note
		return temp;								//return the results
	}
	
	private void registerScheduleEvent(boolean allowClosed, int useMin, boolean reportingEnabled, String[] classes, List<String> primary, boolean[][] sectionsAllowed, int[] numberSelected, ThreadSynch sync, Schedule[] temp){
		try{
			if(!Main.prefs.isAnalyticsOptOut()){
				Map<String, Object> event = new HashMap<>();
				event.put("university.name", "Kettering University");
				event.put("university.term", this.getTerm());
				event.put("parameters.closed", allowClosed);
				event.put("parameters.use.min", useMin);
				event.put("parameters.use.all", sync.getOwner().useAll.isSelected());
				event.put("parameters.reporting", reportingEnabled);
				event.put("parameters.classes", classes);
				event.put("parameters.primary", primary);
				event.put("results.schedules.found", temp.length);
				event.put("results.schedules.conflicts", sync.getConflicts().size());
				
				buildAllowed(event, classes, sectionsAllowed);
				buildSelected(event, classes, numberSelected);
				buildLinked(event, sync);
				
				KeenEngine.getDefaultKeenEngine().registerEvent(Main.KEEN_SCHEDULE, event);
			}
		}catch(Exception e){
			logger.error("Exception processing schedule event", e);
		}
	}
	
	private void buildSelected(Map<String, Object> event, String[] classes, int[] numberSelected){
		for(int classPos = 0; classPos < classes.length; classPos++){
			event.put("parameters.selected."+classes[classPos],numberSelected[classPos]);
		}
	}
	
	private void buildAllowed(Map<String, Object> event, String[] classes, boolean[][] sectionsAllowed){
		for(int classPos = 0; classPos < classes.length; classPos++){
			Course course = this.getCourse(classes[classPos]);
			Section[] sections = course.getSectionsSObj();
			for(int sectionPos = 0; sectionPos < sections.length; sectionPos++){
				event.put("parameters.allowed."+classes[classPos]+"."+sections[sectionPos].getSection(), sectionsAllowed[classPos][sectionPos]);
			}
		}
	}
	
	private void buildLinked(Map<String, Object> event, ThreadSynch sync){
		event.put("parameters.links", sync.getOwner().dependancy);
	}
	
	
	/********************************************************
	 * @purpose returns the database's term
	 * 
	 * @return the term this database represents
	*********************************************************/
	public String getTerm() {
		return term;								//return the term
	}

	
	/********************************************************
	 * @purpose Set the database's term to the specified string
	 * 
	 * @param String term: the string to set as the Term
	*********************************************************/
	public void setTerm(String term) {
		this.term = term;							//set the term
	}

	
	/********************************************************
	 * @purpose Return the date of creation
	 * 
	 * @return Calendar: the date of creation of the database
	*********************************************************/
	public Calendar getCreation() {
		return creation;							//return the date
	}

	
	/********************************************************
	 * @purpose Sets the creation date of the database
	 * 
	 * @param Calendar creation: the date to set the creation to
	*********************************************************/
	public void setCreation(Calendar creation) {
		this.creation = creation;					//set the date
	}
	
	
	/********************************************************
	 * @purpuse Return a string representation of the database
	 * 
	 * @return String: the database represented as a string in form
	 * 		of Semester Year eg. Summer 2008
	*********************************************************/
	@Override
	public String toString(){
		int value = Integer.parseInt(this.term);	//parse the term value
		int year = value / (100);					//extract the year
		int term = value - (year * 100);			//extract the term
		
		return new String(Term.getTerm(term).toString() + " " +
				Integer.toString(year));			//return the string
	}

	
	/********************************************************
	 * @purpose Saves the database to a file name based on term
	 * 		 and return if successful
	 * 
	 * @return booleam: if the save was successful
	*********************************************************/
	public boolean save(){
		return Serial.save(Main.dataFolder + this.getTerm() + Main.databaseExt, this);//return the result of saving the database
	}
	
	
	/********************************************************
	 * @purpose Loads the database from a file name 
	 * 
	 * @return Database: the database deserialized from the file
	*********************************************************/
	public static Database load(String term){
		return Serial.load(Main.dataFolder + term + Main.databaseExt);//return the loaded database
	}
	
	
	/********************************************************
	 * @purpose Forces the database to rerate all sections
	*********************************************************/
	public void reRate(){		
		for(String key: this.database.keySet()){	//for each key
			this.database.get(key).reRate();		//get the course and rerate
		}
	}

	
	/********************************************************
	 * @purpose Return if the database should redownload from banner
	 * 		based on the creation date
	 * 
	 * @return boolean: if the database should update
	*********************************************************/
	public boolean shouldUpdate(){
		Calendar today = Calendar.getInstance();	//get current date
		
		if (today.after(this.creation)){			//check agains creation date
			int currentDay = today.get(Calendar.DAY_OF_YEAR);
			int createdDay = this.creation.get(Calendar.DAY_OF_YEAR);
			
			createdDay = (createdDay - currentDay > 0) ? (367 - createdDay) : createdDay;//get difference
			
			if (Math.abs(currentDay - createdDay) > Main.prefs.getUpdateMin()){
				return true;						//if older than preferred update day
			}										
		}
		
		return false;								//else return false
	}


	/********************************************************
	 * @purpose Return the Prof database
	 * 
	 * @return TreeMap<String, Prof>: The treeMap containing the profs
	 * 		and their rating info
	*********************************************************/
	public ProfDatabase getProfs() {
		return profs;								//return profs
	}


	/********************************************************
	 * @purpose Set the prof rating database
	 * 
	 * @param TreeMap<String, Prof> profs: the prof rating database
	*********************************************************/
	public void setProfs(ProfDatabase profs) {
		this.profs = profs;							//set profs
	}
	
	
	/********************************************************
	 * @purpose Return a list of the courses available in the database
	 * 
	 * @return String[]: the course titles available
	*********************************************************/
	public String[] getCourseList(CourseType type){
		Vector<String> toReturn = new Vector<String>();
		
		for(String item: database.keySet().toArray(new String[1])){
			if(database.get(item).hasSectionOfType(type)){
				toReturn.add(item);
			}
		}
		
		return toReturn.toArray(new String[1]);
	}
	
	
	/********************************************************
	 * @purpose Return the course referenced by the key
	 * 
	 * @return Course: the course mapped to by the key
	*********************************************************/
	public Course getCourse(String key){
		return database.get(key);					//return the requested course
	}
	
	
	/********************************************************
	 * @purpose Return a database clone
	 * 
	 * @return a clone of the database
	*********************************************************/
	@Override
	public Database clone(){
		return new Database(this);					//return a clone of this database
	}
	
	
	/********************************************************
	 * @purpose 
	*********************************************************/
	public boolean isNewerThan(Database other){
		return this.creation.compareTo(other.creation) == Compare.more.value();
	}


	public boolean isUndergrad() {
		return undergrad;
	}


	public void setUndergrad(boolean undergrad) {
		this.undergrad = undergrad;
	}


	public boolean isGradCampus() {
		return gradCampus;
	}


	public void setGradCampus(boolean gradCampus) {
		this.gradCampus = gradCampus;
	}


	public boolean isGradDist() {
		return gradDist;
	}


	public void setGradDist(boolean gradDist) {
		this.gradDist = gradDist;
	}
}
