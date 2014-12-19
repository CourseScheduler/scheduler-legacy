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
 * File: ParseAssistThread.java
 * 
 * Contains class:
 * 
 * 		ParseAssistThread:
 * 
 * 			Purpose: To provide a thread for assisting in downloading
 * 				professor ratings from Rate-My-Professor.com
 * 
 * @author Mike Reinhold
********************************************************/
package Scheduler;									//declare as member of scheduler package


/*********************************************************
 * The Following imports are necessary for the functioning of this class
********************************************************/
import java.io.InputStream;							//input streams
import java.io.IOException;							//input/output exceptions
import java.net.URL;								//url class
import java.util.Scanner;							//scanner utility

import javax.swing.SwingWorker;						//swing worker abstract class

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/*********************************************************
 * Class: ParseAssistThread
 * 
 * @purpose Provide a helper thread for downloading and managing
 * 		rate-my-professor ratings for professors
 * 
 * @see SwingWorker
********************************************************/
public class ParseAssistThread extends SwingWorker<Void, Void> {
	
	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(ParseAssistThread.class);
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2008071400041L;//class version
	
	
	/********************************************************
	 * The following are private static constants in the
	 * 		class for parsing rate my professor ratings
	*********************************************************/
	private static final String ratePrefix = new String("<td class=\"icon_col\">");//parsing prefix
	private static final String namePrefix1 = new String("<td><a href=\"ShowRatings.jsp?tid=");//parsing prefix
	private static final String namePrefix2 = new String("<td><a href=\"AddRating.jsp?tid=");//parsing prefix
	private static final String rateMyProf = new String("http://www.ratemyprofessors.com/SelectTeacher.jsp?the_dept=All&sid=");//posting url
	private static final String query = new String("&orderby=TLName&letter=");//posting url portion
	private static final int name = 0;				//iteration for the prof name
	private static final int numRate = 1;			//iteration for the numerical rating
	private static final int ratingItem = 2;		//iteration for the rating item
	private static final int easeRate = 3;			//iteration for the professor's ease rating
	private static final int startIndex = 1;		//the starting index
	private static final int scale = 10;			//scaling factor used to scale the ratings
	private static final int maxLen = 85;			//the max length of a valid line
	private static final int min = 0;				//minimum value for the iteration
	private static final int reset = -1;			//value to reset to
	
	
	/*********************************************************
	 * The following are the private dynamic values used within a thread
	********************************************************/
	private Char letter;							//the character for this thread
	private boolean exception = false;				//if this thread sourced an exception
	private ThreadSynch sync;						//thread sync object
	private ProfDatabase profs;						//the prof database to add to
	private String sid;								//the school id to use
	
	
	
	/*********************************************************
	 * @purpose provides the necessary method for running the thread,
	 * 		this is the task completed by the thread, parsing the profs
	 * 
	 * @see Override, SwingWorker<T,V>
	********************************************************/
	@Override										//signal override
	protected Void doInBackground() throws Exception {
		int time = min;								//create pass counter
		Prof next = new Prof();						//create new professor
		double rating = min;						//create double for the rating
		
		try{
			InputStream data = new URL(				//new url for rate my prof 
					rateMyProf + sid + query + letter).openStream();
							
			Scanner test = new Scanner(data);		//create scanner on the stream
			String val = new String();				//create new string for line
			
			while(test.hasNext()){					//while more lines to read
				
				if(sync.isCanceled()){				//check if cancelled
					sync.allowUpdate = false;		//disallow update
					sync.removeHelper(this);		//remove this
					return null;					//return null
				}
				
				val = test.nextLine().trim();		//get line and trim
									
				if (val.length() < maxLen && (val.startsWith(ratePrefix)
						|| val.startsWith(namePrefix1) || val.startsWith(namePrefix2))){
													//validate length, and prefixes
					switch(time){					//switch to correct parameter
						case name:{					//current param is name
							Scanner reader = new Scanner(val);//line reader
							reader.useDelimiter(">");//parse by delimiter ">"
							reader.next();			//parse first time
							reader.next();			//parse again
							reader.useDelimiter("<");//parse by delimiter "<"
							String nameStr = reader.next().substring(startIndex);//get last name
							reader.useDelimiter(">");//parse by delimiter ">"
							reader.next();			//parse once
							reader.useDelimiter("<");//parse by delimiter "<"
							nameStr += reader.next().substring(startIndex);//get first name
							next = new Prof();		//create new prof
							next.setName(nameStr);	//set prof name
							reader.close();
							break;					//break case
						}
						case numRate:{break;}		//break the case since we don't care about num of ratings
						case ratingItem:{			//current param is rating
							Scanner reader = new Scanner(val);//get line reader
							reader.useDelimiter(">");//parse by delimiter ">"
							reader.next();			//parse next item
							reader.useDelimiter("<");//parse by delimiter "<"
							String temp = reader.next().substring(startIndex);//get rating as string
							try{
								rating = new Scanner(temp).nextDouble();//try reading double
							}
							catch(Exception ex){	//catch conversion failure
								rating = min;		//set rating to 0
							}
							reader.close();
							break;					//break the case
						}
						case easeRate:	{			//current parameter is ease rating
							Scanner reader = new Scanner(val);//get line reader
							reader.useDelimiter(">");//parse by delimiter ">"
							reader.next();			//parse next item
							reader.useDelimiter("<");String temp = reader.next().substring(1);//get rating as string
							try{					
								rating += new Scanner(temp).nextDouble();//try reading double
							}
							catch(Exception ex){	//catch conversion failure
								rating += min;		//add to rating
							}
							next.setRating(rating * scale); //scale rating
							
							profs.addIfNew(next);	//add if new prof						
							
							time = reset;			//reset current parameter
							reader.close();
							break;					//break the case
						}
					}
					time++;							//increment the current parameter
				}
			}
			test.close();
		}
		catch(IOException ex){
			logger.error("Unable to parse professor and rating", ex);	//print out error message
			exception = true;						//set exception to true
		}
		return null;								//return Void type
	}


