/**
 * @(#) StubInstructor.java
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

import java.util.Random;

import io.devyse.scheduler.model.Instructor;
import io.devyse.scheduler.model.TermDataSet;

/**
 * A stub of the Instructor interface for use when testing things that depend on Instructor, but
 * where we don't need to depend on a fully functional Instructor.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class StubInstructor extends StubClass<Instructor, StubInstructor> implements Instructor {

	/**
	 * Generate a new stubbed Instructor instance randomly using the specified {@link java.util.Random}
	 * as the source of the instance uniqueness
	 * 
	 * @param generator the source of the random uniqueness
	 * 
	 * @return a new, random, stubbed Instructor
	 */
	public static Instructor newRandomInstructor(Random generator){
		return new StubInstructor(generator.nextInt());
	}
	
	/**
	 * Construct a new stubbed Instructor using the specified integer for uniqueness
	 * 
	 * @param value the instance uniqueness value
	 */
	protected StubInstructor(int value) {
		super(value);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Instructor#getName()
	 */
	@Override
	public String getName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Instructor#getTermDataSet()
	 */
	@Override
	public TermDataSet getTermDataSet() {
		return null;
	}
}
