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
 * File: BuildAssistThread.java
 * 
 * Contains class:
 * 
 * 		BuildAssistThread:
 * 
 * 			Purpose: To assist in building schedules
 * 
 * @author Mike Reinhold
********************************************************/
package Scheduler;									//declare as member of scheduler package


/********************************************************
 * The following imports are necessary for this class
********************************************************/
import javax.swing.SwingWorker;						//subclasses by this class
import java.util.ArrayList;							//use to store list of helper threads
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


/********************************************************
 * Class BuildAssistThread
 * 
 * @purpose Assist in the building of schedules by threading
 * 
 * @see SwingWorker<Void,Void>
********************************************************/
public class BuildAssistThread extends SwingWorker<Void,Void> {
	
	
	/********************************************************
	 * The following are protected static constants for versioning
	********************************************************/
	protected final static long versionID = 2009021100023L;//file version
	
	
	/********************************************************
	 * The following are private fields for the class
	********************************************************/
	private ArrayList<int[][]> greyCodes;			//the list of grey codes for this thread to test
	private ScheduleVector result;					//the possible schedules found
	private String term;							//the term for the schedules
	private Course[] possible;						//the courses to use
	private boolean allowClosed;					//if closed courses are allowd
	private int useMin;								//the min number of courses to use
	private int permute;							//the number of combinations of schedules
	private ThreadSynch sync;						//the thread sync object
	private int[] numberSelected;					//the number of each section selected
	private boolean reportingEnabled;				//if conflict reporting is enabled

	
	@Override
	protected Void doInBackground() throws Exception {
		for(int[][]comb: greyCodes){	
			
			if(sync.isCanceled()){					//check if the operation is cancelled
				sync.allowUpdate = false;			//disallow updating the monitor
				sync.removeHelper(this);			//remove this helper from the list of helpers
				return null;						//return null as Void
			}
			
			Schedule item = new Schedule(term);
			Vector<Section> notUsed = new Vector<Section>();
			
			for(int course = 0; course < possible.length; course++){
				for(int toAdd: comb[course]){
					try{
						if(comb[course][comb[course].length-1] != possible[course].getNumOfSections()){
							Section add = possible[course].getSection(toAdd); 
							
							if(!item.add(add, allowClosed)){
								notUsed.add(add);
							}
						}
					}
					catch(Exception ex){}
				}
			}
			
			if (item.numberSections() >= useMin && item.allPrimaryUsed(sync.getPrimary()) && item.hasAllLinks(sync.getOwner().dependancy) && item.allFitTypes(sync.getType()) && item.hasAllSections(numberSelected, possible)){//verify that the minimum number of courses is satisfied
				item.reRate();					//rate the schedule before adding it
				result.addIfNew(item);			//if meets min requirement and not been added, add
			}									//to list of valid results
			else if(reportingEnabled){
				
				
				synchronized(BuildAssistThread.class){
					boolean valid = true;
					
					HashMap<String,Integer> used = new HashMap<String, Integer>();
					
					for(Course course: possible){
						used.put(course.getPerceivedCourse(), new Integer(0));
					}
					for(int combLoc = 0; combLoc < comb.length; combLoc++){
						for(int greyLoc = 0; greyLoc < comb[combLoc].length; greyLoc++){
							
							try{
								if(comb[combLoc][greyLoc] != possible[combLoc].getNumOfSections()){
									int inc = used.remove(possible[combLoc].getPerceivedCourse()).intValue() + 1;
									used.put(possible[combLoc].getPerceivedCourse(), new Integer(inc));
								}
							}
							catch(Exception ex){}
							
						}
					}
					
					int avail = 0;
					for(String key: used.keySet()){
						avail += used.get(key).intValue();
					}
					
					if(useMin > avail){
						valid = false;
					}
					
					for(String course: sync.getPrimary()){
						if(used.get(course).intValue() < 1){
							valid = false;
						}
					}
					
					for(int pos = 0; pos < numberSelected.length; pos++){
						int times = used.get(possible[pos].getPerceivedCourse()).intValue();
						if(times != 0 && times != numberSelected[pos]){
							valid = false;
						}
					}
					
					for(CourseList link: sync.getOwner().dependancy){
						boolean oneContained = false;
						boolean allContained = true;
						for(String course: link){
							if(used.get(course).intValue() > 0){
								oneContained = true;
							}
							else{
								allContained = false;
							}
						}
						
						if(oneContained && !allContained){
							valid = false;
						}
					}
					
					for(int combLoc = 0; combLoc < comb.length; combLoc++){
						for(int greyLoc = 0; greyLoc < comb[combLoc].length; greyLoc++){
							
							try{
								Section check = possible[combLoc].getSection(comb[combLoc][greyLoc]);
								
								if(!check.fitsType(sync.getType())){
									valid = false;
								}
							}
							catch (Exception ex){}
						}
					}
					
					if(valid){
						Conflict conflict = new Conflict();
						conflict.setSchedule(item);
						
						String combStr = new String("Schedule combonation: ");
						
						for(int combLoc = 0; combLoc < comb.length; combLoc++){
							for(int greyLoc = 0; greyLoc < comb[combLoc].length; greyLoc++){
								try{
									combStr += possible[combLoc].getSection(comb[combLoc][greyLoc]) + " ";
								}
								catch (Exception ex){}
							}
						}
						
						conflict.setScheduleCombonationDesc(combStr);
						
						String contStr = new String("Schedule contains: ");
						
						for(Section sec: item.getClassesObj()){
							contStr += sec + " ";
						}
						
						conflict.setScheduleContents(contStr);
												
						if(useMin > item.numberSections()){
							conflict.setCountMessage("Does not contain the minimum number of sections");
						}
						
						HashMap<String, Integer> numUsed = new HashMap<String, Integer>();
						
						for(Course course: possible){
							numUsed.put(course.getPerceivedCourse(), new Integer(0));
						}
						
						for(Section section: item.getClassesObj()){
							int contain = numUsed.remove(section.getPerceivedCourse()).intValue() + 1;
							numUsed.put(section.getPerceivedCourse(), new Integer(contain));
						}
						
						for(Section sec: notUsed){
							if(sync.getPrimary().contains(sec.getPerceivedCourse()) && numUsed.get(sec.getCourseID()).intValue() < 1){
								conflict.addPrimary(sec, item.findConflictingSection(sec));
							}
						}
						
						for(int loc = 0; loc < numberSelected.length; loc++){
							Course course = possible[loc];
							int contained = numUsed.get(course.getPerceivedCourse()).intValue();
							
							if(contained != 0 && contained != numberSelected[loc]){
								
								for(int index: comb[loc]){
									Section missing = possible[loc].getSection(index);
									
									if(notUsed.contains(missing)){
										conflict.addNumberError(missing, item.findConflictingSection(missing), numberSelected[loc]);
									}
								}
							}
						}
						
						HashMap<String, Integer> combPos = new HashMap<String, Integer>();
						
						for(int loc = 0; loc < possible.length; loc++){
							combPos.put(possible[loc].getPerceivedCourse(), new Integer(loc));
						}
						
						Vector<CourseList> usedLinks = new Vector<CourseList>();
						
						for(Section found: item.getClassesObj()){
							for(CourseList link: sync.getOwner().dependancy){
								if(usedLinks.contains(link)){
									continue;
								}
								if(link.contains(found.getPerceivedCourse())){
									boolean firstIn = true, shouldWrite = false;
									String inCourses = new String();
									Vector<Section[]> conf = new Vector<Section[]>();
									
									for(String other: link){
										if(numUsed.get(other).intValue() < 1){
											
											usedLinks.add(link);
											shouldWrite = true;
											
											int loc = combPos.get(other).intValue();
											for(int secIndex: comb[loc]){
												Section missing = possible[loc].getSection(secIndex);
												
												if(!item.contains(missing)){
													conf.add(new Section[]{missing, item.findConflictingSection(missing)});
												}
											}
										}
										else{
											inCourses += (firstIn ? "Missing Course Link: " : ", ") + other;
											firstIn = false;
										}
									}
									
									if(shouldWrite){
										conflict.addLinkError(inCourses, conf, null);
									}
								}
							}
						}
						
						for(Section not: notUsed){
							if(!conflict.hasConflict(not)){
								conflict.addMinUseError(not, item.findConflictingSection(not));
							}
						}
						
						if(Main.conflictDebugEnabled){
							System.out.println("\n\n");
							System.out.println(conflict.toTerminalString());
						}
						
						sync.addConflict(conflict);
					}
				}
			}
			
			if (sync.allowUpdate){				//check if progress monitor can be updated 
				sync.incrementProgressValue();//increment the progress synchronously
			}
			publish((Void)null);
		}
		
		return null;
	}
	
	
	
