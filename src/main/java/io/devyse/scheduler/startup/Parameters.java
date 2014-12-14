/**
 * @(#) Parameters.java
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
package io.devyse.scheduler.startup;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

/**
 * Process and validate the command line parameters and switches passed to the application.
 *
 * This class defines the parameters and switches that are expected by the application and 
 * specifies how they should be handled by JCommander.
 * 
 * @author Mike Reinhold
 * @since 4.12.7
 */
public class Parameters {

	/**
	 * List of schedule files that should be opened by the application at startup
	 * 
	 * Value: {@value}
	 */
	@Parameter(description = "Schedule files to open at start up")
	private List<String> openFiles = new ArrayList<>();
	
	/**
	 * Comma separated list of enabled HTTPS protocols.
	 * 
	 * See the https.protocols property accessible via {@link System#getProperty(String)}
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-https.protocols", description = "Comma separated list of the enabled HTTPS protocols")
	private String httpsProtocols = "TLSv1.2,TLSv1.1,TLSv1,SSLv3,SSLv2Hello";

	/**
	 * @return the openFiles the files that should be opened during startup
	 */
	public List<String> getOpenFiles() {
		return openFiles;
	}
	
	/**
	 * @return the httpsProtocols that should be enabled as defined in the https.protocols system property
	 */
	public String getHttpsProtocols(){
		return httpsProtocols;
	}
}
