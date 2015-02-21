/**
 * @(#) StubTermDataSet.java
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

import io.devyse.scheduler.model.Term;
import io.devyse.scheduler.model.TermDataSet;
import io.devyse.scheduler.model.Version;

/**
 * A stub of the TermDataSet interface for use when testing things that depend on TermDataSet, but
 * where we don't need to depend on a fully functional TermDataSet.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class StubTermDataSet extends StubClass<TermDataSet, StubTermDataSet> implements TermDataSet {

	/**
	 * Generate a new stubbed TermDataSet instance randomly using the specified {@link java.util.Random}
	 * as the source of the instance uniqueness
	 * 
	 * @param generator the source of the random uniqueness
	 * 
	 * @return a new, random, stubbed TermDataSet
	 */
	public static TermDataSet newRandomTermDataSet(Random generator){
		return new StubTermDataSet(generator.nextInt());
	}
	
	/**
	 * Generate a new stubbed TermDataSet instance using the specified uniqueness value
	 * 
	 * @param value the instance uniqueness value
	 * 
	 * @return a new, stubbed TermDataSet
	 */
	public static TermDataSet newTermDataSet(int value){
		return new StubTermDataSet(value);
	}
	
	/**
	 * Create a new stubbed TermDataSet using the specified integer value as it's uniqueness
	 * 
	 * @param value the instance uniqueness value
	 */
	protected StubTermDataSet(int value){
		super(value);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.TermDataSet#getTerm()
	 */
	@Override
	public Term getTerm() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.TermDataSet#getVersion()
	 */
	@Override
	public Version getVersion() {
		return null;
	}
}
