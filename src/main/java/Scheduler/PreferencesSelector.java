/**
 * @(#) PreferencesSelector.java
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
package Scheduler;

import io.devyse.scheduler.retrieval.AbstractTermSelector;

import java.util.Collection;

/**
 * Term selector that uses the currentTerm value in the user Preferences
 * 
 * @author Mike Reinhold
 *
 */
public class PreferencesSelector extends AbstractTermSelector {

	/**
	 * Build a new PreferencesSelector
	 */
	public PreferencesSelector() {
		super();
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.retrieval.TermSelector#selectTerm(java.util.Collection)
	 */
	@Override
	public io.devyse.scheduler.model.Term selectTerm(Collection<io.devyse.scheduler.model.Term> options) {
		String termString = Main.prefs.getCurrentTerm();
		
		for(io.devyse.scheduler.model.Term term: options){
			if(term.getTermId().equals(termString)){
				setTerm(term);		//set and return current preferences term
				return getTerm();	
			}
		}
		
		return getTerm();	//return default value
	}
}
