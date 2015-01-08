/**
 * @(#) SimpleSection.java
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

import io.devyse.scheduler.model.AbstractSection;
import io.devyse.scheduler.model.Section;
import io.devyse.scheduler.model.TermDataSet;
import io.devyse.scheduler.model.TermDataSetUnitTest;

/**
 * Provide a basic implementation of the Section interface
 * for testing the base method functionality and semantics
 *
 * @author Mike Reinhold
 * @since 4.12.8
 * 
 */
public class SimpleSection extends AbstractSection {

	/**
	 * Generate a Section based on the current state of a Random
	 *
	 * @param generator a Random for use in building the Section
	 * @return the next Section
	 */
	public static Section newRandomSection(Random generator){
		return new SimpleSection(
				TermDataSetUnitTest.generateTermDataSet(generator),
				Long.toHexString(generator.nextLong()),
				Long.toHexString(generator.nextLong()), 
				Long.toHexString(generator.nextLong())
		);
	}
	
	/**
	 * Generate a new simple Section based on the specified TermDataSet and associated details
	 * 
	 * @param termDataSet the dataset of the term in which the section is available
	 * @param crn the registration number for the section
	 * @param courseId the course identifier
	 * @param sectionId the section identifier
	 * 
	 * @return a new simple Section
	 */
	public static Section newSection(TermDataSet termDataSet, String crn, String courseId, String sectionId){
		return new SimpleSection(termDataSet, crn, courseId, sectionId);
	}
	
	/**
	 * Create a SimpleSection for the specified term using the
	 * provided registration number and details
	 *
	 * @param termDataSet the dataset of the term in which the section is available
	 * @param crn the registration number for the section
	 * @param courseId the course identifier
	 * @param sectionId the section identifier
	 */
	protected SimpleSection(TermDataSet termDataSet, String crn, String courseId, String sectionId) {
		super(termDataSet, crn, courseId, sectionId);
	}	
}
