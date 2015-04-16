/**
 * @(#) JooqDateTimeBlockFK.java
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

import io.devyse.scheduler.model.DateTimeBlock;
import io.devyse.scheduler.model.jooq.tables.daos.DateTimeBlockDao;

/**
 * Interface to define standard foreign key access to the DateTimeBlock table
 * from any other table that has a foreign key for DateTimeBlock
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public interface JooqDateTimeBlockFK {
	
	/**
	 * @return the foreign key value for the DateTimeBlock corresponding to this object
	 */
	public Integer getDateTimeBlockId();

	/**
	 * Find the DateTimeBlock associated with this Jooq object by accessing the DateTimeBlock
	 * table using the foreign key from this Jooq object
	 * 
	 * @return the associated DateTimeBlock instance via foreign key relationship
	 */
	public default DateTimeBlock getDateTimeBlockByFK() {
		return  new DateTimeBlockDao().fetchOneById(this.getDateTimeBlockId());
	}
}
