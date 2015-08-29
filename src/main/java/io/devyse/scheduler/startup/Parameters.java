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
	 * Parameter specifying the HTTPS protocols which should be enabled. This parameter is 
	 * used to set the corresponding System property for cases where it cannot be set 
	 * during application invocation (for instance via Java WebStart).
	 * 
	 * @see the https.protocols property on the <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-https.protocols", description = "Specify the HTTPS protocols which should be enabled")
	private String httpsProtocols = "TLSv1,TLSv1.1,TLSv1.2,SSLv2Hello";
	
	/**
	 * Parameter specifying the TLS client protocols which should be enabled (JDK8+). This property
	 * is used to set the corresponding System property for cases where it cannot be set
	 * during application invocation (for instance via Java WebStart).
	 * 
	 * @see the jdk.tls.client.protocols property on the <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-jdk.tls.client.protocols", description = "Specify the TLS client protocols which should be enabled")
	private String tlsClientProtocols;
	
	/**
	 * Parameter specifying the TLS algorithms which should be considered legacy (JDK8u51+). This property
	 * is used to set the corresponding System property for cases where it cannot be set
	 * during application invocation (for instance via Java WebStart).
	 * 
	 * @see the jdk.tls.legacyAlgorithms property on the <a href="@see <a href="http://www.oracle.com/technetwork/java/javase/8u51-relnotes-2587590.html">JDK 8u51 Release Notes</a>">Java 8u51 Release Notes</a>
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-jdk.tls.legacyAlgorithms", description = "Specify the TLS algorithms which should be considered legacy")
	private String tlsLegacyAlgorithms;
	
	/**
	 * Parameter specifying the TLS algorithms which should be disabled. This property
	 * is used to set the corresponding System property for cases where it cannot be set
	 * during application invocation (for instance via Java WebStart).
	 * 
	 * @see the jdk.tls.disabledAlgorithms property on the <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-jdk.tls.disabledAlgorithms", description = "Specify the TLS algorithms which should be disabled")
	private String tlsDisabledAlgorithms = "SSLv3, DH keySize < 768";
	
	/**
	 * Parameter specifying the TLS certifivate verification algorithms which should be disabled. This property
	 * is used to set the corresponding System property for cases where it cannot be set
	 * during application invocation (for instance via Java WebStart).
	 * 
	 * @see the jdk.certpath.disabledAlgorithms property on the <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-jdk.certpath.disabledAlgorithms", description = "Specify the certificate path algorithms which should be disabled")
	private String certpathDisabledAlgorithms;
	
	/**
	 * Parameter specifying the HTTPS cipher suites which should be enabled. This property
	 * is used to set the corresponding System property for cases where it cannot be set
	 * during application invocation (for instance via Java WebStart).
	 * 
	 * @see the https.cipherSuites property on the <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 * for more information.
	 * 
	 * Value: {@value}
	 */
	@Parameter(names = "-https.cipherSuites", description = "Specify the HTTPS cipher suites which should be enabled")
	private String httpsCipherSuites = "SSL_RSA_WITH_RC4_128_MD5";
	
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
	 * @return the HTTPS protocols which should be enabled via the https.protocols property
	 */
	public String getHttpsProtocols() {
		return httpsProtocols;
	}

	/**
	 * @return the TLS client protocols which should be enabled via the jdk.tls.client.protocols property
	 */
	public String getTlsClientProtocols() {
		return tlsClientProtocols;
	}

	/**
	 * @return the TLS algorithms which should be considered legacy via the jdk.tls.legacyAlgorithms property
	 */
	public String getTlsLegacyAlgorithms() {
		return tlsLegacyAlgorithms;
	}

	/**
	 * @return the TLS algorithms which should be disabled via the jdk.tls.disabledAlgorithms property
	 */
	public String getTlsDisabledAlgorithms() {
		return tlsDisabledAlgorithms;
	}

	/**
	 * @return the TLS certification path algorithms which should be disabled via the jdk.certpath.disabledAlgorithms property
	 */
	public String getCertpathDisabledAlgorithms() {
		return certpathDisabledAlgorithms;
	}

	/**
	 * @return the HTTPS cipher suites which should be enabled via the https.cipherSuites property
	 */
	public String getHttpsCipherSuites() {
		return httpsCipherSuites;
	}

	/**
	 * @return if debug logging should remain enabled
	 */
	public boolean getDebugEnabled(){
		return debugEnabled;
	}
}
