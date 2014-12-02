/**
 * @(#) JOptionPaneSelector.java
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

import javax.swing.JOptionPane;

/**
 * Term selector that prompts the user to select the term via a JOptionPane.
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public class JOptionPaneSelector extends AbstractTermSelector {

	/**
	 * Build a new JOptionPaneSelector to prompt the user for a term selection.
	 */
	public JOptionPaneSelector() {
		super();
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.retrieval.TermSelection#selectTerm(java.util.Map)
	 */
	@Override
	public Term selectTerm(Collection<Term> options) {
		Object[] optionsArr = options.toArray();
		
		setTerm((Term) JOptionPane.showInputDialog(
			null, 
			"For which term would you like to download data?", 
			"Select download term", 
			JOptionPane.QUESTION_MESSAGE, 
			null, 
			optionsArr,
			optionsArr[0]
		));
		
		return getTerm();
	}
}
