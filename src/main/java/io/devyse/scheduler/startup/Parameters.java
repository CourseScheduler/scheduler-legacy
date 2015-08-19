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
	 * Multiple valued parameter specifying cipher suites which should be forcefully enabled
	 * (to ensure that the cipher suites is available in the Java runtime, even if the JRE
	 * default enabled cipher suite list does not include it).
	 * 
	 * See the {@link SSLParameters#getCipherSuites}
	 * 
	 * Value: Varies
	 */
	@Parameter(names = "-ssl.cipherSuite", 
			description = "Enable one or more cipher suites in addition to the JRE default cipher suites",
			variableArity = true)
	private List<String>  cipherSuites;
	// provide the default list of additional cipher suites
	{
		cipherSuites = new ArrayList<String>();
		cipherSuites.add("SSL_RSA_WITH_RC4_128_MD5");
	}

	/**
	 * Flag to enable debug logging for the duration of the application execution.
	 * 
	 * Debug logging is enabled during application start up in order to assist with troubleshooting
	 * issues. Once the application successfully starts up, logging levels are dropped to "info"
	 * in order to reduce log volume and overhead. Enable debug logging to keep the "debug" log level
	 * for the duration of the application execution.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-debug", description = "Debug logging is always enabled by default during initialization and " +
			"is disabled after successful start up. Using this flag will enable debug logging for the duration of the " +
			"program execution.")
	private boolean debugEnabled = false;
	
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
	
	/**
	 * @return the cipherSuites that should be forcefully enabled via the default SSLContext SSLParameters
	 */
	public String[] getCipherSuites(){
		return cipherSuites.toArray(new String[cipherSuites.size()]);
	}
	
	/**
	 * @return if debug logging should remain enabled
	 */
	public boolean getDebugEnabled(){
		return debugEnabled;
	}
}
