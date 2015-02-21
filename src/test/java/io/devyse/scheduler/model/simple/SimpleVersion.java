/**
 * @(#) SimpleVersion.java
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

import io.devyse.scheduler.model.AbstractVersion;
import io.devyse.scheduler.model.Version;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Random;

/**
 * Basic implementation of a Version object for use in testing base and abstract functionality
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class SimpleVersion extends AbstractVersion {

	/**
	 * Generate a new, random, simple Version using the specifiend {@link java.util.Random}
	 * as the source for the Version
	 * 
	 * @param generator the random generator to use to create a new Version
	 * 
	 * @return a new, randomized Version
	 */
	public static Version newRandomVersion(Random generator){
		return newVersionFromTimestamp(OffsetDateTime.ofInstant(
			Instant.ofEpochMilli(generator.nextLong()),
			ZoneId.systemDefault()
		));
	}
	
	/**
	 * Generate a new, simple Version using the specified timestamp
	 * 
	 * @param timestamp the OffsetDateTime to use as the timestamp
	 * 
	 * @return a new Version based on the specified offset date time
	 */
	public static Version newVersionFromTimestamp(OffsetDateTime timestamp){
		return new SimpleVersion(timestamp);
	}
	
	/**
	 * Duplicate the specified version to create a new instance using the
	 * same timestamp and uniqueness field
	 * 
	 * @param version the version upon which the new Version should be based
	 * 
	 * @return a new Version using the same timestamp as the specified version
	 */
	public static Version duplicateVersion(Version version){
		return newVersionFromTimestamp(version.getRetrievalTime());
	}
	
	/**
	 * Create a new SimpleVersion using the specified retrieval time
	 * 
	 * @param retrievalTime the date and time of the
	 */
	protected SimpleVersion(OffsetDateTime retrievalTime) {
		super(retrievalTime);
	}
}
