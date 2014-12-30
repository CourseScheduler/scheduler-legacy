/**
 * @(#) StaticSelector.java
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
 * Selects from the list of available terms statically based on the provided termId during
 * construction
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public class StaticSelector extends AbstractTermSelector {

	/**
	 * The static term ID requested during construction
	 */
	private String termId;

	/**
	 * Create a new static selector that will select from the available term options
	 * using the specified term by id if available. If not in the list of available
	 * terms, {@link #selectTerm(Collection)} will return the default term.
	 * 
	 * @param termId the term to use during selection, based on id
	 */
	public StaticSelector(String termId) {
		super();
		
		setTermId(termId);
	}

	/**
	 * @param termId the term to use during selection, based on id
	 */
	protected void setTermId(String termId) {
		this.termId = termId;
	}
	
	/**
	 * @return the termId The statically requested term by Id 
	 */
	protected String getTermId() {
		return termId;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.retrieval.TermSelector#selectTerm(java.util.Collection)
	 */
	@Override
	public Term selectTerm(Collection<Term> options) {
		
		for(Term term: options){
			if(term.getInternalId().equals(getTermId())){
				setTerm(term);		//set and return preselected term
				return getTerm();	
			}
		}
		
		return getTerm();			//return default term
	}

}
