/**
 * @(#) TermNotFoundException.java
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
 * Exception message to indicate that the specified term was not found
 * in the source.
 * 
 * @author Mike Reinhold
 * @since 4.12.10
 */
public class TermNotFoundException extends SchedulerException {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new <code>TermNotFoundException</code> without a detailed error message
	 */
	public TermNotFoundException(){
		super();
	}
	
	/**
	 * Create a new <code>TermNotFoundException</code> with a detailed error message
	 * 
	 * @param message a detailed error message
	 */
	public TermNotFoundException(String message){
		super(message);
	}
	
	/**
	 * Create a new <code>TermNotFoundException</code> with a detailed error message
	 * and a <code>Throwable</code> cause which triggered the <code>TermNotFoundException</code>
	 * 
	 * @param message a detailed error message
	 * @param cause the <code>Throwable</code>  cause which triggered the exception
	 */
	public TermNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
	
	/**
	 * Create a new <code>TermNotFoundException</code> with a <code>Throwable</code> 
	 * cause which triggered the <code>TermNotFoundException</code>
	 * 
	 * @param cause the <code>Throwable</code>  cause which triggered the exception
	 */
	public TermNotFoundException(Throwable cause){
		super(cause);
	}	
}
