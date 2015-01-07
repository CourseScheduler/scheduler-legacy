/**
 * @(#) AbstractInstructor.java
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
 * Provide the basic and default implementation of the instructor interface
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public abstract class AbstractInstructor implements Instructor {

	/**
	 * The instructor's name as presented by the university
	 */
	private String name;
	
	/**
	 * The dataset of the Term which contains courses that reference the Instructor
	 */
	private TermDataSet termDataSet;
	
	/**
	 * Construct a new abstract instructor using the specified name and term data set
	 * 
	 * @param termDataSet the dataset for the term containing the instructor
	 * @param name the instructor's name
	 */
	protected AbstractInstructor(TermDataSet termDataSet, String name) {
		super();
		
		this.setName(name);
		this.setTermDataSet(termDataSet);
	}

	/**
	 * Set the instructor name
	 * 
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the TermDataSet which contains this instructor
	 * 
	 * @param termDataSet the termDataSet to set
	 */
	protected void setTermDataSet(TermDataSet termDataSet) {
		this.termDataSet = termDataSet;
	}

	/**
	 * @return the instructor's name as formatted by the source university
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the termDataSet the data set from the term that contains the instructor
	 */
	public TermDataSet getTermDataSet() {
		return termDataSet;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		return this.getHashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other){
		if(other instanceof Instructor){ return this.isEqual((Instructor)other); }
		else { return super.equals(other); }
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return Instructor.toString(this);
	}
}
