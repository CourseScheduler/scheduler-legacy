/**
 * @(#) Encryption.java
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
package io.devyse.scheduler.security;

/**
 * Utility methods for working with and configuring encryption options
 * 
 * @author Mike Reinhold
 * @since 4.12.7
 */
public class Encryption {

	/**
	 * The Java System property that controls the HTTPS protocols that are 
	 * enabled in the JVM.
	 * 
	 * {@link System#getProperties()}
	 */
	public static final String HTTPS_PROTOCOLS_PROPERTY = "https.protocols";
	
	/**
	 * Configure the enabled HTTPS protocols for controlling
	 * 
	 * @param protocols a comma separated list of HTTPS protocols
	 */
	public static void configureHttpsProtocols(String protocols){
		System.setProperty(HTTPS_PROTOCOLS_PROPERTY, protocols);
	}
}