	@Override 
	protected void done(){
		sync.removeHelper(this);					//remove this helper from the list
	}

	@Override
	protected void process(List<Void> l){
		sync.updateProgressValue();
	}

	public ArrayList<int[][]> getGreyCodes() {
		return greyCodes;
	}



	public void setGreyCodes(ArrayList<int[][]> greyCodes) {
		this.greyCodes = greyCodes;
	}



	public ScheduleVector getResult() {
		return result;
	}



	public void setResult(ScheduleVector result) {
		this.result = result;
	}



	public String getTerm() {
		return term;
	}



	public void setTerm(String term) {
		this.term = term;
	}



	public Course[] getPossible() {
		return possible;
	}



	public void setPossible(Course[] possible) {
		this.possible = possible;
	}



	public boolean isAllowClosed() {
		return allowClosed;
	}



	public void setAllowClosed(boolean allowClosed) {
		this.allowClosed = allowClosed;
	}



	public int getUseMin() {
		return useMin;
	}



	public void setUseMin(int useMin) {
		this.useMin = useMin;
	}



	public int getPermute() {
		return permute;
	}



	public void setPermute(int permute) {
		this.permute = permute;
	}



	public ThreadSynch getSync() {
		return sync;
	}



	public void setSync(ThreadSynch sync) {
		this.sync = sync;
	}



	public int[] getNumberSelected() {
		return numberSelected;
	}



	public void setNumberSelected(int[] numberSelected) {
		this.numberSelected = numberSelected;
	}



	public boolean isReportingEnabled() {
		return reportingEnabled;
	}



	public void setReportingEnabled(boolean reportingEnabled) {
		this.reportingEnabled = reportingEnabled;
	}
}
