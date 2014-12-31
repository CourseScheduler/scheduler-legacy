/**
 * @(#) AbstractTermDataSet.java
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

/**
 * Provide basic functionality for the TermDataSet
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public abstract class AbstractTermDataSet implements TermDataSet {

	/**
	 * The Term for which this TermDataSet aggregates course data and tracks version data
	 */
	private Term term;
	
	/**
	 * The version information for this dataset
	 */
	private Version version;

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.TermDataSet#getTerm()
	 */
	@Override
	public Term getTerm() {
		return this.term;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.TermDataSet#getVersion()
	 */
	@Override
	public Version getVersion() {
		return this.version;
	}
	
	/**
	 * @param term the term to set
	 */
	protected void setTerm(Term term) {
		this.term = term;
	}

	/**
	 * @param version the version to set
	 */
	protected void setVersion(Version version) {
		this.version = version;
	}

	/**
	 * @param term
	 * @param version
	 */
	protected AbstractTermDataSet(Term term, Version version) {
		super();
		
		this.setTerm(term);
		this.setVersion(version);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return TermDataSet.toString(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other){
		if(other instanceof TermDataSet){ return this.equals((TermDataSet)other); }
		else { return super.equals(other); }
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getHashCode();
	}
}
