/**
 * @(#) StubUniversity.java
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

import io.devyse.scheduler.model.University;

/**
 * A stub of the University interface for use when testing things that depend on University, but
 * where we don't need to depend on a fully functional University.
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class StubUniversity extends StubClass<University, StubUniversity> implements University {

	/**
	 * Generate a new stubbed University instance randomly using the specified {@link java.util.Random}
	 * as the source of the instance uniqueness
	 * 
	 * @param generator the source of the random uniqueness
	 * 
	 * @return a new, random, stubbed University
	 */
	public static University newRandomUniversity(Random generator){
		return new StubUniversity(generator.nextInt());
	}
	
	/**
	 * Generate a new stubbed University instance using the specified value as the instance
	 * uniqueness
	 * 
	 * @param value the instance uniqueness value
	 * 
	 * @return a new, static, stubbed University
	 */
	public static University newStaticUniversity(int value){
		return new StubUniversity(value);
	}
	
	/**
	 * Construct a new stubbed Version using the specified integer for uniqueness
	 * 
	 * @param value the instance uniqueness value
	 */
	protected StubUniversity(int value) {
		super(value);
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.University#getName()
	 */
	@Override
	public String getName() {
		return null;
	}

}
