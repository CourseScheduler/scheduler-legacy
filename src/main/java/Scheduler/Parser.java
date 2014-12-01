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
import io.devyse.scheduler.parse.jsoup.banner.CourseSearchParser;
import io.devyse.scheduler.parse.jsoup.banner.CourseSelectionParser;
import io.devyse.scheduler.parse.jsoup.banner.TermSelectionParser;
import io.devyse.scheduler.retrieval.StaticSelector;
import io.devyse.scheduler.retrieval.TermSelector;

import java.io.IOException;						//import IOExceptions
import java.io.File;							//import File class
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;						//import scanner
import java.util.concurrent.ForkJoinPool;

import javax.swing.JOptionPane;					//Import message pane

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;

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
		
		long start = System.currentTimeMillis();
		jsoupParse(items, sync, url, term);
		long end = System.currentTimeMillis();		
		
		if(sync.isCanceled()){
			return null;
		}
		
		sync.updateWatch(null, sync.getWatch().getMaximum());//set progress finished
		items.setTerm(term);							//set database term
		
		registerDownloadEvent(url, term, items, downloadRatings, end - start);
		
		return items;									//return database
	}
	
	private static Database jsoupParse(Database items, ThreadSynch sync, String url, String term){
		String startURL = "https://jweb.kettering.edu/cku1/xhwschedule.P_SelectSubject";
		
		url = startURL;
		
		ForkJoinPool pool = new ForkJoinPool();
				
		try {
			TermSelector selector = new StaticSelector(term);
			sync.updateWatch("Checking available terms in Banner", sync.finished++);
			TermSelectionParser termSelect = new TermSelectionParser(Jsoup.connect(url).method(Method.GET).execute().parse(), selector);

			if(sync.isCanceled()){
				pool.shutdownNow();
				return null;
			}
			
			CourseSelectionParser courseSelect = new CourseSelectionParser(pool.invoke(termSelect));
			items.setTerm(selector.getTerm().getId());

			if(sync.isCanceled()){
				pool.shutdownNow();
				return null;
			}
			
			sync.updateWatch("Querying course data from Banner", sync.finished++);
			CourseSearchParser courseParse = new CourseSearchParser(pool.invoke(courseSelect), new LegacyDataModelPersister(items));;

			if(sync.isCanceled()){
				pool.shutdownNow();
				return null;
			}
			
			sync.updateWatch("Processing courses retrieved from Banner", sync.finished++);
			pool.execute(courseParse);
			
			//simple progress updating
			long last = pool.getQueuedTaskCount();
			while(!courseParse.isDone()){
				Thread.sleep(100);
				long queued = pool.getQueuedTaskCount();
				sync.finished = (int)(sync.finished + Long.max((last-queued),1));
				sync.updateWatch("Waiting for " + queued + " processing tasks to complete", sync.finished);
				last = queued;

				if(sync.isCanceled()){
					pool.shutdownNow();
					return null;
				}
			}
			
			sync.updateWatch("Finished processing courses from Banner", sync.finished++);
			return items;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private static void registerDownloadEvent(String url, String term, Database items, boolean downloadRatings, long runtime){
		if(!Main.prefs.isAnalyticsOptOut()){

			Map<String, Object> event = new HashMap<>();
			Main.mapifyEntry(event, "university.name", "Kettering University");
			Main.mapifyEntry(event, "university.url", url);
			Main.mapifyEntry(event, "university.term", term);
			Main.mapifyEntry(event, "results.courses.count", items.getDatabase().size());
			Main.mapifyEntry(event, "results.courses.undergrad", items.isUndergrad());
			Main.mapifyEntry(event, "results.courses.graduate_distance", items.isGradDist());
			Main.mapifyEntry(event, "results.courses.graduate_campus", items.isGradCampus());
			Main.mapifyEntry(event, "results.professors.count", items.getProfs().size());
			Main.mapifyEntry(event, "results.professors.rate_my_prof", downloadRatings);
			Main.mapifyEntry(event, "results.runtime", Long.valueOf(runtime));
			
			Main.registerEvent(Main.KEEN_DOWNLOAD, event);
		}
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
				case 52:{	//Meeting Times Exist indicator
					hasMeetingTimes = (entry.compareTo("") != 0);
					
					break;
				}
				case 82:{	//Meeting Time Table
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
				case 142:	//Seats can also be at 142
				case 183:{	//or at 183
					
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
		section.setType(courseType);	//--done
		section.setCRN(crn.intValue());	//--done
		section.setCourseID(courseId);	//--done
		section.setSection(sectionId);	//--done
		section.setCredit(credits);		//--done
		section.setTitle(title);		//--done
		
		if(instructorList.size()>0){
			section.setInstructorList(instructorList.get(0));	//--done
		}
		
		//TODO update for more than 2 entries
		section.setSecondary(daysList.size() > 1);	//--done
		
		//make a note if there are unrecognized meeting times
		if(daysList.size() > 2) {
			notes += "\nThis course has more than 2 meeting times. /nPlease confirm that the additional meeting times do not impact your schedule.";
			section.setNotes(notes);
		}else{
			section.setNotes(notes);
		}
		
		if(periodList.size() > 0){		//--done
			section.setPeriod(periodList.get(0));
			if(periodList.size() > 1){
				section.setSecPeriod(periodList.get(1));
			}else{
				section.setSecPeriod("");
			}
		}else{
			section.setPeriod("");
		}
		
		if(daysList.size() > 0){		//--done
			section.setDays(daysList.get(0));
			if(daysList.size() > 1){
				section.setSecDays(daysList.get(1));
			}else{
				section.setSecDays(new boolean[Day.values().length]);
			}
		}else{
			section.setDays(new boolean[Day.values().length]);
		}
		
		if(locationList.size() > 0){	//--done
			section.setLocation(locationList.get(0));
			if(locationList.size() > 1){
				section.setLocation(locationList.get(1));
			}else{
				section.setSecLocation("");
			}
		}else{
			section.setLocation("");
		}
		
		section.setSeats(seats);		//--done
		
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
					
					second.close();
					first.close();
				}
				catch(Exception ex){}					//catch bad line input, but do nothing
			}
			file.close();
		}
		catch(Exception ex1){ex1.printStackTrace(); System.err.println(ex1.getLocalizedMessage());}							//catch file not found but do nothing
	}
}
