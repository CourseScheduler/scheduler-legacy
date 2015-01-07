/**
 * @(#) StubMeeting.java
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
package io.devyse.scheduler.model.stub;

import java.util.Random;
import java.util.Set;

import io.devyse.scheduler.model.DateTimeBlock;
import io.devyse.scheduler.model.Instructor;
import io.devyse.scheduler.model.Meeting;
import io.devyse.scheduler.model.Section;

/**
 * A stub of the Meeting interface for use when testing things that depend on Meeting, but
 * where we don't need to depend on a fully functional Meeting.
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class StubMeeting extends StubClass<Meeting, StubMeeting> implements Meeting {

	/**
	 * Generate a new stubbed Meeting instance randomly using the specified {@link java.util.Random}
	 * as the source of the instance uniqueness
	 * 
	 * @param generator the source of the random uniqueness
	 * 
	 * @return a new, random, stubbed Meeting
	 */
	public static Meeting newRandomMeeting(Random generator){
		return new StubMeeting(generator.nextInt());
	}
	
	/**
	 * Construct a new stubbed Meeting using the specified integer for uniqueness
	 * 
	 * @param value the instance uniqueness value
	 */
	protected StubMeeting(int value){
		super(value);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getDateTimeBlock()
	 */
	@Override
	public DateTimeBlock getDateTimeBlock(){
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getCampus()
	 */
	@Override
	public String getCampus() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getBuilding()
	 */
	@Override
	public String getBuilding() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getRoom()
	 */
	@Override
	public String getRoom() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getMeetingType()
	 */
	@Override
	public String getMeetingType() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getScheduleType()
	 */
	@Override
	public String getScheduleType() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getInstructors()
	 */
	@Override
	public Set<Instructor> getInstructors() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getSection()
	 */
	@Override
	public Section getSection() {
		return null;
	}
}
