/**
 * @(#) JooqSectionFK.java
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

import io.devyse.scheduler.model.Section;
import io.devyse.scheduler.model.jooq.tables.daos.JooqSectionDao;

/**
 * Interface to define standard foreign key access to the section table
 * from any other table that has a foreign key for Section
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public interface JooqSectionFK {
	
	/**
	 * @return the foreign key value for the section corresponding to this object
	 */
	public Integer getSectionId();

	/**
	 * Find the Section associated with this Jooq object by accessing the Section
	 * table using the foreign key from this Jooq object
	 * 
	 * @return the associated Section instance via foreign key relationship
	 */
	public default Section getSectionByFK() {
		return new JooqSectionDao().fetchOneById(this.getSectionId());
	}
}
