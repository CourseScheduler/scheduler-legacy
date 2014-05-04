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
 * File: Parser.java
 * 
 * Contains classes:
 * 
 * 		Parser:
 * 
 * 			Purpose: To parse course information for 
 * 				databasing the courses
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;								//define as member of Scheduler package


/********************************************************
 * Import IOException class for handleing the exceptions
 * 		possibly thrown by the input stream
 * Import InputStream class for interfacing with the 
 * 		ClientHttpRequest class
 * Import Scanner for parsing the Input Stream
 * Import TreeSet for returning professors
 * Import URL class for connecting to web sites
 * Import Progress Monitor generic for monitoring downloads
 * Import JOption Pane for gui messages
*********************************************************/
import java.io.IOException;						//import IOExceptions
import java.io.File;							//import File class
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;						//import scanner
import javax.swing.JOptionPane;					//Import message pane

import com.pollicitus.scheduler.retrieval.BannerDynamicCourseRetrieval;


/********************************************************
 * Class: Course
 * 
 * 		@purpose To parse course information for databasing
 * 			the courses
*********************************************************/
public enum Parser {
	
	
	/********************************************************
	 * The following are the enumerators for the parser codes
	********************************************************/
	ku (Main.prefs.getSID());					//enumerator construction
	
	
	/********************************************************
	 * The following are the fields of the enumerators 
	********************************************************/
	protected final String rmpSID;					//rate my professor id
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected static final long versionID = 2013010100070L;//object ID
		
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose To instantiate the parser enumerator
	 * 
	 * @param String sid: the rate my professor school id
	********************************************************/
	Parser(String sid){
		this.rmpSID = sid;						//set school id
	}
	
	
	/********************************************************
	 * @purpose Returns the school id number
	 * 
	 * @return String: the rate my professor school id
	********************************************************/
	public String getSID(){
		return new String(this.rmpSID);			//return school id
	}
	
	
	/********************************************************
	 * The following are private static constants in the
	 * 		class for parsing KU courses
	*********************************************************/
	private final static String tdClass = "<TD CLASS=\"dddefault\">";//used to help parse
	private final static String tdNoWrap = "<TD NOWRAP CLASS=\"dddefault\">";//ditto above
	private final static String termStr = "validterm";	//part of post structure
	private final static String subjCode = "subjcode";	//part of post structure
	private final static String onlyOpen = "openclasses";//part of post structure
	private final static String allSubj = "%";			//subjCode value to post
	private final static String allowClosed = "N";		//onlyOpen value to post
	private final static int beginIndex = 0;			//used for substrings
	private final static int endIndex1 = 22;			//used for substrings
	private final static int endIndex2 = 29;			//used for substring
	private final static String slashSmall = "</SMALL>";//used for parsing
	private final static String small = "<SMALL>";		//used for parsing
	private final static String redFont = "<br><font color=#ff0000>";//used for parsing
	private final static String slashFont = "</font>";	//used for parsing
	private final static String emptyTag = "&nbsp";		//used for determining section days
	
	private final static int crn = 0;					//ku position of the crn
	private final static int section = 1;				//ku position of course id and section id
	private final static int credit = 2;				//ku position of credit
	private final static int title = 3;					//ku position of title and notes
	private final static int prof = 4;					//ku position of instructor
	private final static int mon = 5;					//ku position of monday bool
	private final static int tue = 6;					//ku position of tuesday bool
	private final static int wed = 7;					//ku position of wednesday bool
	private final static int thu = 8;					//ku position of thursday bool
	private final static int fri = 9;					//ku position of friday bool
	private final static int per = 10;					//ku position of time period
	private final static int loc = 11;					//ku position of location
	private final static int seats = 12;				//ku position of open seats
	private final static int bldg = 0;					//ku grad loc position of building
	private final static int reset = -1;				//reset value for pre incremented indexes
	
	
	/********************************************************
	 * @purpose Parses KU courses and returns a new database
	 * 
	 * @throws IOException
	 * 
	 * @return Database: the database of kettering courses
	*********************************************************/
	private static Database parseKUCourses(String term, String url, ThreadSynch sync)
			throws IOException{
		boolean downloadRatings = Main.prefs.isRateMyProfessorEnabled() && Main.prefs.isRatingsEnabled();
		Database items = new Database(downloadRatings);//create new database
				
		if (downloadRatings){		//check if using rate my professor ratings
			items.setProfs(Parser.parseRateMyProf(Parser.ku, sync)); //get ratings
		}
		
		if(sync.isCanceled()){							//check if the operation is cancelled
			return null;								//if so, return invalid value
		}
		
		parseNew(items, sync, url, term, false, true);
		if(sync.isCanceled()){
			return null;
		}
		
		sync.updateWatch(null, sync.getWatch().getMaximum());//set progress finished
		items.setTerm(term);							//set database term
		
		return items;									//return database
	}
	
