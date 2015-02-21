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

import io.devyse.scheduler.model.AbstractMeeting;
import io.devyse.scheduler.model.DateTimeBlock;
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
		super(parent, dateTimeBlock);
	}	
}
