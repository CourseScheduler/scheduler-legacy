/**
 * @(#) Term.java
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

import java.util.Objects;

/**
 * Interface describing a registration term.
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public interface Term extends Comparable<Term>{

	/**
	 * The unique term identifier for this term as defined by
	 * the university. Often a numeric representation of the
	 * year and semester (201402)
	 *
	 * @return the term identifier
	 * 
	 * @since 4.12.8
	 */
	public String getInternalId();
	
	/**
	 * The common name of the term as defined by the university.
	 * Often a "plain language" representation of the year and
	 * semester (Spring 2014)
	 *
	 * @return the term name
	 */
	public String getName();
	
	/**
	 * The university for which this registration term is valid
	 *
	 * @return the university for this term
	 * 
	 * @since 4.12.8
	 */
	public University getUniversity();
	
	//TODO
	/*
	 * public List<TermDataset> getDatasets() 
	 */	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public default boolean equals(Term other) {
		return	this.getUniversity().equals(other.getUniversity()) &&
				this.getInternalId().equals(other.getInternalId())
		;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public default int getHashCode() {
		return Objects.hash(
				this.getUniversity(),
				this.getInternalId()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public default int compareTo(Term other) {
		int result = this.getUniversity().compareTo(other.getUniversity());
		
		if(result == 0){
			result = this.getInternalId().compareTo(other.getInternalId());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(Term term){
		StringBuilder sb = new StringBuilder();
		
		sb.append(term.getName());
		sb.append(" (");
		sb.append(term.getInternalId());
		sb.append(") - ");
		sb.append(term.getUniversity());
		
		return sb.toString();
	}
}
