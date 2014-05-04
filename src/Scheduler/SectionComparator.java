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
 * File: ScheduleComparator.java
 * 
 * Contains classes:
 * 
 * 		SectionComparator:
 * 
 * 			Purpose: To compare sections based on user choice at
 * 				contruction time
 * 
 *  	Comparer:
 *  		
 *  		Purpose: To enumerate the compare methods
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;

/********************************************************
 * Import Comparator class for implementation
********************************************************/
import java.util.Comparator;

/********************************************************
 * Class: SectionComparator:
 * 
 * @purpose Compares sections based on the selected compare
 * 		method
 * 
 * @see Comparator<E>
********************************************************/
public class SectionComparator implements Comparator<Section>{
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2008043000007L;//object version
	
	
	/********************************************************
	 * The following is a public enum in SectionComparator for
	 * 		enumerating the compare methods
	********************************************************/
	public enum Comparer{
		
		
		/********************************************************
		 * The following are the enumerator values
		********************************************************/
		standard,							//standard compareTo method								
		time,								//use the compareTime method
		rating;								//use the compareRating method
	}
	
	
	/********************************************************
	 * The following are the private fields of the SectionComparator
	********************************************************/
	private Comparer use;					//the compare method to use
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose Create the comparator
	 * 
	 * @param Comparer use: the compare method to use
	********************************************************/
	public SectionComparator(Comparer use){
		this.use = use;						//set the compare method
	}
	
	
	/********************************************************
	 * @purpose execute the comparison
	 * 
	 * @param Section a: the first section in the comparison
	 * @param Section b: the second section in the comparison
	 * 
	 * @return int: the result of the comparison as Compare.(result).value()
	********************************************************/
	public int compare(Section a, Section b){
		if(this.use == Comparer.standard) {
			return a.compareTo(b);			//if standard, use compareTo
		}
		else if(this.use == Comparer.rating) {
			return a.compareRating(b);		//if rating, use compareRating
		}
		else {
			return a.compareTime(b);		//if time, use compareTime
		}
	}
}
