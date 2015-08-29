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

import java.security.Security;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Utility methods for working with and configuring encryption options
 * 
 * @author Mike Reinhold
 * @since 4.12.7
 */
public class Encryption {

	/**
	 * Static class logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(Encryption.class);
	
	/**
	 * The Java System property that controls the HTTPS protocols that are 
	 * enabled in the JVM.
	 * 
	 * @see <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 */
	public static final String HTTPS_PROTOCOLS_PROPERTY = "https.protocols";
	
	/**
	 * The Java System property that controls the TLS client protocols that are 
	 * enabled in the JVM.
	 * 
	 * @see <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 */
	public static final String TLS_CLIENT_PROTOCOLS_PROPERTY = "jdk.tls.client.protocols";
	
	/**
	 * The Java System property that controls the TLS algorithms that are 
	 * configured as legacy in the JVM.
	 * 
	 * @see <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 * @see <a href="http://www.oracle.com/technetwork/java/javase/8u51-relnotes-2587590.html">JDK 8u51 Release Notes</a>
	 */
	public static final String TLS_LEGACY_ALGORITHMS_PROPERTY = "jdk.tls.legacyAlgorithms";
	
	/**
	 * The Java Security property that controls the TLS algorithms that are
	 * disabled in the JVM
	 * 
	 * @see <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 */
	public static final String TLS_DISABLED_ALGORITHMS_PROPERTY = "jdk.tls.disabledAlgorithms";
	
	/**
	 * The Java System property that controls the certificate validation protocols that are 
	 * disabled in the JVM.
	 * 
	 * @see <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 */
	public static final String CERTPATH_DISABLED_ALGORITHMS_PROPERTY = "jdk.certpath.disabledAlgorithms";
	
	/**
	 * The Java System property that controls the HTTPS cipher suites that are 
	 * enabled in the JVM.
	 * 
	 * @see <a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JSSE Reference Guide</a>
	 */
	public static final String HTTPS_CIPHER_SUITES_PROPERTY = "https.cipherSuites";
	
	
	/**
	 * Override the enabled HTTPS protocols by setting the {@value #HTTPS_PROTOCOLS_PROPERTY} system property
	 * 
	 * @param protocols comma separated list of HTTPS protocols which should be enabled
	 */
	public static void configureHttpsProtocols(String protocols){
		if(protocols != null){
			logger.info("Overriding HTTPS protocols via the <{}> system property as: <{}>", HTTPS_PROTOCOLS_PROPERTY, protocols);
			System.setProperty(HTTPS_PROTOCOLS_PROPERTY, protocols);
		} else {
			logger.debug("HTTPS protocol override not specified");
		}
	}
	
	/**
	 * Override the enabled TLS client protocols by setting the {@value #TLS_CLIENT_PROTOCOLS_PROPERTY} system property
	 * 
	 * @param protocols a comma separated list of TLS protocols which should be enabled
	 */
	public static void configureTlsClientProtocols(String protocols){
		if(protocols != null){
			logger.info("Overriding TLS client protocols via the <{}> system property as: <{}>", TLS_CLIENT_PROTOCOLS_PROPERTY, protocols);
			System.setProperty(TLS_CLIENT_PROTOCOLS_PROPERTY, protocols);
		} else {
			logger.debug("TLS client protocols override not specified");
		}
	}
	
	/**
	 * Override the TLS algorithms which should be considered as legacy by setting the {@value #TLS_LEGACY_ALGORITHMS_PROPERTY} security property
	 * 
	 * @param algorithms a comma separated list of TLS algorithms which should be considered legacy
	 */
	public static void configureTlsLegacyAlgorithms(String algorithms){
		if(algorithms != null){
			logger.info("Overriding TLS legacy algorithms via the <{}> security property as: <{}>", TLS_LEGACY_ALGORITHMS_PROPERTY, algorithms);
			Security.setProperty(TLS_LEGACY_ALGORITHMS_PROPERTY, algorithms);
		} else {
			logger.debug("TLS legacy algorithms override not specified");
		}
	}
	
	/**
	 * Override the TLS algorithms which should be disabled by setting the {@value #TLS_DISABLED_ALGORITHMS_PROPERTY} security property
	 * 
	 * @param algorithms a comma separated list of TLS algorithms which should be disabled
	 */
	public static void configureTlsDisabledAlgorithms(String algorithms){
		if(algorithms != null){
			logger.info("Overriding TLS disabled algorithms via the <{}> security property as: <{}>", TLS_DISABLED_ALGORITHMS_PROPERTY, algorithms);
			Security.setProperty(TLS_DISABLED_ALGORITHMS_PROPERTY, algorithms);
		} else    {
			logger.debug("TLS disabled algorithms override not specified");
		}
	}
	
	/**
	 * Override the certificate verification algorithms that should be disabled by setting the {@value #CERTPATH_DISABLED_ALGORITHMS_PROPERTY} security property
	 * 
	 * @param algorithms a comma separated list of certificate verification algorithms which should be disabled
	 */
	public static void configureCertPathDisabledAlgorithms(String algorithms){
		if(algorithms != null){
			logger.info("Overriding certificate verification disabled algorithms via the <{}> security property as: <{}>", CERTPATH_DISABLED_ALGORITHMS_PROPERTY, algorithms);
			Security.setProperty(CERTPATH_DISABLED_ALGORITHMS_PROPERTY, algorithms);
		} else {
			logger.debug("Certificate verification algorithms override not specified");
		}
	}
	
	/**
	 * Override the HTTPS cipher suites which should be enabled by setting the {@value #HTTPS_CIPHER_SUITES_PROPERTY} system property
	 * 
	 * @param cipherSuites a comma separated list of cipher suites which should be enabled
	 */
	public static void configureHttpsCipherSuites(String cipherSuites){
		if(cipherSuites != null){
			logger.info("Overriding HTTPS cipher suites via the <{}> system property as: <{}>", HTTPS_CIPHER_SUITES_PROPERTY, cipherSuites);
			System.setProperty(HTTPS_CIPHER_SUITES_PROPERTY, cipherSuites);
		} else {
			logger.debug("HTTPS cipher suites override not specified");
		}
	}
}
