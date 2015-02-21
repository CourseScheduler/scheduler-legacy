/**
 * @(#) Universities.java
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
 * Utility classes and constructor methods for Universities
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class Universities {

	/**
	 * Create a new University with the specified name
	 * 
	 * @param name common name of the university
	 * 
	 * @return the new university
	 */
	public University newUniversity(String name){
		return new TransientUniversity(name);
	}
	
	/**
	 * Transient university class which is a placeholder until persistence based classes are available
	 * 
	 * @author Mike Reinhold
	 * @since 4.13.0
	 *
	 */
	private static class TransientUniversity extends AbstractUniversity{

		/**
		 * Create a new placeholder university
		 * 
		 * @param name the university name
		 */
		public TransientUniversity(String name) {
			super(name);
		}
		
	}
}
