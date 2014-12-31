/**
 * @(#) Terms.java
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
 * Utility and constructor method class
 * 
 * @author Mike Reinhold
 * 
 * @since 4.12.8
 *
 */
public class Terms {
	
	/**
	 * Construct a new Term for the specified university using the internal id and external 
	 * name.
	 * 
	 * @param university the university for which the term is valid
	 * @param internalId the internal name of the term used by the university
	 * @param name the external name of the term used by the university
	 * 
	 * @return the new term 
	 */
	public static Term newTerm(University university, String internalId, String name){
		return new TransientTerm(university, name, name);
	}
	
	/**
	 * The TransientTerm is a placeholder class that will be used until terms can be created
	 * based on the persistence model
	 * 
	 * @author Mike Reinhold
	 * @since 4.12.8
	 *
	 */
	private static class TransientTerm extends AbstractTerm{

		/**
		 * Create new placeholder Term
		 * 
		 * @param university the university for which the term is valid
		 * @param internalId the internal name of the term used by the university
		 * @param name the external name of the term used by the university
		 */
		protected TransientTerm(University university, String internalId, String name) {
			super(university, internalId, name);
		}

		@Override
		public Collection<TermDataSet> getDatasets() {
			// TODO Auto-generated method stub
			return new ArrayList<TermDataSet>();	//TODO this is only temporary
		}		
	}
}
