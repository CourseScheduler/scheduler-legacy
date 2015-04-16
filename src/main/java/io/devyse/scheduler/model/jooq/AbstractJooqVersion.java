/**
 * @(#) AbstractJooqVersion.java
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

import io.devyse.scheduler.model.AbstractVersion;

/**
 * Jooq specific AbstractVersion that provides the necessary logic for following the foreign key
 * relationships between the different tables in the database.
 * 
 * @author MIke Reinhold
 * @since 4.13.0
 *
 */
public abstract class AbstractJooqVersion extends AbstractVersion implements JooqPojo {

	/**
	 * Create a new AbstractJooqVersion
	 * 
	 * For use by implementation classes only
	 */
	protected AbstractJooqVersion() {
		super();
	}
}
