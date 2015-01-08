/**
 * @(#) SimpleUniversity.java
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

import java.util.Random;

import io.devyse.scheduler.model.AbstractUniversity;
import io.devyse.scheduler.model.University;

/**
 * Simple University implementation for unit testing the basic
 * functionality and default implementation
 *
 * @author Mike Reinhold
 * @since 4.12.8
 * 
 */
public class SimpleUniversity extends AbstractUniversity {
	
	/**
	 * Generate a new simple University using the specified {@link java.util.Random}
	 * as the source for generating the University
	 * 
	 * @param generator the source for the random generation of the University
	 * 
	 * @return a new, random, simple University
	 */
	public static University newRandomUniversity(Random generator){
		return new SimpleUniversity(
			Long.toHexString(generator.nextLong())
		);
	}
	
	/**
	 * Generate a new simple University using the specified name
	 * 
	 * @param name the name of the university
	 * 
	 * @return a new simple University
	 */
	public static University newUniversity(String name){
		return new SimpleUniversity(name);
	}
	
	/**
	 * Create a new SimpleUniversity using the specified name
	 *
	 * @param name the name of the university
	 */
	protected SimpleUniversity(String name) {
		super(name);
	}
}
