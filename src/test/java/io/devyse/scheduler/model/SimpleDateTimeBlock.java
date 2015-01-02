/**
 * @(#) SimpleDateTimeBlock.java
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * Simple implementation of DateTimeBlock via AbstractDateTimeBlock to unit test the base functionality
 * of the DateTimeBlock interface and AbstractDateTimeBlock class (constructors, equals, hashCode,
 * compareTo, duration, etc)
 *
 * @author Mike Reinhold
 *
 */
public class SimpleDateTimeBlock extends AbstractDateTimeBlock{

	/**
	 * Create a new SimpleDateTimeBlock using local times and a specific time zone
	 * 
	 * @param dow the day of the week
	 * @param startTime the local start time
	 * @param endTime the local end time
	 * @param zone the time zone reference for the local times
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	protected SimpleDateTimeBlock(DayOfWeek dow, LocalTime startTime, LocalTime endTime, ZoneOffset zone, LocalDate startDate, LocalDate endDate) {
		super(dow, startTime, endTime, zone, startDate, endDate);
	}
}
