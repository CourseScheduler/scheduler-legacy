/**
 * @(#) StubDateTimeBlock.java
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

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Random;

import io.devyse.scheduler.model.DateTimeBlock;

/**
 * A stub of the DateTimeBlock interface for use when testing things that depend on DateTimeBlock, but
 * where we don't need to depend on a fully functional DateTimeBlock.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class StubDateTimeBlock extends StubClass<DateTimeBlock, StubDateTimeBlock> implements DateTimeBlock {

	/**
	 * Generate a new stubbed DateTimeBlock instance randomly using the specified {@link java.util.Random}
	 * as the source of the instance uniqueness
	 * 
	 * @param generator the source of the random uniqueness
	 * 
	 * @return a new, random, stubbed DateTimeBlock
	 */
	public static DateTimeBlock newRandomDateTimeBlock(Random generator){
		return new StubDateTimeBlock(generator.nextInt());
	}
	
	/**
	 * Generate a new stubbed DateTimeBlock instance using the specified value as the instance
	 * uniqueness
	 * 
	 * @param value the instance uniqueness value
	 * 
	 * @return a new, static, stubbed DateTimeBlock
	 */
	public static DateTimeBlock newStaticDateTimeBlock(int value){
		return new StubDateTimeBlock(value);
	}
	
	/**
	 * Construct a new stubbed DateTimeBlock using the specified integer for uniqueness
	 * 
	 * @param value the instance uniqueness value
	 */
	protected StubDateTimeBlock(int value) {
		super(value);
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getDayOfWeek()
	 */
	@Override
	public DayOfWeek getDayOfWeek() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getOffsetStartTime()
	 */
	@Override
	public OffsetTime getOffsetStartTime() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getLocalStartTime()
	 */
	@Override
	public LocalTime getLocalStartTime() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getOffsetEndTime()
	 */
	@Override
	public OffsetTime getOffsetEndTime() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getLocalEndTime()
	 */
	@Override
	public LocalTime getLocalEndTime() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getZoneOffset()
	 */
	@Override
	public ZoneOffset getZoneOffset() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getDuration()
	 */
	@Override
	public Duration getDuration() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getStartDate()
	 */
	@Override
	public LocalDate getStartDate() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getEndDate()
	 */
	@Override
	public LocalDate getEndDate() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getPeriod()
	 */
	@Override
	public Period getPeriod() {
		return null;
	}
}
