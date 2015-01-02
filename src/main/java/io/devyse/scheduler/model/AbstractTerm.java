/**
 * @(#) AbstractTerm.java
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
 * Basic term implementation containing the term id and a user friendly name.
 * 
 * The basic term is not associated with any specific university
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public abstract class AbstractTerm implements Term {
	
	/**
	 * Term identifier, usually a code defining the year and semester
	 */
	private String termId;

	/**
	 * Term name, usually text declaring the year and semester
	 */
	private String name;
	
	/**
	 * University for which this term is related
	 */
	private University university;
	
	/**
	 * Build an term based on the id and name
	 *
	 * @param university the university to which the term is related
	 * @param internalId the internal name of the term as used by the university
	 * @param name the external name of the term as used by the university
	 */
	protected AbstractTerm(University university, String internalId, String name) {
		super();
		this.setTermId(internalId);
		this.setName(name);
		this.setUniversity(university);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return Term.toString(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other){
		if(other instanceof Term){return equals((Term)other);}
		else{return super.equals(other);}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getHashCode();
	}
	
	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getId()
	 */
	@Override
	public String getTermId() {
		return this.termId;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getUniversity()
	 */
	@Override
	public University getUniversity() {
		return university;
	}
	
	/**
	 * @param id the id to set
	 */
	protected void setTermId(String id) {
		this.termId = id;
	}

	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param university the university to set
	 */
	protected void setUniversity(University university){
		this.university = university;
	}
}
