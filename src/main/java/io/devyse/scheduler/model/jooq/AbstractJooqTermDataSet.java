/**
 * @(#) AbstractJooqTermDataSet.java
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

import io.devyse.scheduler.model.AbstractTermDataSet;
import io.devyse.scheduler.model.Term;
import io.devyse.scheduler.model.Version;

/**
 * Jooq specific AbstractTermDataSet that provides the necessary logic for following the foreign key
 * relationships between the different tables in the database.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public abstract class AbstractJooqTermDataSet extends AbstractTermDataSet implements JooqPojo, JooqVersionFK, JooqTermFK {
		
	/**
	 * Construct a new AbstractJooqTermDataSet
	 * 
	 * For use by implementation classes only
	 */
	public AbstractJooqTermDataSet() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.TermDataSet#getTerm()
	 */
	@Override
	public Term getTerm() {
		return this.getTermByFK();
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.TermDataSet#getVersion()
	 */
	@Override
	public Version getVersion() {
		return this.getVersionByFK();
	}
}
