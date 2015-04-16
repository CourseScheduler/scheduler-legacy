/**
 * @(#) SimpleTermDataSet.java
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

import io.devyse.scheduler.model.AbstractTermDataSet;
import io.devyse.scheduler.model.Term;
import io.devyse.scheduler.model.TermDataSet;
import io.devyse.scheduler.model.Version;
import io.devyse.scheduler.model.stub.StubTerm;
import io.devyse.scheduler.model.stub.StubVersion;

/**
 * Basic implementation of a TermDataSet for use in testing base and abstract functionality
 * for the TermDataSet
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class SimpleTermDataSet extends AbstractTermDataSet {

	/**
	 * The Term for which this TermDataSet aggregates course data and tracks version data
	 */
	private Term term;
	
	/**
	 * The version information for this dataset
	 */
	private Version version;
	
	/**
	 * Generate a new simple TermDataSet using the specified {@link java.util.Random}
	 * as the source for the TermDataSet generation
	 * 
	 * @param generator the random source for the TermDataSet
	 * 
	 * @return a new, random, simple TermDataSet
	 */
	public static TermDataSet newRandomTermDataSet(Random generator){
		return new SimpleTermDataSet(
			StubTerm.newRandomTerm(generator),
			StubVersion.newRandomVersion(generator)
		);
	}
	
	/**
	 * Generate a new simple TermDataSet based on the specified Term and Version
	 * 
	 * @param term the Term for which this dataset contains course and instructor data
	 * @param version the version information for the dataset
	 * 
	 * @return a new simple TermDataSet
	 */
	public static TermDataSet newTermDataSet(Term term, Version version){
		return new SimpleTermDataSet(term, version);
	}
	
	/**
	 * Construct a new simple term data set for testing base and abstract functionality
	 * 
	 * @param term the Term for which this dataset contains course and instructor data
	 * @param version the version information for the dataset
	 */
	protected SimpleTermDataSet(Term term, Version version) {
		super();
		
		this.term = term;
		this.version = version;
	}
	

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.TermDataSet#getTerm()
	 */
	@Override
	public Term getTerm() {
		return this.term;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.TermDataSet#getVersion()
	 */
	@Override
	public Version getVersion() {
		return this.version;
	}
}
