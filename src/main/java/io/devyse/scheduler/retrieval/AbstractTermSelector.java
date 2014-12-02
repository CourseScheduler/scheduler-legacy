/**
 * @(#) AbstractTermSelector.java
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

package io.devyse.scheduler.retrieval;

import io.devyse.scheduler.model.Term;

/**
 * Abstract term selector providing basic functionality for implementing more complex
 * term selectors.
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public abstract class AbstractTermSelector implements TermSelector {

	/**
	 * The term that has been previously selected, or null if {@link #selectTerm(Collection)} 
	 * has not been called
	 */
	private Term term;
	
	/**
	 * Construct a new AbstractTermSelector and initialize it for selecting a term during
	 * course data download
	 */
	public AbstractTermSelector() {
		super();
		
		term = null; 			//term must be null initially
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.retrieval.TermSelector#getTerm()
	 */
	@Override
	public Term getTerm() {
		return term;
	}

	/**
	 * Store the term for future recall via {@link #getTerm()}
	 * 
	 * @param term the term previously selected in {@link #selectTerm(Collection)}
	 */
	protected void setTerm(Term term){
		this.term = term;
	}
}
