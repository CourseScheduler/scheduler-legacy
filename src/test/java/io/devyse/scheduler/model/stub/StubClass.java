/**
 * @(#) StubClass.java
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
package io.devyse.scheduler.model.stub;

import java.util.Objects;

/**
 * The StubClass abstract base class provides default functionality for stub instances of a 
 * data model class. This is used to provide simple mechanisms for testing basic object functionality
 * such as object sameness/equality, comparability, and hashcode viability on other objects without
 * relying on a tested dependency. This is used to simplify mocking of those test harness classes.
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public abstract class StubClass<T, U extends Valuable> implements Valuable {
	
	/**
	 * The uniqueness value for the stubbed class used for equality checks,
	 * comparisons, and for generation of the hashcode
	 */
	private int value;
	
	/**
	 * Construct a new instance of this stubbed class using the specified instance uniqueness
	 * value
	 * 
	 * @param value the instance uniqueness value
	 */
	public StubClass(int value){
		super();
		
		this.setValue(value);
	}
	
	/**
	 * @param value the new instance uniqueness value
	 */
	protected void setValue(int value){
		this.value = value;
	}
	
	/**
	 * @return the instance uniqueness value
	 */
	public int getValue(){
		return this.value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		return Objects.hash(this.getValue());
	}
	
	/**
	 * Provide an instance specific compareTo method following the necessary signature of 
	 * {@link java.lang.Comparable} for the generic type.
	 * 
	 * Same semantics as for {@link java.lang.Comparable#compareTo(Object)}
	 * 
	 * @param t the instance to compare to this instance
	 * @return whether this instance is less than, greater than, or equal to the provided instance
	 */
	@SuppressWarnings("unchecked")
	public int compareTo(T t){
		return compareTo((U)t);
	}
	
	/**
	 * Custom compareTo implementation that provides naive comparison for stubbed classes via the
	 * @{link io.devyse.scheduler.model.stub.Valuable} interface
	 * 
	 * Same semantics as for {@link java.lang.Comparable#compareTo(Object)}
	 * 
	 * @param o the instance to compare to this instance
	 * @return whether this instance is less than, greater than, or equal to the provided instance
	 */
	public int compareTo(U o){
		return Integer.compare(this.getValue(), o.getValue());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o){
		if(o instanceof Valuable){ return this.getValue() == ((U)o).getValue(); }
		else { return super.equals(o); }
	}
	
	/**
	 * Provide an instance type specific isEqual method for satisfying
	 * data model classes that define their equality function as a default interface
	 * method with this signature.
	 * 
	 * Same semantics as for {@link java.lang.Object#equals(Object)
	 * 
	 * @param t other object to check for equality with this instance
	 * @return if this instance is equal to the provided instance
	 */
	public boolean isEqual(T t){
		return this.equals(t);
	}
}
