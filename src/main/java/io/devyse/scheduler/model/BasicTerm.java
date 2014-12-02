/**
 * @(#) BasicTerm.java
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
 * Basic term implementation containing the term id and a user friendly name
 * 
 * @author Mike Reinhold
 * @since 4.12.4
 */
public class BasicTerm implements Term {

	/**
	 * Term identifier, usually a code defining the year and semester
	 */
	private String id;

	/**
	 * Term name, usually text declaring the year and semester
	 */
	private String name;
	
	/**
	 * Build an term based on the id and name
	 */
	public BasicTerm(String id, String name) {
		super();
		this.setId(id);
		this.setName(name);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param id the id to set
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Term other) {
		return this.getId().equals(other.getId())
		;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int getHashCode() {
		return Objects.hash(
				this.getId()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Term other) {
		int result = this.getId().compareTo(other.getId());
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.getName());
		
		return sb.toString();
	}
}