	/*********************************************************
	 * @purpose return the letter for this thread
	 * 
	 * @return Char: the letter this thread was dispatched for
	********************************************************/
	public Char getLetter() {
		return letter;								//return the char
	}


	/*********************************************************
	 * @purpose set the letter for this thread to download and parse
	 * 
	 * @param Char letter: the letter this thread is dispatched for
	********************************************************/
	public void setLetter(Char letter) {
		this.letter = letter;						//set the letter
	}


	/*********************************************************
	 * @purpose return the Rate-My-Professor school id for this thread
	 * 
	 * @return String: the SID for this thread
	********************************************************/
	public String getSid() {
		return sid;									//return the School id
	}

	
	/*********************************************************
	 * @purpose set the school id for this thread
	 * 
	 * @param String sid: the Rate-My-Professor school id for this thread
	********************************************************/
	public  void setSid(String sid) {
		this.sid = sid;								//set the sid
	}


	/*********************************************************
	 * @purpose return the prof database from this thread
	 * 
	 * @return ProfDatabase: the ProfDatabase used by this thread
	********************************************************/
	public ProfDatabase getProfs() {
		return profs;								//return the database
	}


	/*********************************************************
	 * @purpose set the ProfDatabase for this thread
	 * 
	 * @param ProfDatabase profs: the ProfDatabase this thread should 
	 * 		add to
	********************************************************/
	public  void setProfs(ProfDatabase profs) {
		this.profs = profs;							//set the prof database
	}


	/*********************************************************
	 * @purpose to clean up after the thread is done
	 * 
	 * @see Override, SwingWorker<T,V>
	********************************************************/	
	@Override
	public void done(){
		sync.removeHelper(this);					//remove this thread from the thread list													
		
		if(!sync.isCanceled() && !this.isCancelled() && sync.allowUpdate){//check if cancelled
			if(sync.hasLivingHelpers()){			//check if no more threads helping
				if(exception){						//check for exceptions caught
					sync.updateWatch("Failed rating download: " + letter, -1);//set note for character
				}
				else{								//no problems
					sync.updateWatch("Finished rating download: " + letter, -1);//set note for character
				}
				sync.incrementProgress();			//increment the progress
			}
		}
	}


	/*********************************************************
	 * @purpose return the thread synchronization object
	 * 
	 * @return ThreadSynch: this thread's synchronization object
	********************************************************/
	public ThreadSynch getSync() {
		return sync;								//return the object
	}


	/*********************************************************
	 * @purpose set the thread synchronization object for this thread
	 * 
	 * @param ThreadSynch sync: the synchronization object
	********************************************************/
	public void setSync(ThreadSynch sync) {
		this.sync = sync;							//set the object
	}
}
