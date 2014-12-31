/**
 * @(#) AbstractSection.java
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
 * Provide the basic functionality and implementation of
 * the Section interface
 *
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public abstract class AbstractSection implements Section {
	
	/**
	 * The course request number, registration number
	 */
	private String crn;
	
	/**
	 * The course identifier
	 */
	private String courseNumber;
	
	/**
	 * The section description (course description)
	 */
	private String description;
	
	/**
	 * The common section name
	 */
	private String name;
	
	/**c
	 * The unique section identifier
	 */
	private String sectionNumber;
	
	/**
	 * The registration term for this section
	 */
	private Term term;
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getCRN()
	 */
	@Override
	public String getCRN() {
		return this.crn;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getCourseId()
	 */
	@Override
	public String getCourseNumber() {
		return this.courseNumber;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getSectionId()
	 */
	@Override
	public String getSectionNumber() {
		return this.sectionNumber;
	}
	
	//TODO replace with getTermDataSet()? 
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Section#getTerm()
	 */
	@Override
	public Term getTerm() {
		return this.term;
	}
	
	/**
	 * @param crn the crn to set
	 */
	protected void setCrn(String crn) {
		this.crn = crn;
	}

	/**
	 * @param courseId the courseId to set
	 */
	protected void setCourseNumber(String courseId) {
		this.courseNumber = courseId;
	}

	/**
	 * @param description the description to set
	 */
	protected void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * @param sectionId the sectionId to set
	 */
	protected void setSectionNumber(String sectionId) {
		this.sectionNumber = sectionId;
	}

	/**
	 * @param term the term to set
	 */
	protected void setTerm(Term term) {
		this.term = term;
	}

	/**
	 * Create a new AbstractSection for the specified term using the
	 * provided course request number
	 * 
	 * @param term the term in which the section is available
	 * @param crn the registration number for the course
	 * @param courseId the course identifier
	 * @param sectionId the section identifier
	 */
	protected AbstractSection(Term term, String crn, String courseId, String sectionId){
		super();
		
		this.setTerm(term);
		this.setCrn(crn);
		this.setCourseNumber(courseId);
		this.setSectionNumber(sectionId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Section){ return this.equals((Section)obj); }
		else return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Section.toString(this);
	}
	
}
