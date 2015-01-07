/**
 * @(#) StubTerm.java
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

import java.util.Collection;
import java.util.Random;

import io.devyse.scheduler.model.Term;
import io.devyse.scheduler.model.TermDataSet;
import io.devyse.scheduler.model.University;

/**
 * A stub of the Term interface for use when testing things that depend on Term, but
 * where we don't need to depend on a fully functional Term.
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class StubTerm extends StubClass<Term, StubTerm> implements Term {

	/**
	 * Generate a new stubbed Term Randomly using the specified {@link java.util.Random}
	 * as the source for the instance uniqueness value
	 * 
	 * @param generator the source of the random uniqueness
	 * 
	 * @return a new, random, stubbed Term
	 */
	public static Term newRandomTerm(Random generator){
		return new StubTerm(generator.nextInt());
	}
	
	/**
	 * Construct a new stubbed Term using the specified integer for uniqueness
	 * 
	 * @param value the instance uniqueness value
	 */
	protected StubTerm(int value) {
		super(value);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getTermId()
	 */
	@Override
	public String getTermId() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getName()
	 */
	@Override
	public String getName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getUniversity()
	 */
	@Override
	public University getUniversity() {
		return null;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getDatasets()
	 */
	@Override
	public Collection<TermDataSet> getDatasets() {
		return null;
	}
}
