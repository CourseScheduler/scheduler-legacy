/**
 * @(#) TermDataSet.java
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
 * A TermDataSet represents a specific instance of a Term (retrieved at a specific point in time)
 * which is referenced by all of the retrieved data
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public interface TermDataSet extends Comparable<TermDataSet>{
	
	/**
	 * The Term for which this TermDataSet was retrieved
	 * 
	 * @return the associated term
	 */
	public Term getTerm();
	
	/**
	 * The Version containing the retrieval date and time
	 * 
	 * @return the version of the TermDataSet
	 */
	public Version getVersion();
	
	//TODO course access methods
	/*
	 * map<String(Course_Number), Section>
	 * map<String(Department), Map<String(Course_Number), Section>>
	 * others?
	 */

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public default boolean equals(TermDataSet other) {
		return 	this.getTerm().isEqual(other.getTerm()) &&
				this.getVersion().isEqual(other.getVersion())
		;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public default int getHashCode() {
		return Objects.hash(
			this.getTerm(),
			this.getVersion()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public default int compareTo(TermDataSet other) {
		int result = this.getTerm().compareTo(other.getTerm());
		
		if(result == 0){
			result = this.getVersion().compareTo(other.getVersion());
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(TermDataSet term){
		StringBuffer sb = new StringBuffer();
		
		sb.append(term.getTerm().toString());
		sb.append(" - ");
		sb.append(term.getVersion().toString());
		
		return sb.toString();
	}
}
