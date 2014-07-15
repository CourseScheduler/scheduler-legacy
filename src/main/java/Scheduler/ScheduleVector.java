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
 * File: ScheduleVector.java
 * 
 * Contains class:
 * 
 * 		ScheduleVector:
 * 
 * 			Purpose: To provide a synchronized method for adding
 * 				to the Vector<Schedule>
 * 
 * @author Mike Reinhold
********************************************************/
package Scheduler;									//declare as member of scheduler package


/********************************************************
 * The following imports are necessary for this class
********************************************************/
import java.util.Vector;							//subclassed by this class


/********************************************************
 * Class ScheduleVector
 * 
 * @purpose Provides a synchronized method for adding to 
 * 				the underlying Vector<Schedule>
 * 
 * @see Vector<T>, Synchronized
********************************************************/
public class ScheduleVector extends Vector<Schedule> {
	
	
	/********************************************************
	 * The following are private static values for versioning
	********************************************************/
	protected final static long versionID = 2008072800003L;//class version
	protected static final long serialVersionUID = 1L + 
				Version.scheduleVect.id;			//serial version
	
	
	/********************************************************
	 * (Constructor)
	 * 
	 * @purpose provide a no-arg constructor that merely calls the super
	 * 		no-arg constructor
	********************************************************/
	public ScheduleVector(){
		super();									//call super()
	}
	
	
	/********************************************************
	 * @purpose Provides a synchronized method for adding to the
	 * 		underlying Vector<Schedule> if the Schedule is new
	 * 
	 * @param Schedule item: the schedule to be added if it is not already
	 * 			in the Vector
	 * 
	 * @see Vector<T>, Synchronized
	********************************************************/
	public synchronized void addIfNew(Schedule item){
		if(item != null){							//verify the item is not null
			boolean isContained = super.contains(item);
			
			if(!isContained){						//if not already contained
				this.add(item);						//then add the item
			}	
		}
	}
}
