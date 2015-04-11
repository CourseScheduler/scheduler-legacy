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
package io.devyse.scheduler.model.simple;

import io.devyse.scheduler.model.AbstractDateTimeBlock;
import io.devyse.scheduler.model.DateTimeBlock;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Random;

/**
 * Simple implementation of DateTimeBlock via AbstractDateTimeBlock to unit test the base functionality
 * of the DateTimeBlock interface and AbstractDateTimeBlock class (constructors, equals, hashCode,
 * compareTo, duration, etc)
 *
 * @author Mike Reinhold
 * @since 4.13.0
 * 
 */
public class SimpleDateTimeBlock extends AbstractDateTimeBlock{

	/**
	 * Day of the week for the time block
	 */
	private DayOfWeek dayOfWeek;
	
	/**
	 * Start time and timezone for the time block
	 */
	private LocalTime startTime;
	
	/**
	 * End time and timezone for the time block
	 */
	private LocalTime endTime;
	
	/**
	 * Time zone offset for the time block
	 */
	private ZoneOffset zoneOffset;
	
	/**
	 * Start date for the time block
	 */
	private LocalDate startDate;
	
	/**
	 * End date for the time block
	 */
	private LocalDate endDate;
	
	
	/**
	 * Generate a DateTimeBlock based on the current state of a Random
	 *
	 * @param generator a Random for use in building the DateTimeBlocks
	 * 
	 * @return the next DateTimeBlock
	 */
	public static DateTimeBlock newRandomDateTimeBlock(Random generator){
		return new SimpleDateTimeBlock(
				DayOfWeek.of(Math.abs(generator.nextInt() % DayOfWeek.values().length)+1),
				LocalTime.of(Math.abs(generator.nextInt(24)), 
						Math.abs(generator.nextInt(60)), Math.abs(generator.nextInt(60)), 
						Math.abs(generator.nextInt(1000000000))),
				LocalTime.of(Math.abs(generator.nextInt(24)), 
						Math.abs(generator.nextInt(60)), Math.abs(generator.nextInt(60)), 
						Math.abs(generator.nextInt(1000000000))),
				ZoneOffset.ofHours(generator.nextInt(19)),
				LocalDate.ofEpochDay(Math.abs(generator.nextInt(1000000000))),
				LocalDate.ofEpochDay(Math.abs(generator.nextInt(1000000000)))
		);
	}
	
	/**
	 * Generate a DateTimeBlock based on the provided day, times, dates, and timezone
	 * 
	 * @param dow the day of the week
	 * @param startTime the local start time
	 * @param endTime the local end time
	 * @param zone the time zone reference for the local times
	 * @param startDate the start date
	 * @param endDate the end date
	 * 
	 * @return a new simple DateTimeBlock instance
	 */
	public static DateTimeBlock newDateTimeBlock(DayOfWeek dow, LocalTime startTime, LocalTime endTime, ZoneOffset zone, LocalDate startDate, LocalDate endDate){
		return new SimpleDateTimeBlock(dow, startTime, endTime, zone, startDate, endDate);
	}
	
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
		super();
		this.dayOfWeek = dow;
		this.startTime = startTime;
		this.endTime = endTime;
		this.zoneOffset = zone;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getDayOfWeek()
	 */
	public DayOfWeek getDayOfWeek() {
		return this.dayOfWeek;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getZoneOffset()
	 */
	public ZoneOffset getZoneOffset(){
		return this.zoneOffset;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getStartDate()
	 */
	public LocalDate getStartDate() {
		return this.startDate;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getEndDate()
	 */
	public LocalDate getEndDate() {
		return this.endDate;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getLocalStartTime()
	 */
	public LocalTime getLocalStartTime() {
		return this.getStartTime();
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getLocalEndTime()
	 */
	public LocalTime getLocalEndTime() {
		return this.getEndTime();
	}
	
	/**
	 * @return the local start time
	 */
	protected LocalTime getStartTime(){
		return this.startTime;
	}
	
	/**
	 * @return the local end time
	 */
	protected LocalTime getEndTime(){
		return this.endTime;
	}
	
}