	/**
	 * TODO Describe this method
	 *
	 * @param items
	 * @param sync
	 * @param url
	 * @param term
	 * @param grad
	 * @param campus
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void parseNew(Database items, ThreadSynch sync, String url, String term, boolean grad, boolean campus) throws MalformedURLException, IOException{
		sync.updateWatch("Download Course Information from Banner", sync.finished + 1);//set the course info note
		BannerDynamicCourseRetrieval bdcr = new BannerDynamicCourseRetrieval();
		
		//register progress monitor
		bdcr.addObserver(sync);
		
		Collection<List<String>> courseQueues = bdcr.retrieveCourses(url, term);
		int queueID = 0;
				
		//process each course
		for(List<String> dataSet: courseQueues){
			try{
				Section section = translateCourseData(dataSet, queueID);
				sync.updateWatch("Process Course Information: " + section.getCourseID() + " " + section.getSection(), sync.finished++);//set the course info note
				items.addSection(section);
			}catch(Exception e){
				System.err.println("Error processing course data queue " + queueID);
				e.printStackTrace();
			}
			queueID++;
		}
	}
	
	/**
	 * TODO Describe this method
	 *
	 * @param dataSet
	 * @param queueID
	 * @return
	 */
	private static Section translateCourseData(List<String> dataSet, int queueID){
		Iterator<String> entryIterator = dataSet.iterator();
		int position = 0;
		String title = null;
		Integer crn = null;
		String courseId = null;
		String sectionId = null;
		String notes = null;	
		String credits = null;
		List<String> periodList = new ArrayList<String>();
		List<String> locationList = new ArrayList<String>();
		List<boolean[]> daysList = new ArrayList<boolean[]>();
		List<List<String>> instructorList = new ArrayList<List<String>>();
		List<String> dateList = new ArrayList<String>();
		List<String> scheduleTypeList = new ArrayList<String>();
		CourseType courseType = null;
		int seats = 0;
		boolean hasMeetingTimes = false;
		boolean graduate = false;
		
		//process each data line
		while(entryIterator.hasNext()){
			String entry = entryIterator.next();
			
			//TODO remove the temporary tracing output
			System.out.println("#" + queueID + ":" + position + ": " + entry);
			
			Scanner entryScanner = new Scanner(entry);
			
			switch(position){
				case 4:{	//Course title, CRN, Course ID, Section ID
					entryScanner.useDelimiter(" - ");
					title = entryScanner.next();
					
					while(!entryScanner.hasNextInt() && entryScanner.hasNext()){
						title += " - " + entryScanner.next();
					}
					crn = entryScanner.nextInt();
					courseId = entryScanner.next();
					sectionId = entryScanner.next();
					
					//TODO remove temporary tracing output
					System.out.println("Title: " + title);
					System.out.println("CRN: " + crn);
					System.out.println("Course ID: " + courseId);
					System.out.println("Section ID: " + sectionId);
					
					break;
				}
				case 10:{	//Special Notes
					if(entryScanner.hasNext()){
						notes = entryScanner.nextLine();
						entryIterator.next();
						entryIterator.next();
					}else{
						notes = new String();
					}
					
					//TODO remove temporary tracing output
					System.out.println("Notes: " + notes);
					
					break;
				}
				case 26:{	//Degree levels
					entryScanner.useDelimiter(", ");
					
					while(entryScanner.hasNext()){
						if(entryScanner.next().compareTo("Graduate") == 0){
							graduate = true;
							break;
						}
					}
					
					//TODO remove temporary tracing output
					System.out.println("Graduate Course: " + graduate);
					
					break;
				}
				case 32:{	//Lecture Type
					
					//TODO
					
					break;
				}
				case 34:{	//Credits
					credits = entryScanner.next();

					//TODO remove temporary tracing output
					System.out.println("Credits: " + credits);
					
					break;
				}
				case 54:{	//Meeting Times Exist indicator
					hasMeetingTimes = (entry.compareTo("") != 0);
					
					break;
				}
				case 90:{	//Meeting Time Table
					if(hasMeetingTimes){
						int meetingIndex = 0;
						int meetingPosition = 0;
						boolean tableEnd = false;
						
						while(entryIterator.hasNext() && !tableEnd){
							entry = entryIterator.next();
							entryScanner = new Scanner(entry);
							
							//TODO remove tracing output
							System.out.println("#" + queueID + ":" + position + ":" + meetingIndex + ":" + meetingPosition + ": " + entry);
							
							switch(meetingPosition){
								case 0:{	//Start of meeting time
									//TODO remove temporary tracing output
									System.out.println("Start of meeting time");
									
									break;
								}
								case 5:{	//Meeting Time
									String period;
									
									//entry is present directly if not TBA
									if(entryScanner.hasNextLine()){
										period = entryScanner.nextLine();
									}else{
										entryIterator.next();
										period = entryIterator.next();
										entryIterator.next();
										entryIterator.next();
									}
									
									periodList.add(meetingIndex, period);
									
									//TODO remove temporary tracing output
									System.out.println("Period: " + period);
									
									break;
								}
								case 9:{	//Meeting Days
									boolean[] days = new boolean[Day.values().length];
									
									//TODO remove temporary tracing output
									System.out.print("Days:");
									
									String dayString = entryScanner.next();
									if(dayString.compareTo("&nbsp;") != 0){
										for(char dayCode: dayString.toCharArray()){
											Day day = Day.getDay(new Character(dayCode).toString());
											days[day.value()] = true;
											
											//TODO remove temporary tracing output
											System.out.print(" " + day);
										}
									}
									
									daysList.add(meetingIndex, days);
									
									//TODO remove temporary tracing output
									System.out.println();
									
									break;
								}
								case 13:{	//Meeting Location
									String location;
									
									//entry is present directly if not TBA
									if(entryScanner.hasNextLine()){
										location = entryScanner.nextLine();
									}else{
										entryIterator.next();
										location = entryIterator.next();
										entryIterator.next();
										entryIterator.next();
									}
									
									if(graduate){
										if(entry.compareTo("Distance Learning Center") == 0){
											courseType = CourseType.distanceGrad;
										}else{
											courseType = CourseType.campusGrad;
										}
									}else{
										courseType = CourseType.undergrad;
									}

									locationList.add(meetingIndex, location);
									
									//TODO remove temporary tracing output
									System.out.println("Course Type: " + courseType);
									System.out.println("Location: " + location);
									
									break;
								}
								case 17:{	//Meeting Course Dates
									
									break;
								}
								case 21:{	//Meeting Schedule Type
									
									break;
								}
								case 24:{	//Meeting Instructor
									List<String> instructors = new ArrayList<String>();
									int instructorPosition = 0;
									
									//TODO handle the multiple instructor scenario
									String startTag = entryScanner.next();
									
									while(entryIterator.hasNext()){
										entry = entryIterator.next();
										entryScanner = new Scanner(entry);
										String beginning = "";
										
										if(entryScanner.hasNext()){
											beginning = entryScanner.next();
										}
										
										//TODO remove tracing output
										System.out.println("#" + queueID + ":" + position + ":" + meetingIndex + ":" + meetingPosition + ":" + instructorPosition + ": " + entry);
										
										String instructor;
										
										if(entry.startsWith("/" + startTag)){
											break;
										}else{
											if(entry.compareTo("") == 0) {
												//skip
											}else if(beginning.compareTo("ABBR") == 0 || beginning.compareTo("A") == 0){
												while(!entry.startsWith("/" + beginning)){
													entry = entryIterator.next();
													
													if(entry.compareTo("TBA") == 0){
														instructor = entry;
														instructors.add(instructor);
													}
												}
											}else if(entry.startsWith(")")){
												//skip
											}else if(beginning.compareTo("TD") == 0){
												//skip
											}else{
												instructor = entry;
												if(entry.endsWith("(")){
													instructor = instructor.substring(0, entry.length()-2);
												}
												if(instructor.startsWith(", ")){
													instructor = instructor.substring(2, entry.length()-1);
												}
												
												instructors.add(instructor);
											}
											
										}
										instructorPosition++;
									}
									
									instructorList.add(meetingIndex, instructors);
									
									//TODO remove temporary tracing output
									System.out.print("Instructor List:");
									for(String instructorEntry: instructors){
										System.out.print(" " + instructorEntry);
									}
									System.out.println("");
									
									break;
								}
								case 26:{	//End of Meeting
									meetingIndex++;
									meetingPosition = -1;
									
									//TODO remove temporary tracing output
									System.out.println("End of Meeting");
									
									entryIterator.next();
									entry = entryIterator.next();
									if(entry.compareTo("/TABLE") == 0){
										tableEnd = true;
									}else{
										entryIterator.next();
									}
									
									break;
								}
							}
							meetingPosition++;
						}
					}
					break;
				}
				case 199:{	//Seats
					
					if(entryScanner.hasNextInt()){
						seats = entryScanner.nextInt();
						
						if(seats < 0) {
							seats = 0;
						}
						
						//TODO remove temporary tracing output
						System.out.println("Open Seats: " + seats);
					}
					
					break;
				}
			}
			
			entryScanner.close();
			position++;
		}
		
		Section section = new Section();
		section.setType(courseType);
		section.setCRN(crn.intValue());
		section.setCourseID(courseId);
		section.setSection(sectionId);
		section.setCredit(credits);
		section.setTitle(title);
		//TODO professor
		
		//TODO update for more than 2 entries
		section.setSecondary(daysList.size() > 1);
		
		//make a note if there are unrecognized meeting times
		if(daysList.size() > 2) {
			notes += "\nThis course has more than 2 meeting times. /nPlease confirm that the additional meeting times do not impact your schedule.";
			section.setNotes(notes);
		}else{
			section.setNotes(notes);
		}
		
		section.setPeriod(periodList.get(0));
		if(periodList.size() > 1){
			section.setSecPeriod(periodList.get(1));
		}else{
			section.setSecPeriod("");
		}
		
		section.setDays(daysList.get(0));
		if(daysList.size() > 1){
			section.setSecDays(daysList.get(1));
		}else{
			section.setSecDays(new boolean[Day.values().length]);
		}
		
		section.setLocation(locationList.get(0));
		if(locationList.size() > 1){
			section.setLocation(locationList.get(1));
		}else{
			section.setSecLocation("");
		}
		
		section.setSeats(seats);
		
		return section;
	}
	
	
	/********************************************************
	 * @purpose main method for parsing courses
	 * @param String term: the term being requested
	 * @param String url: the url to request the course page from
	 * 
	 * @param int parser: the parser code for the desired parsing algorithm
	 * @return Database: the newly parsed database
	*********************************************************/
	public static Database parseCourses(Parser parser, String term, String url, ThreadSynch sync){
		
		sync.allowUpdate = true;						//allow progress monitor updates
		try{											//encapsulate to catch exceptions
			Main.master.mainMenu.termChooser.previous 	//set the previous term's string
				= Main.prefs.getCurrentTerm();
			Main.master.mainMenu.termChooser.lastTerm	//set the last term's database
				= Main.terms.get(Main.prefs.getCurrentTerm());
			
			switch(parser){								//choose parser
			case ku:{return parseKUCourses(term, url, sync);}//use KU parser
			
			default:{throw new IOException("Invalid parser specified");}//no parser found, throw exception
			}
		}
		catch(IOException ex){							//catch exception, IO or parser
			sync.updateWatch(null, sync.getWatch().getMaximum());//close progress
			JOptionPane.showMessageDialog(				//show download error
					Main.master.mainMenu.termChooser,
					"Unable to download from specified URL." + 
					" Verify your Internet connection, " +
					"the availability of the website, and the " +
					"requested term selection.\n\n" + 
					ex.toString() + "\n",
					"Error", JOptionPane.ERROR_MESSAGE);//display error
			Main.master.mainMenu.termChooser.restoreTerm();
			sync.closeWatch();							//finish the progress
			Main.master.setEnabled(true);				//enable the main frame
						
			for(int pos = 0; pos < Main.master.tabControl.getComponentCount(); pos++){
				if(Main.master.tabControl.getTitleAt(pos).equals(Term.getTermString(term))){
					Main.master.tabControl.remove(pos);//close all open 
				}
			}
			
			return Main.terms.get(term);				//return empty database
		}
	}
	
	
	/********************************************************
	 * @purpose Downloads professor info from rate my professor
	 * 		and parses into the database
	 * 
	 * @param Parser parser: the parser to use the SID of
	 * @param ProgressMonitor watch: the associated progress monitor
	 * 			for the operation
	 * @param ArrayList<ParseAssistThread> profAssist: the threads helping
	 * 			parse the profs 
	 * 
	 * @return ProfDatabase: the tree of professors
	*********************************************************/
	public static ProfDatabase parseRateMyProf(Parser parse, ThreadSynch sync){
		
		String sid = parse.getSID();					//get school id
		ProfDatabase profs = new ProfDatabase(Main.prefs.isRateMyProfessorEnabled());//create tree map
				
		for(Char letter: Char.values()){				//iterate through all characters
			if(sync.isCanceled()){
				return null;							//cancel the downloading
			}
			ParseAssistThread thisLetter = new ParseAssistThread();
			thisLetter.setLetter(letter);				//set letter for thread
			
			thisLetter.setSync(sync);					//set the thread sync assistant
			thisLetter.setProfs(profs);					//set profs for thread group
			thisLetter.setSid(sid);						//set sid for thread group
			
			sync.addHelper(thisLetter);					//add to the helper list
			
			Main.threadExec.execute(thisLetter);		//run thread asymchronously
		}
		Prof staff = new Prof();						//create new prof for STAFF entries
		profs.addIfNew(staff);							//add the STAFF professor to the list if new
		
		while(sync.hasLivingHelpers()){					//wait until all threads finished
			if(sync.isCanceled()){						//check if user cancelled
				sync.cancel();							//cancel the operation
			}
		}					
		
		parseRMPFixFile(profs);							//fix the RMP database
		return profs;									//return the list
	}
	
	
	/********************************************************
	 * @purpose Fixes the names in the RMP database based on 
	 * 		input from the Profs.txt inside the Data folder
	 * 
	 * @param ProfDatabase: the tree of professors to fix
	*********************************************************/
	public static void parseRMPFixFile(ProfDatabase profs){
		try{											//for file not found exceptions
			File in = new File(Main.fixRMP);			//check if external rmp fix file
			Scanner file;								//make scanner for reading fix file
			
			if(!in.exists()){							//if external file does not exist
				file = new Scanner(Main.fixRMPFile);	//get fix file from the jar
			}
			else{
				file = new Scanner(in);					//open the file from the system location
			}			
						
			while(file.hasNext()){						//while there are more lines to look at
				try{									//catch bad lines of input
					Scanner first = new Scanner(file.nextLine());//get the whole line
					
					first.useDelimiter(";");			//set comment delimiter
					String line = first.next();			//get the non comment portion
				
					Scanner second = new Scanner(line);	//make a scanner to parse rmp from ku names
					second.useDelimiter(":");			//set to separator
					String rmp = second.next().trim();	//get the rmp name
					second.skip(":");					//skip the delimiter
					second.reset();						//reset to default delimiter
					String ku = second.nextLine().trim();//get the ku name
					
					Prof old = profs.remove(rmp);		//remove old prof with rmp name
					
					old.setName(ku);					//set the ku name to the prof rating
					profs.put(old.getName(), old);		//put the prof back in the list
				}
				catch(Exception ex){}					//catch bad line input, but do nothing
			}
		}
		catch(Exception ex1){ex1.printStackTrace(); System.err.println(ex1.getLocalizedMessage());}							//catch file not found but do nothing
	}
}
