/**
 * @(#) Meeting.java
 *
 * This file is part of the Course Scheduler, an open source, cross platform
 * course scheduling tool, configurable for most universities.
 *
 * Copyright (C) 2010-2014 Devyse.io; All rights reserved.
 *
 * @license GNU General Public License version 3 (GPLv3)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package io.devyse.scheduler.model;

import java.util.Objects;
import java.util.Set;

/**
 * Represent a specific instance of when a Section meets. Contains
 * a DateTimeBlock as well as the location information (campus, building,
 * room), the instructor, and the type of meeting (lab vs lecture vs other).
 *
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public interface Meeting extends Comparable<Meeting> {
	
	/**
	 * The time DateTimeBlock in which the Meeting occurs. This method
	 * returns null if the Meeting time DateTimeBlock is not yet announced
	 *
	 * @return the time DateTimeBlock for this meeting
	 */
	public DateTimeBlock getDateTimeBlock();
	
	/**
	 * The name or address of the campus at which the Meeting occurs.
	 * This method returns null if the Meeting location is not yet
	 * announced
	 *
	 * @return the campus portion of the location
	 */
	public String getCampus();		//ANALYZE should campus be up a level?
	
	/**
	 * The name or address of the building at which the Meeting
	 * occurs. This method returns null if the Meeting location
	 * is not yet announced
	 *
	 * @return the building portion of the location
	 */
	public String getBuilding();
	
	/**
	 * The name or number of the room in which this Meeting occurs.
	 * This method returns null if the Meeting location is not
	 * yet announced
	 *
	 * @return the building room portion of the location
	 */
	public String getRoom();
	
	/**
	 * A description of the type of meeting, whether a lecture, lab,
	 * discussion, seminar, etc. This Meeting type is strictly for
	 * informational purposes, not used in the scheduling algorithm
	 * in anyway
	 *
	 * @return the type of Meeting
	 */
	public String getMeetingType();
	
	/**
	 * A description of the type of schedule item for the Meeting. Schedule
	 * type varies greatly between universities. This schedule type is for 
	 * both informational and scheduling purposes.
	 *
	 * @return type of scheduling for the Meeting
	 */
	public String getScheduleType();
	
	
	/**
	 * A meeting may have one or more instructors
	 *
	 * @return the set of instructors for this meeting
	 */
	public Set<Instructor> getInstructors();
	
	/**
	 * Meetings are specific to a single Section of a course
	 *
	 * @return the Section containing this Meeting
	 */
	public Section getSection();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public default boolean equals(Meeting other) {
		return 	this.getSection().equals(other.getSection()) &&
				this.getDateTimeBlock().equals(other.getDateTimeBlock())
		;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public default int getHashCode() {
		return Objects.hash(
			this.getSection(),
			this.getDateTimeBlock()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public default int compareTo(Meeting other) {
		int value = this.getSection().compareTo(other.getSection());
		
		if(value == 0) {
			value = this.getDateTimeBlock().compareTo(other.getDateTimeBlock());
		}
		
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(Meeting meeting){
		StringBuilder sb = new StringBuilder();
		
		sb.append(meeting.getCampus());
		sb.append("-");
		sb.append(meeting.getBuilding());
		sb.append("-");
		sb.append(meeting.getRoom());
		sb.append(" ");
		sb.append(meeting.getDateTimeBlock());
		
		return sb.toString();
	}
}
