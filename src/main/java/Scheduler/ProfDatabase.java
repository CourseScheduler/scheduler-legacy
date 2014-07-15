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
 * File: ProfDatabase.java
 * 
 * Contains classes:
 * 
 * 		ProfDatabase:
 * 
 * 			Purpose: To encapsulate store the prof information
 * 				while providing synchronized methods as necessary
 * 
 * @author Mike Reinhold
*********************************************************/
package Scheduler;							//declare as member of scheduler package


/*********************************************************
 * The following imports are necessary for this wrapper class
*********************************************************/
import java.util.TreeMap;


/*********************************************************
 * Class ProfDatabase
 * 
 * @purpose Wrap the data structure for the profs and provide
 * 		synchronized access where necessary
*********************************************************/
public class ProfDatabase extends TreeMap<String,Prof> {
	
	
	/********************************************************
	 * UPDATE SERIAL VERSION IN VERSION WHEN THIS FILE CHANGES
	********************************************************/
	protected final static long versionID = 2008071600002L;	//file version
	protected final static long serialVersionUID = Version.profDatabase.id + 1L;
											//object version
	
	/********************************************************
	 * The following are private fields of the class
	********************************************************/
	private boolean ratings = false;		//if the profs have ratings
	
	
	/*********************************************************
	 * (Constructor)
	 * 
	 * @purpose calls the super() and sets if this has ratings
	 * 
	 * @param boolean hasRatings: if the profs have ratings stored
	*********************************************************/
	public ProfDatabase(boolean hasRatings){
		super();							//call super's no-arg constructor
		ratings = hasRatings;				//set ratings appropriately
	}
	
	
	/*********************************************************
	 * @purpose Adds the prof ifthe prof is new
	 * 
	 * @param Prof value: the prof to attempt to add
	 * 
	 * @see synchronized
	*********************************************************/
	public synchronized void addIfNew(Prof value){
		if(!this.containsKey(value.getName())){//check if prof already added
			this.put(value.getName(), value);//add it if not
		}
	}

	
	/*********************************************************
	 * @purpose return if the profs have ratings
	 * 
	 * @return boolean: if the profs in this database are rated
	*********************************************************/
	public boolean hasRatings() {
		return ratings;
	}

	
	/*********************************************************
	 * @purpose sets if the profs in this database are rated
	 * 
	 * @param boolean ratings: whether the profs are rated or not
	*********************************************************/
	public void setRatings(boolean ratings) {
		this.ratings = ratings;
	}
}
