/**
 * @(#) Valuable.java
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

/**
 * Stubbed classes must have some uniqueness factor in order to satisfy basic
 * requirements for being part of a test harness for the unit under test (such
 * as necessary for {@link java.lang.Object#equals(Object)} and other core methods.
 * 
 * Valuable provides the uniqueness needed for the StubClass base class
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
interface Valuable {

	/**
	 * Return the instance uniqueness value used by this stubbed class
	 * 
	 * @return the instance uniqueness value
	 */
	public int getValue();
}
