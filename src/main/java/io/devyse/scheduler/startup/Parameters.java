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
	 * Multiple valued parameter specifying the HTTPS protocols which should be enabled
	 * 
	 * See the https.protocols property accessible via {@link System#getProperty(String)}
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "+https.protocols", description = "Enable one or more HTTPS protocols in addition to the JRE default",
			variableArity = true)
	private List<String> enableProtocols;
	{
		enableProtocols = new ArrayList<>();
		enableProtocols.add("SSLv2Hello");
	}
	
	/**
	 * Multiple valued parameter specifying the HTTPS protocols which should be disabled
	 * 
	 * See the https.protocols property accessible via {@link System#getProperty(String)}
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-https.protocols", description = "Disable one or more HTTPS protocols from the JRE default",
			variableArity = true)
	private List<String> disableProtocols;
	{
		disableProtocols = new ArrayList<>();
	}
	
	/**
	 * Multiple valued parameter specifying cipher suites which should be enabled in the SSLEngine
	 * 
	 * Note: the corresponding algorithm may also need to be enabled via -tls.algorithms
	 * 
	 * See the {@link SSLParameters#getCipherSuites}
	 * 
	 * Value: Varies
	 */
	@Parameter(names = "+ssl.cipherSuite", 
			description = "Enable one or more SSL/TLS cipher suites in addition to the JRE default cipher suites",
			variableArity = true)
	private List<String>  activateCipherSuites;
	// provide the default list of additional cipher suites
	{
		activateCipherSuites = new ArrayList<>();
		activateCipherSuites.add("SSL_RSA_WITH_RC4_128_MD5");
	}
	
	/**
	 * Multiple valued parameter specifying cipher suites which should be disabled in the SSLEngine
	 * in order to prevent its use.
	 * 
	 * Value: varies
	 */
	@Parameter(names = "-ssl.cipherSuite",
			description = "Disable one or more SSL/TLS cipher suites from the JRE default cipher suite list",
			variableArity = true)
	private List<String> disableCipherSuites;
	{
		disableCipherSuites = new ArrayList<>();
	}
	
	/**
	 * Multiple valued parameter specifying the algorithms which should be enabled by removing them from the 
	 * {@value io.devyse.scheduler.security.Encryption#TLS_DISABLED_ALGORITHMS_PROPERTY} security property.
	 * 
	 * Note: specific cipher suite combinations may still need to be enabled via +ssl.cipherSuites
	 * 
	 * Value: Varies
	 */
	@Parameter(names = "+tls.algorithms", description = "Enable one or more TLS algorithms.", variableArity = true)
	private List<String> activateAlgorithms;
	{
		activateAlgorithms = new ArrayList<>();
		activateAlgorithms.add("DH < 768");
	}
	
	/**
	 * Multiple valued parameter specifying the algorithms which should be disabled by adding them
	 * to the {@value io.devyse.scheduler.security.Encryption#TLS_DISABLED_ALGORITHMS_PROPERTY} security property.
	 * 
	 * Value: Varies
	 */
	@Parameter(names = "-tls.algorithms", description = "Disable one or more TLS algorithms", variableArity = true)
	private  List<String> disableAlgorithms;
	{
		disableAlgorithms = new ArrayList<>();
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
	 * @return the httpsProtocols that should be enabled as defined in the {@value io.devyse.scheduler.security.Encryption#HTTPS_PROTOCOLS_PROPERTY} system property
	 */
	public List<String> getEnableProtocols(){
		return enableProtocols;
	}
	
	/**
	 * @return the https protocols that should be disabled as defined in the {@value io.devyse.scheduler.security.Encryption#HTTPS_PROTOCOLS_PROPERTY} system property
	 */
	public List<String> getDisableProtocols(){
		return disableProtocols;
	}
	
	/**
	 * @return the cipher suites that should be enabled via the default SSLContext SSLParameters
	 */
	public List<String> getEnableCipherSuites(){
		return activateCipherSuites;
	}
	
	/**
	 * @return the cipher suites that should be disabled via the default SSLContext SSLParameters
	 */
	public List<String> getDisableCipherSuites(){
		return disableCipherSuites;
	}
	
	/**
	 * @return the algorithms which should be enabled via the {@value io.devyse.scheduler.security.Encryption#TLS_DISABLED_ALGORITHMS_PROPERTY}
	 */
	public List<String> getEnableAlgorithms(){
		return activateAlgorithms;
	}
	
	/**
	 * @return the algorithms which should be disabled via the {@value io.devyse.scheduler.security.Encryption#TLS_DISABLED_ALGORITHMS_PROPERTY}
	 */
	public List<String> getDisableAlgorithms(){
		return disableAlgorithms;
	}
	
	/**
	 * @return if debug logging should remain enabled
	 */
	public boolean getDebugEnabled(){
		return debugEnabled;
	}
}
