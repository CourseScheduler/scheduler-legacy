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
package io.devyse.scheduler.model;

/**
 * Basic implementation of a TermDataSet for use in testing base and abstract functionality
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class SimpleTermDataSet extends AbstractTermDataSet {

	/**
	 * Construct a new simple term data set for testing base and abstract functionality
	 * 
	 * @param term the Term for which this dataset contains course and instructor data
	 * @param version the version information for the dataset
	 */
	protected SimpleTermDataSet(Term term, Version version) {
		super(term, version);
	}
}
