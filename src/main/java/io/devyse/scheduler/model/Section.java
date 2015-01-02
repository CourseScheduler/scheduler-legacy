/**
 * @(#) Section.java
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
 * Represent the registration unit of a given course.
 *
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public interface Section extends Comparable<Section> {
		
	/**
	 * The CRN is the course registration number that is used
	 * by the registrar to identify a specific Section during
	 * registration. CRNs are generally only unique within
	 * a registration term
	 *
	 * @return the course registration number
	 */
	public String getCRN();
	
	/**
	 * The Course Number/Identifier is the common code for the course, 
	 * often represented by a department character sequence and
	 * a number (aka, "BIO 101" or "CE-210"). The exact format of 
	 * the course identifier may vary significantly
	 *
	 * @return the course identifier
	 */
	public String getCourseNumber();

	/**
	 * A description of the content of the course, often includes
	 * details about the topics of study
	 *
	 * @return the course description
	 */
	public String getDescription();
	
	/**
	 * The name of the course, for example "Computing and Algorithms
	 * III"
	 *
	 * @return the course name
	 */
	public String getName();
	
	/**
	 * The section number/identifier which uniquely identifies the section
	 * within the course (aka: CE-210-01 where CE-210 is the course 
	 * id and 01 is the section identifier). Usually numeric though 
	 * may include characters to identify labs versus seminars versus
	 * other types of sections. 
	 *
	 * @return the section identifier
	 */
	public String getSectionNumber();
	
	/**
	 * The term dataset which contains this section
	 *
	 * @return the data set for the registration term which contains this section
	 */
	public TermDataSet getTermDataSet();
	
	
	//TODO seat availability, other fields?
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public default boolean equals(Section other) {
		return	this.getTermDataSet().equals(other.getTermDataSet()) &&
				this.getCRN().equals(other.getCRN())
		;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public default int getHashCode() {
		return Objects.hash(
				this.getTermDataSet(),
				this.getCRN()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public default int compareTo(Section other) {
		int result = this.getTermDataSet().compareTo(other.getTermDataSet());
		
		if(result == 0){
			//slightly different than the equals or hashCode because we would want to sort
			//by course ID and section ID, not CRN
			result = this.getCourseNumber().compareTo(other.getCourseNumber());
			if(result == 0){
				result = this.getSectionNumber().compareTo(other.getSectionNumber());
			}
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(Section section){
		StringBuilder sb = new StringBuilder();
		
		sb.append(section.getCourseNumber());
		sb.append("-");
		sb.append(section.getSectionNumber());
		sb.append(" (");
		sb.append(section.getName());
		sb.append("");
		
		return sb.toString();
	}
}
