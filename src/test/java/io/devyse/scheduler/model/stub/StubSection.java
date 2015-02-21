/**
 * @(#) StubSection.java
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

import io.devyse.scheduler.model.Section;
import io.devyse.scheduler.model.TermDataSet;


/**
 * A stub of the Section interface for use when testing things that depend on Section, but
 * where we don't need to depend on a fully functional Section.
 *
 * @author Mike Reinhold
 * @since 4.13.0
 */
public class StubSection extends StubClass<Section, StubSection> implements Section {

	/**
	 * Generate a new stubbed Section instance randomly using the specified {@link java.util.Random}
	 * as the source of the instance uniqueness
	 * 
	 * @param generator the source of the random uniqueness
	 * 
	 * @return a new, random, stubbed Section
	 */
	public static Section newRandomSection(Random generator){
		return new StubSection(generator.nextInt());
	}
	
	/**
	 * Generate a new stubbed Section instance using the specified value as the instance
	 * uniqueness
	 * 
	 * @param value the instance uniqueness value
	 * 
	 * @return a new, static, stubbed Section
	 */
	public static Section newStaticSection(int value){
		return new StubSection(value);
	}
	
	/**
	 * Construct a new stubbed Section using the specified integer for uniqueness
	 * 
	 * @param value the instance uniqueness value
	 */
	protected StubSection(int value) {
		super(value);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getCRN()
	 */
	@Override
	public String getCRN() {
		return "";
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getCourseID()
	 */
	@Override
	public String getCourseNumber() {
		return "";
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getDescription()
	 */
	@Override
	public String getDescription() {
		return "";
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getName()
	 */
	@Override
	public String getName() {
		return "";
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getSectionID()
	 */
	@Override
	public String getSectionNumber() {
		return "";
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getTermDataSet()
	 */
	@Override
	public TermDataSet getTermDataSet() {
		return null;
	}
}
