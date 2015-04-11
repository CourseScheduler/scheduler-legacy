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

import java.time.Duration;
import java.time.OffsetTime;
import java.time.Period;

/**
 * Provide basic functionality for implementations of the DateTimeBlock
 * interface.
 *
 * @author Mike Reinhold
 * @since 4.13.0
 */
public abstract class AbstractDateTimeBlock implements DateTimeBlock{
		
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getOffsetStartTime()
	 */
	public OffsetTime getOffsetStartTime() {
		//TODO precalculate this result? 
		return OffsetTime.of(getLocalStartTime(), getZoneOffset());
	}
		
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getOffsetEndTime()
	 */
	public OffsetTime getOffsetEndTime() {
		return OffsetTime.of(getLocalEndTime(), getZoneOffset());
	}
		
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.DateTimeBlock#getDuration()
	 */
	public Duration getDuration() {
		return Duration.between(this.getOffsetStartTime(), this.getOffsetEndTime());
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
	 * Create a new AbstractDateTimeBlock
	 *
	 * For use by implementation classes only
	 */
	protected AbstractDateTimeBlock() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {										
		if(other instanceof DateTimeBlock) return this.isEqual((DateTimeBlock)other);
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
