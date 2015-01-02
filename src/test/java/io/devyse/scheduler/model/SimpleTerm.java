/**
 * @(#) SimpleTerm.java
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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple Term implementation for unit testing the base Term
 * methods and semantics
 *
 * @author Mike Reinhold
 * @since 4.12.8
 * 
 */
public class SimpleTerm extends AbstractTerm {

	/**
	 * Create a new SimpleTerm for the specified university using the 
	 * provided identifier
	 *
	 * @param university the parent university
	 * @param internalId the unique internal identifier
	 * @param name the external name of the university
	 */
	public SimpleTerm(University university, String internalId, String name) {
		super(university, internalId, name);
		
		//TODO ensure that the AbstractTerm is fully initialized?
		/*
		 * this.setName(id);
		 */		
	}

	@Override
	public Collection<TermDataSet> getDatasets() {
		return new ArrayList<TermDataSet>();
	}

}
