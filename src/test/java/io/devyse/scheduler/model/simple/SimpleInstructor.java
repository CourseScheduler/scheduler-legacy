/**
 * @(#) SimpleInstructor.java
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

import io.devyse.scheduler.model.AbstractInstructor;
import io.devyse.scheduler.model.Instructor;
import io.devyse.scheduler.model.TermDataSet;
import io.devyse.scheduler.model.stub.StubTermDataSet;

/**
 * A simple implementation of the instructor interface for testing the base and abstract
 * functionality.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class SimpleInstructor extends AbstractInstructor {

	/**
	 * Generate a new Instructor randomly using the specified {@link java.util.Random}
	 * 
	 * @param generator the random source for the construction of the Instructor
	 * 
	 * @return a new, random instructor
	 */
	public static Instructor newRandomInstructor(Random generator){
		return new SimpleInstructor(
			StubTermDataSet.newRandomTermDataSet(generator),
			Long.toHexString(generator.nextLong())
		);
	}
	
	/**
	 * Generate a new Instructor using the specified TermDataSet and name
	 * 
	 * @param dataset the term dataset containing the instructor
	 * @param name the instructor's name
	 * 
	 * @return a new, simple Instructor
	 */
	public static Instructor newInstructor(TermDataSet dataset, String name){
		return new SimpleInstructor(dataset, name);
	}
	
	/**
	 * Create a new Simple instructor for testing base and abstract functionality
	 * 
	 * @param dataset the term dataset containing the instructor
	 * @param name the instructor's name
	 */
	protected SimpleInstructor(TermDataSet dataset, String name) {
		super(dataset, name);
	}
}
