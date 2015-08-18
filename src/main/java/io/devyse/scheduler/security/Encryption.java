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

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

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
	 * {@link System#getProperties()}
	 */
	public static final String HTTPS_PROTOCOLS_PROPERTY = "https.protocols";
	
	/**
	 * Configure the enabled HTTPS protocols for controlling
	 * 
	 * @param protocols a comma separated list of HTTPS protocols
	 */
	public static void configureHttpsProtocols(String protocols){
		String originalProtocols = System.getProperty(HTTPS_PROTOCOLS_PROPERTY);
		logger.debug("Updating enabled HTTPS protocols from {} to {}", originalProtocols, protocols);
		System.setProperty(HTTPS_PROTOCOLS_PROPERTY, protocols);
	}
	
	/**
	 * Enable a list of SSL cipher suites in addition to the default enabled SSL cipher suites
	 * 
	 * @param additionalCiphers  an array of cipher suites which should be enabled in addition to the JRE default
	 */
	public static void configureCipherSuites(String[] additionalCiphers){
		try {
			SSLContext defaultContext = SSLContext.getDefault();
			SSLParameters defaultParameters = defaultContext.getDefaultSSLParameters();
						
			String[] defaultCiphers = defaultParameters.getCipherSuites();
			String[] newCiphers = new String[defaultCiphers.length + additionalCiphers.length];
			System.arraycopy(defaultCiphers, 0, newCiphers, 0, defaultCiphers.length);
			System.arraycopy(additionalCiphers, 0, newCiphers, defaultCiphers.length, additionalCiphers.length);
			
			if(logger.isDebugEnabled()){
				for(String enabled: defaultCiphers){
					logger.debug("Existing cipher suite enabled by default: {}", enabled);
				}
				
				for(String additional: additionalCiphers){
					logger.debug("Attempting to enable additional cipher suite:  {}", additional);
				}
			}
			defaultParameters.setCipherSuites(newCiphers);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Unable to retrieve default SSL context", e);
		} catch (IllegalArgumentException e){
			logger.error("One or more cipher suites could not be enabled because it is not supported by this runtime.", e);
		}
		
		
	}
}
