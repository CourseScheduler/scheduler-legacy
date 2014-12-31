/**
 * @(#) AbstractDateTimeBlock.java
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneOffset;

/**
 * Provide basic functionality for implementations of the DateTimeBlock
 * interface.
 *
 * @author Mike Reinhold
 * @since 4.12.8
 */
public abstract class AbstractDateTimeBlock implements DateTimeBlock{
	
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
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getDayOfWeek()
	 */
	public DayOfWeek getDayOfWeek() {
		return this.dayOfWeek;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getOffsetStartTime()
	 */
	public OffsetTime getOffsetStartTime() {
		//TODO precalculate this result? 
		return OffsetTime.of(getLocalStartTime(), getZoneOffset());
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getLocalStartTime()
	 */
	public LocalTime getLocalStartTime() {
		return this.getStartTime();
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getOffsetEndTime()
	 */
	public OffsetTime getOffsetEndTime() {
		return OffsetTime.of(getLocalEndTime(), getZoneOffset());
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getLocalEndTime()
	 */
	public LocalTime getLocalEndTime() {
		return this.getEndTime();
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getZoneOffset()
	 */
	public ZoneOffset getZoneOffset(){
		return this.zoneOffset;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getDuration()
	 */
	public Duration getDuration() {
		return Duration.between(this.getOffsetStartTime(), this.getOffsetEndTime());
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
	 * @see io.devyse.scheduler.model.DateTimeBlock#getPeriod()
	 */
	public Period getPeriod() {
		return Period.between(this.getStartDate(), this.getEndDate());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return DateTimeBlock.toString(this);
	}

	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	protected void setDayOfWeek(DayOfWeek day) {
		this.dayOfWeek = day;
	}

	/**
	 * @param start the startTime to set
	 */
	protected void setStartTime(LocalTime start) {
		this.startTime = start;
	}

	/**
	 * @param end the endTime to set
	 */
	protected void setEndTime(LocalTime end) {
		this.endTime = end;
	}
	
	/**
	 * @param start the startDate to set
	 */
	protected void setStartDate(LocalDate start) {
		this.startDate = start;
	}
	
	/**
	 * @param end the endDate to set
	 */
	protected void setEndDate(LocalDate end) {
		this.endDate = end;
	}
	
	/**
	 * @param zone the zoneOffset to set
	 */
	protected void setZoneOffset(ZoneOffset zone){
		this.zoneOffset = zone;
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
	
	/**
	 * Create a new AbstractDateTimeBlock with the appropriate day of week, start time,
	 * end time, and timezone.
	 *
	 * @param dow the day of the week
	 * @param startTime the local start time, uses the zone as a reference
	 * @param endTime the local end time, uses the zone as a reference
	 * @param zone the time zone, likely based on campus location
	 * @param startDate the start date 
	 * @param endDate the end date
	 */
	protected AbstractDateTimeBlock(DayOfWeek dow, LocalTime startTime, LocalTime endTime, ZoneOffset zone, LocalDate startDate, LocalDate endDate) {
		super();
		
		this.setDayOfWeek(dow);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setZoneOffset(zone);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {										
		if(other instanceof DateTimeBlock) return this.equals((DateTimeBlock)other);
		else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getHashCode();
	}
}
