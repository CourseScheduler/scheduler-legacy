/**
 * @(#) AbstractMeeting.java
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

import java.util.Set;

/**
 * Provide basic functionality for implementations of
 * the Meeting interface
 *
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public abstract class AbstractMeeting implements Meeting {
	
	/**
	 * The date and time information (start and end time, time zone, date range)
	 */
	private DateTimeBlock dateTimeBlock;
	
	/**
	 * The campus portion of the meeting location
	 */
	private String campus;
	
	/**
	 * The building on campus
	 */
	private String building;
	
	/**
	 * The room within the building
	 */
	private String room;
	
	/**
	 * A description of the type of meeting
	 */
	private String meetingType;
	
	/**
	 * Type of meeting for scheduling purposes
	 */
	private String scheduleType;
	
	/**
	 * Instructors which participate in the meeting
	 */
	private Set<String> instructors;
	
	/**
	 * The parent section (registration unit)
	 */
	private Section section;
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getDateTimeBlock()
	 */
	@Override
	public DateTimeBlock getDateTimeBlock() {
		return this.dateTimeBlock;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getCampus()
	 */
	@Override
	public String getCampus() {
		return this.campus;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getBuilding()
	 */
	@Override
	public String getBuilding() {
		return this.building;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getRoom()
	 */
	@Override
	public String getRoom() {
		return this.room;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getMeetingType()
	 */
	@Override
	public String getMeetingType() {
		return this.meetingType;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getScheduleType()
	 */
	@Override
	public String getScheduleType() {
		return this.scheduleType;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getInstructors()
	 */
	@Override
	public Set<String> getInstructors() {
		return this.instructors;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getSection()
	 */
	@Override
	public Section getSection() {
		return this.section;
	}

	/**
	 * @param dateTimeBlock the timeBlock to set
	 */
	protected void setDateTimeBlock(DateTimeBlock dateTimeBlock) {
		this.dateTimeBlock = dateTimeBlock;
	}

	/**
	 * @param campus the campus to set
	 */
	protected void setCampus(String campus) {
		this.campus = campus;
	}

	/**
	 * @param building the building to set
	 */
	protected void setBuilding(String building) {
		this.building = building;
	}

	/**
	 * @param room the room to set
	 */
	protected void setRoom(String room) {
		this.room = room;
	}

	/**
	 * @param meetingType the meetingType to set
	 */
	protected void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	/**
	 * @param scheduleType the scheduleType to set
	 */
	protected void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	/**
	 * @param instructors the instructors to set
	 */
	protected void setInstructors(Set<String> instructors) {
		this.instructors = instructors;
	}

	/**
	 * @param section the section to set
	 */
	protected void setSection(Section section) {
		this.section = section;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Meeting) return this.equals((Meeting)other);
		else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		return this.getHashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Meeting.toString(this);
	}

	/**
	 * Create a new AbstractMeeting for the specified Section in the 
	 * specified DateTimeBlock
	 *
	 * @param parent the parent Section to which the Meeting belongs
	 * @param timeBlock the date and time of the Meeting
	 */
	protected AbstractMeeting(Section parent, DateTimeBlock timeBlock){
		super();
		
		this.setSection(parent);
		this.setDateTimeBlock(timeBlock);
	}
}
