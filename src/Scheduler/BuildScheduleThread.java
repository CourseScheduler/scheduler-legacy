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
 * File: BuildScheduleThread.java
 * 
 * Contains class:
 * 
 * 		BuildScheduleThread:
 * 
 * 			Purpose: To manage building of schedules and
 * 				properly displayingprogress bars
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;							//declare as member of scheduler package


/********************************************************
 * Import the Swing Worker class to implement
********************************************************/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import javax.swing.DefaultListModel;		//import list model
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;				//import swing worker
import javax.swing.JOptionPane;				//import the option pane


/********************************************************
 * Class BuildScheduleThread
 * 
 * 		@purpose Manage the parsing thread so the gui 
 * 			doesn't freeze when executing the parse routine
 * 
 * 		@see SwingWorker
********************************************************/
public class BuildScheduleThread extends SwingWorker<Schedule[], Void>{
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2008101300068L;
	
	
	/********************************************************
	 * The following are the fileds of the worker thread
	********************************************************/
	private boolean allowClosed;			//if closed courses are allowed
	private String[] classes;				//the course keys to use
	private ArrayList<String> primary;		//the course keys for primary courses
	private int useMin;						//the minimum number of courses to allow
	private MakeSchedule owner;				//the owning frame
	private boolean allowPopup;				//if the popup for no courses found is allowed
	private ThreadSynch sync;				//the thread synch object
	private CourseType type;				//the course types allowed
	private TreeMap<String, boolean[]> allowedSections;//the sections that are allowed
	private TreeMap<String, Integer> numberSelections;//the number of each section
	private boolean reportingEnabled;		//if conflict reporting is enabled
	
	
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
	protected Schedule[] doInBackground() throws Exception {
		sync = new ThreadSynch();
		sync.finished = 0;
		sync.allowUpdate = true;
		sync.setOwner(owner);
		sync.setPrimary(primary);
		sync.setType(type);
		
		ProgressMonitor progress = new ProgressMonitor(Main.master, "Building Schedules: ",
				"Calculating Combinations and Assigning Schedule Tests to Threads",0, 2);	//set up progress monitor
				
		sync.setWatch(progress);
				
		boolean[][] allowSection = new boolean[classes.length][];
		int[] numberSections = new int[classes.length];
		
		for(int temp = 0; temp < classes.length; temp++){
			allowSection[temp] = allowedSections.get(classes[temp]);
			numberSections[temp] = numberSelections.get(classes[temp]).intValue();
		}
			
		return owner.local.makeSchedulesOpt(classes, primary, allowClosed, useMin, sync, allowSection, numberSections, reportingEnabled);//return the schedules
	}
	
	/********************************************************
	 * @purpose To restore everything when the task is done
	 * 
	 * @see Override, SwingWorker
	********************************************************/
	@Override
	protected void done(){
		try{											//required by get()			
			if(sync.isCanceled()){
				JOptionPane.showMessageDialog(Main.master, "Schedule build cancelled by user",
					"Build Cancelled", JOptionPane.ERROR_MESSAGE);
			}
			else{
				Schedule[] possible = get();			//get the result of the tast
				
				if (sync.failed){
					throw new NoSuchElementException();
				}
				
				owner.schedulesModel = new DefaultListModel();//make new list model
				
				for(Schedule item: possible){			//for each schedule found
					owner.schedulesModel.addElement(item);//add it to the model
				}
				
				Conflict[] confs = sync.getConflicts().toArray(new Conflict[0]);
				Arrays.sort(confs);
				
				for(Conflict conf: confs){
					owner.schedulesModel.addElement(conf);
				}
				
				owner.schedules.setListData(owner.schedulesModel.toArray());  //set the list data
				if(owner.schedulesModel.getSize() != 0){
					Object item = owner.schedulesModel.getElementAt(0);
					
					owner.showSchedule(item);
					owner.schedules.setSelectedIndex(0);//set selected index
				}
				else{
					owner.showSchedule(null);
					
					if(allowPopup){					
						JOptionPane.showMessageDialog(Main.master,
							"No available schedules for the requested courses.",
							"No Schedules Found", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
		catch (NoSuchElementException ex1){
			owner.updateSchedules(true);
		}
		catch (Exception ex){						//catch exception
			JOptionPane.showMessageDialog(owner, 
					"Unable to create schedules for the selected classes.", 
					"Unable to Build Schedules", JOptionPane.ERROR_MESSAGE);
			int[] vals = new int[owner.scheduleClassModel.size()];
			for(int col = 0; col < vals.length; col++){
				vals[col] = col;
			}
			
			owner.scheduleClassList.setSelectedIndices(vals);
			owner.removeCourse.doClick();
		}										//do nothing
		
		owner.updateScheduleCount();
		owner.setEnabled(true);					//reenable the owning gui
		owner.conditionalDisable();
		sync.closeWatch();
		Main.master.requestFocus();				//request focus
		owner.schedules.requestFocusInWindow();
	}

	
	/********************************************************
	 * @purpose Returns if closed courses are allowed
	 * 
	 * @return boolean: if closed courses are allowed in the schedule
	********************************************************/
	public boolean isAllowClosed() {
		return allowClosed;							//return if closed courses are allowed
	}

	
	/********************************************************
	 * 
	********************************************************/
	public void setAllowClosed(boolean allowClosed) {
		this.allowClosed = allowClosed;
	}

	
	/********************************************************
	 * 
	********************************************************/
	public String[] getClasses() {
		return classes;
	}

	
	/********************************************************
	 * 
	********************************************************/
	public void setClasses(String[] classes) {
		this.classes = classes;
	}

	
	/********************************************************
	 * 
	********************************************************/
	public int getUseMin() {
		return useMin;
	}

	
	/********************************************************
	 * 
	********************************************************/
	public void setUseMin(int useMin) {
		this.useMin = useMin;
	}

	
	/********************************************************
	 * 
	********************************************************/
	public MakeSchedule getOwner() {
		return owner;
	}

	
	/********************************************************
	 * 
	********************************************************/
	public void setOwner(MakeSchedule owner) {
		this.owner = owner;
	}

	
	/********************************************************
	 * 
	********************************************************/
	public boolean isAllowPopup() {
		return allowPopup;
	}

	
	/********************************************************
	 * 
	********************************************************/
	public void setAllowPopup(boolean allowPopup) {
		this.allowPopup = allowPopup;
	}

	public ArrayList<String> getPrimary() {
		return primary;
	}

	public void setPrimary(ArrayList<String> primary) {
		this.primary = primary;
	}

	public CourseType getType() {
		return type;
	}

	public void setType(CourseType type) {
		this.type = type;
	}

	public TreeMap<String, boolean[]> getAllowedSections() {
		return allowedSections;
	}

	public void setAllowedSections(TreeMap<String, boolean[]> allowedSections) {
		this.allowedSections = allowedSections;
	}

	public TreeMap<String, Integer> getNumberSelections() {
		return numberSelections;
	}

	public void setNumberSelections(TreeMap<String, Integer> numberSelections) {
		this.numberSelections = numberSelections;
	}

	public boolean isReportingEnabled() {
		return reportingEnabled;
	}

	public void setReportingEnabled(boolean reportingEnabled) {
		this.reportingEnabled = reportingEnabled;
	}	
}
