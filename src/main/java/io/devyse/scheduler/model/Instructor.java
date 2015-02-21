/**
 * @(#) Instructor.java
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
 * Represent an instructor that teaches a course at a university
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public interface Instructor extends Comparable<Instructor> {
	
	/**
	 * Retrieve the instructor name
	 * 
	 * @return the instructor name
	 */
	public String getName();
	
	/**
	 * The Instructor is tied to the Term dataset which contains the course data which references
	 * this instructor.
	 * 
	 * @return the dataset of the term which contains the Instructor
	 */
	public TermDataSet getTermDataSet();
	
	//TODO other methods?

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public default boolean isEqual(Instructor other) {
		return	this.getTermDataSet().equals(other.getTermDataSet()) &&
				this.getName().equals(other.getName())
		;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public default int getHashCode() {
		return Objects.hash(
			this.getTermDataSet(),
			this.getName()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public default int compareTo(Instructor other) {
		int result = this.getTermDataSet().compareTo(other.getTermDataSet());
		
		if(result == 0){
			result = this.getName().compareTo(other.getName());
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(Instructor instructor){
		StringBuilder sb = new StringBuilder();
		
		sb.append(instructor.getTermDataSet());
		sb.append(" - ");
		sb.append(instructor.getName());
		
		return sb.toString();
	}
}
