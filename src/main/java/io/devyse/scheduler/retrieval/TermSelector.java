/**
 * @(#) TermSelector.java
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

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Interface for classes that perform term selection during data retrieval
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public interface TermSelector {

	/**
	 * Select a term from the specified list of term options. This may be an automated 
	 * selection or prompt the user for input. TermSelectors are intended to be only used
	 * once.
	 * 
	 * Implementations must be thread safe and cleanly utilize the EDT. 
	 * 
	 * @param options list of term codes to term names
	 * @return the selected term
	 * @throws NoSuchElementException if the requested term is not found
	 */
	public Term selectTerm(Collection<Term> options) throws NoSuchElementException;
	
	/**
	 * Return the term previously selected by the {@link #selectTerm(Collection)} method. If the
	 * {@link #selectTerm(Collection)} method has not yet been called on this term selector, this method
	 * will return null.
	 * 
	 * @return the previously selected term, which may be null.
	 */
	public Term getTerm();
}
