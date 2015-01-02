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
package io.devyse.scheduler.model;

/**
 * Simple implementation of Meeting via AbstractMeeting to unit test the base functionality
 * of the Meeting interface and AbstractMeeting class (constructors, equals, hashCode,
 * compareTo, etc)
 *
 * @author Mike Reinhold
 * @since 4.12.8
 * 
 */
public class SimpleMeeting extends AbstractMeeting {
	
	/**
	 * Create a new SimpleMeeting with the specified parent Section and
	 * DateTimeBlock
	 *
	 */
	public SimpleMeeting(Section parent, DateTimeBlock dateTimeBlock) {
		super(parent, dateTimeBlock);
		
		//TODO ensure that the AbstractMeeting is fully initialized?
		/*
		 * this.setCampus("campus");
		 * this.setBuilding("building");
		 * this.setRoom("room");
		 * this.setMeetingType("meeting type");
		 * this.setScheduleType("schedule type");
		 * this.setInstructors(new HashSet<>());
		 */
	}	
}
