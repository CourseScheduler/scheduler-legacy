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
 * File: ParseThread.java
 * 
 * Contains class:
 * 
 * 		ParseThread:
 * 
 * 			Purpose: To managae downloading and parsing from
 * 				Banner and Rate-My-Professor to allow progress 
 * 				monitoring
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;							//declare as member of scheduler package


/********************************************************
 * Import the Swing Worker class to implement
********************************************************/
import java.awt.Component;					//import Component superclass
import java.util.Scanner;					//import scanner utility
import javax.swing.JOptionPane;				//import JOptionPane
import javax.swing.ProgressMonitor;			//import progress monitor
import javax.swing.SwingWorker;				//import swing worker


/********************************************************
 * Class ParseThread
 * 
 * 		@purpose Manage the parsing thread so the gui 
 * 			doesn't freeze when executing the parse routine
 * 
 * 		@see SwingWorker
********************************************************/
public class ParseThread extends SwingWorker<Database, Void> {
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2013010900068L;	//class version
	
	
	/********************************************************
	 * The following are the protected fields of the Thread
	********************************************************/
	private Parser engine;					//the parsing engine to use
	private String term;					//the term to query for
	private String url;						//the url to query
	private ThreadSynch sync;				//thread synch helper	
	
	
	/********************************************************
	 * @purpose Does the background task when the task is run
	 * 
	 * @see Override, SwingWorker
	 * 
	 * @throws Exception
	 * 
	 * @return Database: the completed database
	********************************************************/
	@Override		
	protected Database doInBackground() throws Exception {
		sync = new ThreadSynch();						//create the thread synchronization object
		sync.finished = 0;								//set finished to none
		
		ProgressMonitor watch = new ProgressMonitor(Main.master, "Downloading: " + Term.getTermString(term) 
			, "", 0, 500);								//create progress monitor
		watch.setMillisToDecideToPopup(0);				//set to decide to popup right away
		watch.setMillisToPopup(0);						//set to popup right away
		
		sync.setWatch(watch);							//set the progress monitor
		
		sync.updateWatch("Connecting to www.ratemyprofessors.com", 0);//set initial text
		
		return Parser.parseCourses(engine, new String(term), url, sync);//return the database
	}
	
	
	/********************************************************
	 * @purpose To restore everything when the task is done
	 * 
	 * @see Override, SwingWorker
	********************************************************/
	@Override
	protected void done(){
		Database local = null;							//clear database
		if(sync.isCanceled()){							//check if user cancelled
			JOptionPane.showMessageDialog(Main.master, 	//show cancelled message
				"Download cancelled by user!", "Cancelled", JOptionPane.ERROR_MESSAGE);
		}
				
		try{											//required by get()
			local = get();								//get the result of the thread
				
			if (local != null){							//check if null result
				Main.terms.put(term, local);			//get the result of the task
				
				local.save();							//save the database
						
				Main.master.setEnabled(true);			//reenable the master gui
				
				if(Main.master != null){				//update any make schedule tabs that need it
					for(int pos = 0; pos < Main.master.tabControl.getTabCount(); pos++){//for each tab
						Component item = Main.master.tabControl.getComponentAt(pos);//get the component there
						
						if(item instanceof MakeSchedule){//check the class
							String title = Main.master.tabControl.getTitleAt(pos);
							
							if(title.contains("-")){	//check if needs to be parsed
								Scanner parse = new Scanner(title);//make a parser
								parse.useDelimiter(" - ");//set delimiter
								title = parse.next().trim();//get term title
								parse.close();
							}
							
							String term = Term.getTermString(local.getTerm());//get term string
							
							if(title.compareTo(term) == Compare.equal.value()){
								((MakeSchedule)item).setDatabase(local, true, true);//set database and update schedueles
							}
						}
					}
					
					Main.master.mainMenu.newScheduleMenu.setEnabled(true);//enable the new schedule menu item
				}
			}
		}
		catch (Exception ex){						//catch exception
			if(Main.terms.get(Main.prefs.getCurrentTerm()) == null){
				Main.master.mainMenu.newScheduleMenu.setEnabled(false);//disable the new schedule menu item
			}
		}											//do nothing
		finally{			
			Main.displayDate();						//display the download date
			Main.master.setEnabled(true);			//reenable
			Main.master.requestFocus();				//request focus
			
			if(Main.termChanged){					//check if the term changed
				String newTerm = Main.prefs.getCurrentTerm();//get new term
				String term = Term.getTermString(newTerm);	//convert term to title
				
				boolean newMake = true;				//for deciding if we need a new make schedule tab
				
				for(int pos = 0; pos < Main.master.tabControl.getTabCount(); pos++){//for each tab
					Component com = Main.master.tabControl.getComponentAt(pos);//get the component there
					
					if(com instanceof MakeSchedule){//check the class
						String title = Main.master.tabControl.getTitleAt(pos);//get tab title
						
						if(title.contains("-")){	//check if needs to be parsed
							Scanner parse = new Scanner(title);//make a parser
							parse.useDelimiter(" - ");//set delimiter
							title = parse.next().trim();//get term title
							parse.close();
						}
						
						if(title.compareTo(term) == Compare.equal.value()){//check if equal
							newMake = false;		//do not need new make
						}
					}
				}
				
				if(newMake && local != null){		//check if should prompt for new schedule
					int result = JOptionPane.showOptionDialog(Main.master, "Create a new schedule for " + 
						Term.getTermString(newTerm), "Create new Schedule?",//ask if we want a new make schedule tab
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
						null, null, null);
					
					if(result == JOptionPane.YES_OPTION){	//if result is yes
						Main.master.mainMenu.newScheduleMenu.doClick();//make tab
					}
				}
				Main.termChanged = false;			//reset the changed flag
			}

			Main.reRateAll();
			sync.closeWatch();						//close the progress monitor
		}
	}

	
	/********************************************************
	 * @purpose returns the engine used by this task
	 * 
	 * @return Parser: the parsing engine used
	********************************************************/
	public Parser getEngine() {
		return engine;								//return the parsing engine
	}

	
	/********************************************************
	 * @purpose Sets the parser engine
	 * 
	 * @param Parser engine: the engine to assign to thie task
	********************************************************/
	public void setEngine(Parser engine) {
		this.engine = engine;						//set the engine
	}

	
	/********************************************************
	 * @purpose returnt the term specified for this thread
	 * 
	 * @return String: the term associated with this thread
	********************************************************/
	public String getTerm() {
		return term;								//return this term
	}

	
	/********************************************************
	 * @purpose Set the term associated with this thread
	 * 
	 * @param String term: the string to set this thread's term to
	********************************************************/
	public void setTerm(String term) {
		this.term = term;							//set the term
	}
	
	
	/********************************************************
	 * @purpose return the url associated with the thread
	 * 
	 * @return String: the url associated with this thread
	********************************************************/
	public String getUrl() {	
		return url;									//return the url
	}

	
	/********************************************************
	 * @purpose Sets the url for the thread
	 * 
	 * @param String url: the url to associate with this thread
	********************************************************/
	public void setUrl(String url) {
		this.url = url;								//set the url
	}
}
