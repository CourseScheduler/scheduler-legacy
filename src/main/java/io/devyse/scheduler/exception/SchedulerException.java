/**
 * @(#) SchedulerException.java
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
package io.devyse.scheduler.exception;

/**
 * Abstract Application layer exception class.
 * 
 * This class and subclasses should not be used to just simply wrap existing
 * API exceptions. Instead, these should be used to signal application specific
 * failures that should be handled differently depending and distinctly from 
 * typical or common failures.  
 * 
 * These use cases are typically anticipated errors which can be gracefully handled
 * if they are not aliased by standard exceptions.
 * 
 * @author Mike Reinhold
 * @since 4.12.10
 */
public abstract class SchedulerException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new <code>SchedulerException</code> without a detailed error message
	 */
	public SchedulerException(){
		super();
	}
	
	/**
	 * Create a new <code>SchedulerException</code> with a detailed error message
	 * 
	 * @param message a detailed error message
	 */
	public SchedulerException(String message){
		super(message);
	}
	
	/**
	 * Create a new <code>SchedulerException</code> with a detailed error message
	 * and a <code>Throwable</code> cause which triggered the <code>SchedulerException</code>
	 * 
	 * @param message a detailed error message
	 * @param cause the <code>Throwable</code>  cause which triggered the exception
	 */
	public SchedulerException(String message, Throwable cause){
		super(message, cause);
	}
	
	/**
	 * Create a new <code>SchedulerException</code> with a <code>Throwable</code> 
	 * cause which triggered the <code>SchedulerException</code>
	 * 
	 * @param cause the <code>Throwable</code>  cause which triggered the exception
	 */
	public SchedulerException(Throwable cause){
		super(cause);
	}
}
