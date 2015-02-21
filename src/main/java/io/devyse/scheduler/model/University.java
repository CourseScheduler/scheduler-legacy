/**
 * @(#) University.java
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
 * Represent the University for which course data can or has been
 * downloaded and schedules can be generated. 
 *
 * @author Mike Reinhold
 * @since 4.13.0
 */
public interface University extends Comparable<University>{

	/**
	 * The common name of the university, for instance "Kettering University"
	 *
	 * @return university name
	 */
	public String getName();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public default boolean isEqual(University other) {
		return this.getName().equals(other.getName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public default int getHashCode() {
		return Objects.hash(
			this.getName()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public default int compareTo(University other) {
		return this.getName().compareTo(other.getName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(University uni){
		return uni.getName();
	}
}
