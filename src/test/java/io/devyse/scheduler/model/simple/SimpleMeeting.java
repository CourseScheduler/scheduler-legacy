/**
 * @(#) SimpleMeeting.java
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
package io.devyse.scheduler.model.simple;

import java.util.Random;
import java.util.Set;

import io.devyse.scheduler.model.AbstractMeeting;
import io.devyse.scheduler.model.DateTimeBlock;
import io.devyse.scheduler.model.Instructor;
import io.devyse.scheduler.model.Meeting;
import io.devyse.scheduler.model.Section;
import io.devyse.scheduler.model.stub.StubDateTimeBlock;
import io.devyse.scheduler.model.stub.StubSection;

/**
 * Simple implementation of Meeting via AbstractMeeting to unit test the base functionality
 * of the Meeting interface and AbstractMeeting class (constructors, equals, hashCode,
 * compareTo, etc)
 *
 * @author Mike Reinhold
 * @since 4.13.0
 * 
 */
public class SimpleMeeting extends AbstractMeeting {

	/**
	 * The date and time information (start and end time, time zone, date range)
	 */
	private DateTimeBlock dateTimeBlock;

	/**
	 * The parent section (registration unit)
	 */
	private Section section;
	
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
	private Set<Instructor> instructors;
	
	/**
	 * Generate a Meeting based on the current state of a Random
	 *
	 * @param generator a Random for use in building the Meeting
	 * @return the next Meeting
	 */
	public static Meeting newRandomMeeting(Random generator){
		return new SimpleMeeting(
				StubSection.newRandomSection(generator), 
				StubDateTimeBlock.newRandomDateTimeBlock(generator)
		);
	}
	
	/**
	 * Generate a new Meeting using the specified Section and DateTimeBlock 
	 * 
	 * @param parent the Section which contains the Meeting
	 * @param dateTimeBlock the dDateTimeBlock for the Meeting
	 * 
	 * @return a new simple Meeting
	 */
	public static Meeting newMeeting(Section parent, DateTimeBlock dateTimeBlock){
		return new SimpleMeeting(parent, dateTimeBlock);
	}
	
	/**
	 * Create a new SimpleMeeting with the specified parent Section and
	 * DateTimeBlock
	 *
	 */
	protected SimpleMeeting(Section parent, DateTimeBlock dateTimeBlock) {
		super();
		this.section = parent;
		this.dateTimeBlock = dateTimeBlock;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.AbstractMeeting#getDateTimeBlock()
	 */
	public DateTimeBlock getDateTimeBlock() {
		return dateTimeBlock;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.AbstractMeeting#getSection()
	 */
	public Section getSection() {
		return section;
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
	public Set<Instructor> getInstructors() {
		return this.instructors;
	}
}
