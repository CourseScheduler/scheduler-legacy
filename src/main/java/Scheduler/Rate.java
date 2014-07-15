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
 * File: Rate.java
 * 
 * Contains class:
 * 
 * 		Rate:
 * 
 * 			Purpose: To provide methods for rating schedules
 * 				and sections
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;							//define as member of scheduler package


/*********************************************************
 * Import ArrayList for storing sections
 * Import PriorityQueue for ordering schedules/sections
*********************************************************/
import java.util.ArrayList;					//import arraylist
import java.util.PriorityQueue;				//import priority queue


/*********************************************************
 * Class Rate:
 * 
 * @purpose provides methods for rating sections and schedules
*********************************************************/
public class Rate {
	
	
	/*********************************************************
	 * The following are private static constants in Rate
	*********************************************************/
	private final static double max = 100;			//max rating
	private final static int min = 0;				//min value
	private final static int scale = 2;				//scaling factor
	private final static int base = 15;				//base value
	private final static int percent = 100;			//percentage
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2008100100012L;//object version
	
	
	/*********************************************************
	 * @purpose Rerates a given section
	 * 
	 * @param Section item: the section to reRate
	*********************************************************/
	public static void reRate(Section item){
		Preferences prefs = Main.prefs;				//get the preferences
		
		double rating;								//create a double for the rating
		
		if (prefs.getPreferred().contains(item.getPeriodPer())){//check if section is in the preferred class hours
			rating = max;							//set to max value
		}
		else{										//otherwise get the percent outside of the preferred hours
			rating = max - item.getPeriodPer().percentOutside(prefs.getPreferred()); 
		}											//and find the complement percent
		
		if(item.hasSecondary()){
			if (prefs.getPreferred().contains(item.getSecPeriodPer())){//check if section is in the preferred class hours
				rating = max;						//set to max value
			}
			else{									//otherwise get the percent outside of the preferred hours
				rating = max - item.getSecPeriodPer().percentOutside(prefs.getPreferred()); 
			}
		}
		
		if (prefs.hasDayOff()){						//check if day off was specified
			boolean[] itemDays = item.getDaysBool();//get the sections days
			rating *= Day.values().length;			//scale by number of days
			
			for(Day count : Day.values()){			//for each day
				rating += (prefs.getDaysOff()[count.value()] && //if the day is preferred off
							itemDays[count.value()] ? min : max);//and has class set to min
			}										//else set to max
			
			rating /= (Day.values().length * scale);//average the scores again
		}
		
		if (prefs.isRateMyProfessorEnabled()){			//check if rate my professor ratings enabled
			rating += item.getInstructor().getRating();//get instructor rating and add to rating
			rating /= scale;						//re average
		}
		
		item.setRating(rating);						//set rating
	}
		
	
	/*********************************************************
	 * @purpose Rerate a schedule object
	 * 
	 * @param Schedule item: the schedule to re rate
	*********************************************************/
	public static void reRate(Schedule item){
		double rating = min;					//create and initialize to 0 the rating
		int numGaps = min;						//initialize the numberof gaps to 0
		ArrayList<Section> courses = item.getClassesObj();//get list of sections for schedule
		int num = courses.size();				//get num of sections
		Preferences prefs = Main.prefs;			//get prefs from main
		double prefWaitMin = prefs.getShortestBreak();//get the shortest break period
		double prefWaitMax = prefs.getLongestBreak();//get the longest break period
		
		for (Section section: courses){			//for each section in the schedule
			rating += section.getRating();		//sum their ratings
		}
		
		for (Day day: Day.values()){			//for each day of the week
			PriorityQueue<Section> ordered = new PriorityQueue<Section>(
					base, new SectionComparator(SectionComparator.Comparer.time));
												//create a queue for the sections based on time
			for (Section section: courses){		//for each section
				if (section.sectionMeetsOnDay(day)){//if the section meets on that day
					ordered.add(section);		//add it to the priority queue
				}
			}
			
			if (ordered.size() > 1){			//if items in the queue
				Section first = ordered.poll();	//get first item
				Section second = ordered.poll();//get second item
				
				while (second != null){			//if items pulled were good
					if(!first.getPeriodStr().equals("TBA") && !second.getPeriodStr().equals("TBA")){
						Time endFirst = first.getPeriodPer().getEndTime().clone();
						Time startSecond = second.getPeriodPer().getStartTime().clone();
													//get times that are the start and end of the break
						
						String time = new String(endFirst.toString() +//make new period string
								"-" + startSecond.toString() + 
								(startSecond.getAm() ? "am" : "pm"));
						
						Period gap = new Period(time);//create a pag period from that string
						
						Time diff = gap.getStartTime().timeDifference(gap.getEndTime());
						double wait = Time.toMinutes(diff.toString());
													//get the duration of the gap
						if (wait > prefWaitMin && wait < prefWaitMax){//check gap length
							rating += max;			//if good length, add max
							numGaps++;				//increase gaps
						}
						else if(wait > prefWaitMax){//if long gap
							wait -= prefWaitMax;	//find wait length extra
							wait /= prefWaitMax;	//find percent as decimal
							wait = max - (wait * percent);//find percent composite
							rating += wait;			//add to rating
							numGaps++;				//increase number of gaps
						}
						else{						//if short gap
							wait /= prefWaitMin;	//find decimal percent
							wait *= percent;		//find percent
							rating += wait;			//add to rating
							numGaps++;				//increase number of gaps
						}
					}	
					first = second;				//move second to first
					second = ordered.poll();	//get next item as second
				}
				
			}
		}
		
		rating /= (numGaps + num);				//re-average the rating
		item.setRating(rating);					//set rating
	}
}
