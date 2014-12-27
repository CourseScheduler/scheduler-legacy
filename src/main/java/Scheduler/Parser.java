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
import io.devyse.scheduler.analytics.keen.KeenEngine;
import io.devyse.scheduler.parse.jsoup.banner.CourseSearchParser;
import io.devyse.scheduler.parse.jsoup.banner.CourseSelectionParser;
import io.devyse.scheduler.parse.jsoup.banner.TermSelectionParser;
import io.devyse.scheduler.retrieval.StaticSelector;
import io.devyse.scheduler.retrieval.TermSelector;

import java.io.IOException;						//import IOExceptions
import java.io.File;							//import File class
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;						//import scanner
import java.util.concurrent.ForkJoinPool;

import javax.swing.JOptionPane;					//Import message pane

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


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
	
	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(Parser.class);
	
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
			logger.info("Querying course data from Banner");
			CourseSearchParser courseParse = new CourseSearchParser(pool.invoke(courseSelect), new LegacyDataModelPersister(items));;

			if(sync.isCanceled()){
				pool.shutdownNow();
				return null;
			}
			
			sync.updateWatch("Processing courses retrieved from Banner", sync.finished++);
			logger.info("Processing courses retrieved from Banner");
			pool.execute(courseParse);
			
			//simple progress updating
			long last = pool.getQueuedTaskCount();
			while(!courseParse.isDone()){
				Thread.sleep(100);
				long queued = pool.getQueuedTaskCount();
				sync.finished = (int)(sync.finished + Long.max((last-queued),1));
				sync.updateWatch("Waiting for " + queued + " processing tasks to complete", sync.finished);
				logger.info("Waiting for {} processing tasks to complete", queued);
				last = queued;

				if(sync.isCanceled()){
					logger.info("Download cancelled. Shutting down executor pool");
					pool.shutdownNow();
					return null;
				}
			}
			
			sync.updateWatch("Finished processing courses from Banner", sync.finished++);
			logger.info("Finished processing courses from Baner");
			return items;
		} catch (Exception e) {
			logger.error("Error retrieving or parsing course dataset from Banner", e);
			return null;
		}
	}
	
	private static void registerDownloadEvent(String url, String term, Database items, boolean downloadRatings, long runtime){
		if(!Main.prefs.isAnalyticsOptOut()){

			Map<String, Object> event = new HashMap<>();
			event.put("university.name", "Kettering University");
			event.put("university.url", url);
			event.put("university.term", term);
			event.put("results.courses.count", items.getDatabase().size());
			event.put("results.courses.undergrad", items.isUndergrad());
			event.put("results.courses.graduate_distance", items.isGradDist());
			event.put("results.courses.graduate_campus", items.isGradCampus());
			event.put("results.professors.count", items.getProfs().size());
			event.put("results.professors.rate_my_prof", downloadRatings);
			event.put("results.runtime", Long.valueOf(runtime));
			
			KeenEngine.getDefaultKeenEngine().registerEvent(Main.KEEN_DOWNLOAD, event);
		}
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
		catch(Exception ex1){
			logger.warn("Unable to access professor name realignment file", ex1);
		}							//catch file not found but do nothing
	}
}
