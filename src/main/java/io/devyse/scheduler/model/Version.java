/**
 * @(#) Version.java
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

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public interface Version extends Comparable<Version>{

	/**
	 * Get the date and time at which this version was retrieved
	 * 
	 * @return the retrieval date and time
	 */
	public OffsetDateTime getRetrievalTime();
	
	/**
	 * Get the status of this version, whether the retrieval was fully completed
	 * 
	 * @return if the version is complete and and usable
	 */
	public boolean isComplete();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public default boolean isEqual(Version other){
		return 	this.getRetrievalTime().equals(other.getRetrievalTime()) &&
				this.isComplete() == other.isComplete()
		;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public default int compareTo(Version other){
		int result = this.getRetrievalTime().compareTo(other.getRetrievalTime());
		
		if(result == 0){
			result = Boolean.valueOf(isComplete()).compareTo(other.isComplete());
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public default int getHashCode(){
		return Objects.hash(
			this.getRetrievalTime(),
			this.isComplete()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(Version version){
		return Version.toString(version, DateTimeFormatter.ISO_INSTANT);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(Version version, DateTimeFormatter formatter){
		StringBuffer sb = new StringBuffer();
		
		sb.append(version.getRetrievalTime().format(formatter));
		sb.append(" (");
		sb.append(version.isComplete() ? "Complete" : "Pending");
		sb.append("");
		
		return sb.toString();
	}
}
