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
 * File: ThreadSynch.java
 * 
 * Contains class:
 * 
 * 		ThreadSynch:
 * 
 * 			Purpose: To synchronize the work of the worker threads
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;								//declare as member of scheduler package


/*********************************************************
 * The following imports are necessary for this class
*********************************************************/
import java.util.ArrayList;						//for storing lists
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;						//for the worker thread list
import javax.swing.SwingWorker;					//the worker threads are SwingWorkers
import javax.swing.ProgressMonitor;				//for monitoring the progress

import com.pollicitus.scheduler.retrieval.BannerDynamicCourseRetrieval;


/*********************************************************
 * Class ThreadSynch
 * 
 * @purpose To help synchronize shared objects between threads
*********************************************************/
public class ThreadSynch implements Observer {
	
	
	/*********************************************************
	 * The following are static constants for versioning
	*********************************************************/
	protected static final long versionID = 2013010900015L;	//object version
	
	
	/*********************************************************
	 * The following are public static class fields
	*********************************************************/
	public static int invalid = -1;				//the invalid value
	
	
	/*********************************************************
	 * The following are protected class fields
	*********************************************************/
	protected int finished = 0;					//the number of finished items
	protected boolean allowUpdate = true;		//if monitor updates are allowed
	
	
	/*********************************************************
	 * The following are private class fields
	*********************************************************/
	private ProgressMonitor watch;				//progress monitor for downloads
	private Vector<SwingWorker<Void,Void>> helpers;//rmp helper threads
	private Thread parent;						//the parent thread
	private boolean isCancelled = false;		//if the operation is cancelled
	private ArrayList<String> primary;			//the primary course list
	private MakeSchedule owner;					//the owning frame for this synchronization object
	protected boolean failed = false;			//if the build failed, initialize to false
	private CourseType type;					//the course types allowed
	private int permute;						//the maximum value of the monitor
	private Vector<Conflict> conflicts;			//the conflicts from this schedule build
	
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose constructs a new ThreadSynch objet and initialized the
	 * 		necessary fields
	*********************************************************/
	public ThreadSynch(){
		helpers = new Vector<SwingWorker<Void,Void>>();
		conflicts = new Vector<Conflict>();
	}
	
	
	/*********************************************************
	 * @purpose adds a SwingWorker Helper to the helper list
	 * 
	 * @param SwingWorker<Void,Void> helper: the worker to add
	 * 
	 * @see synchronized
	*********************************************************/
	public synchronized void addHelper(SwingWorker<Void,Void> helper){
		helpers.add(helper);					//add the worker
	}
	
	
	/*********************************************************
	 * @purpose increment the progress and update the progress monitor
	 * 
	 * @see synchronized
	*********************************************************/
	public synchronized void incrementProgress(){
		finished++;								//increment the finished value
		updateWatch(null, finished);			//only change the progress int
	}
	
	
	/*********************************************************
	 *  @purpose Closes the progress monitor for this sync object
	*********************************************************/
	public void closeWatch(){
		allowUpdate = false;					//disallow updates
		
		watch.close();							//close the monitor
	}
	
	
	/*********************************************************
	 * @purpose Synchronously update the progress monitor
	 * 
	 * @param String note: the string to set as the monitor's note
	 * @param int progress: the int value to use as the progress
	 * 
	 * @see synchronized
	*********************************************************/
	public synchronized void updateWatch(String note, int progress){
		if(note != null){						//check if note is not null
			watch.setNote(note);				//set the note if not null
		}
		
		if(progress != invalid){				//check if progress is not invalid
			watch.setProgress(progress);		//set the progress if not invalid
		}
	}
	
	
	/*********************************************************
	 * @purpose Returns if the operation is cancelled
	*********************************************************/
	public synchronized boolean isCanceled(){
		return isCancelled || watch.isCanceled();//return operation cancelled or monitor cancelled
	}
	
	
	/*********************************************************
	 * @purpose Returns the progress monitor associated with this synch object
	 * 
	 * @return ProgressMonitor: this synch objects monitor
	*********************************************************/
	public ProgressMonitor getWatch() {
		return watch;							//return the monitor
	}
	
	
	/*********************************************************
	 * @purpose Sets the progress monitor for this synch object
	 * 
	 * @param ProgressMonitor watch: the monitor for this thread
	*********************************************************/
	public void setWatch(ProgressMonitor watch) {
		this.watch = watch;						//set the monitor
	}
	
	
	/*********************************************************
	 * @purpose Returnt the list of helper threads
	 * 
	 * @return ArrayList<SwingWorker<Void,Void>>: the list of workers
	*********************************************************/
	public Vector<SwingWorker<Void,Void>> getHelpers() {
		return helpers;							//return the list of workers
	}
	
	
	/*********************************************************
	 * @purpose set the list of helper workers
	 * 
	 * @param ArrayList<SwingWorker<Void,Void>> helpers: the list of workers
	*********************************************************/
	public void setHelpers(Vector<SwingWorker<Void,Void>> helpers) {
		this.helpers = helpers;					//set the list of helper workers
	}
	
	
	/*********************************************************
	 * @purpose return the parent thread
	 * 
	 * @return Thread: the parent thread that this synch object is working for
	*********************************************************/
	public Thread getParent() {
		return parent;							//return the parent thread
	}
	
	
	/*********************************************************
	 * @purpose Sets the parent thread for this synch object
	 * 
	 * @param Thread parent: the thread that should be this objects parent
	*********************************************************/
	public void setParent(Thread parent) {
		this.parent = parent;					//set the parent thread
	}
	
	
	/*********************************************************
	 * @purpose Cancels this synch objects operation
	*********************************************************/
	public void cancel(){
		isCancelled = true;						//set cancelled
		allowUpdate = false;					//disallow progress monitor updates
		
		for(SwingWorker<Void,Void> helper: helpers){//for each worker
			helper.cancel(true);				//cancel the worker
		}
	}
	
	
	/*********************************************************
	 * @purpose return if this synch object has living helpers
	 * 
	 * @return boolean: if there are living worker threads
	*********************************************************/
	public boolean hasLivingHelpers(){
		return !helpers.isEmpty();				//return if any workers are living
	}
	
	
	/*********************************************************
	 * @purpose Removes the helper from the list of helpers in a synchronized manner
	 * 
	 * @param SwingWorker<Void,Void> helper: the worker to remove from the list of helpers
	*********************************************************/
	public synchronized void removeHelper(SwingWorker<Void,Void> helper){
		helpers.remove(helper);					//remove the helper
	}


