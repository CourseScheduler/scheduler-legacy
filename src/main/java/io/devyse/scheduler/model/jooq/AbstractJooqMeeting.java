/**
 * @(#) AbstractJooqMeeting.java
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
package io.devyse.scheduler.model.jooq;

import java.util.HashSet;
import java.util.Set;

import io.devyse.scheduler.model.AbstractMeeting;
import io.devyse.scheduler.model.DateTimeBlock;
import io.devyse.scheduler.model.Instructor;
import io.devyse.scheduler.model.Section;
import io.devyse.scheduler.model.jooq.tables.daos.InstructorDao;
import io.devyse.scheduler.model.jooq.tables.daos.MeetingInstructorDao;

/**
 * Jooq specific AbstractMeeting that provides the necessary logic for following the foreign key
 * relationships between the different tables in the database.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public abstract class AbstractJooqMeeting extends AbstractMeeting implements JooqPojo, JooqSectionFK, JooqDateTimeBlockFK {

	/**
	 * DAOs for accessing the instructors corresponding to this Meeting
	 * via the MeetingInstructor mapping table
	 */
	protected MeetingInstructorDao meetingInstructorDao = new MeetingInstructorDao();
	protected InstructorDao instructorDao = new InstructorDao();
	
	/**
	 * Create a new AbstractJooqMeeting for integrating between the JooqMeeting and the 
	 * data model.
	 * 
	 * For use by implementation classes only.
	 */
	protected AbstractJooqMeeting() {
		super();
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getDateTimeBlock()
	 */
	@Override
	public DateTimeBlock getDateTimeBlock() {
		return this.getDateTimeBlockByFK();
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getSection()
	 */
	@Override
	public Section getSection() {
		return this.getSectionByFK();
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Meeting#getInstructors()
	 */
	@Override
	public Set<Instructor> getInstructors() {
		
		//use the MeetingInstructor mapping table to find the instructor ids associated with this meeting
		Integer[] instructorIds = meetingInstructorDao.fetchByMeetingId(this.getId())
			.parallelStream().map(
				meetingInstructor -> meetingInstructor.getInstructorId()
			).toArray(
				size -> new Integer[size]
			);
		
		//get the specific instructors from the list of ids
		return new HashSet<>(instructorDao.fetchById(instructorIds));
	}
}
