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

/**
 * Interface for classes that perform term selection during data retrieval
 * 
 * @author Mike Reinhold
 *
 */
public interface TermSelector {

	/**
	 * Select a term from the specified list of term options. This may be an automated 
	 * selection or prompt the user for input.
	 * 
	 * Implementations must be thread safe and cleanly utilize the EDT
	 * 
	 * @param options list of term codes to term names
	 * @return the selected term
	 */
	public Term selectTerm(Collection<Term> options);
}
