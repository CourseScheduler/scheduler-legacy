/**
 * @(#) AbstractVersion.java
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

/**
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public abstract class AbstractVersion implements Version {

	/**
	 * If this version was fully and successfully retrieved
	 */
	private boolean complete;
	
	/**
	 * The date and time of the retrieval
	 */
	private OffsetDateTime retrievalTime;


	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Version#getRetrievalTime()
	 */
	@Override
	public OffsetDateTime getRetrievalTime() {
		return this.retrievalTime;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Version#isSuccess()
	 */
	@Override
	public boolean isComplete() {
		return this.complete;
	}

	/**
	 * @param complete the complete to set
	 */
	protected void setComplete(boolean complete) {
		this.complete = complete;
	}

	/**
	 * @param retrievalTime the retrievalTime to set
	 */
	protected void setRetrievalTime(OffsetDateTime retrievalTime) {
		this.retrievalTime = retrievalTime;
	}
	
	/**
	 * @param retrievalTime
	 */
	protected AbstractVersion(OffsetDateTime retrievalTime) {
		super();
		
		this.setRetrievalTime(retrievalTime);
		this.setComplete(false);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object){
		if(object instanceof Version) { return this.equals((Version)object); }
		else { return super.equals(object); }
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return Version.toString(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getHashCode();
	}
}
