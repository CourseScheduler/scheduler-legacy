/**
 * @(#) StubVersion.java
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

import java.time.OffsetDateTime;
import java.util.Random;

import io.devyse.scheduler.model.Version;

/**
 * A stub of the Version interface for use when testing things that depend on Version, but
 * where we don't need to depend on a fully functional Version.
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class StubVersion extends StubClass<Version, StubVersion> implements Version {

	/**
	 * Generate a new stubbed Version instance randomly using the specified {@link java.util.Random}
	 * as the source of the instance uniqueness
	 * 
	 * @param generator the source of the random uniqueness
	 * 
	 * @return a new, random, stubbed Version
	 */
	public static Version newRandomVersion(Random generator){
		return new StubVersion(generator.nextInt());
	}
	
	/**
	 * Generate a new stubbed Version instance using the specified uniqueness value
	 * 
	 * @param value the instance uniqueness value
	 * 
	 * @return a new, stubbed Version
	 */
	public static Version newVersion(int value){
		return new StubVersion(value);
	}
	
	/**
	 * Construct a new stubbed Version using the specified integer for uniqueness
	 * 
	 * @param value the instance uniqueness value
	 */
	protected StubVersion(int value) {
		super(value);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Version#getRetrievalTime()
	 */
	@Override
	public OffsetDateTime getRetrievalTime() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Version#isComplete()
	 */
	@Override
	public boolean isComplete() {
		return false;
	}
}