	public ArrayList<String> getPrimary() {
		return primary;
	}


	public void setPrimary(ArrayList<String> primary) {
		this.primary = primary;
	}


	public MakeSchedule getOwner() {
		return owner;
	}


	public void setOwner(MakeSchedule owner) {
		this.owner = owner;
	}


	public CourseType getType() {
		return type;
	}


	public void setType(CourseType type) {
		this.type = type;
	}
	
	/********************************************************
	 * @purpose Increment the progress displayed on the progress monitor
	 * 		in a synchronized manner
	 * 
	 * @see synchronized
	********************************************************/
	public synchronized void incrementProgressValue(){
		finished++;							//increment number of finished schedules
	}
	
	public synchronized void updateProgressValue(){
		updateWatch("Finished " + //display the new progress
				finished + " of " + permute + ".", finished);
	}


	public int getPermute() {
		return permute;
	}


	public void setPermute(int permute) {
		watch.setMaximum(permute);
		this.permute = permute;
	}


	public Vector<Conflict> getConflicts() {
		return conflicts;
	}


	public void setConflicts(Vector<Conflict> conflicts) {
		this.conflicts = conflicts;
	}
	
	public void addConflict(Conflict toAdd){
		conflicts.add(toAdd);
	}


	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof BannerDynamicCourseRetrieval){
			try{
				String message = (String)arg;
				updateWatch(message, finished++);
			}catch(Exception e){
				
			}
		}
	}
}
