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
import java.security.Security;
import java.util.Arrays;
import java.util.List;

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
	 * The Java Security property that controls the TLS algorithms that are
	 * enabled in the JVM
	 * 
	 * {@link Security#getProperty(String)}
	 */
	public static final String TLS_DISABLED_ALGORITHMS_PROPERTY = "jdk.tls.disabledAlgorithms";
	
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
	
	public static void enableLegacyAlgorithms(String[] algorithms){
		String original = Security.getProperty(TLS_DISABLED_ALGORITHMS_PROPERTY);
		logger.debug("Default disabled algorithms in {}: {}", TLS_DISABLED_ALGORITHMS_PROPERTY, original);
		
		/*
		 * The disabledAlgorithms property is a comma separated list that may contain
		 * whitespace before or after the comma. Additionally, constraints may be placed
		 * on the algorithm's key size
		 * 
		 * Example: jdk.tls.disabledAlgorithms=RSA, DSA, HDE, EC keySize < 256
		 */
		List<String> disabledAlgorithms = Arrays.asList(original.split("\\s*,\\s*"));
		
		for(String algorithm: algorithms){
			if(original.contains(algorithm)){
				logger.debug("Removing {} from default disabled algorithms", algorithm);
				
				disabledAlgorithms.remove(algorithm);
			} else {
				logger.debug("Algorithm {} not disabled by default in this runtime", algorithm);
			}
		}
		
		/*
		 * We need to rebuild the updated property using the same format as the original
		 */
		StringBuilder builder = new StringBuilder();
		for(String algorithm:  disabledAlgorithms){
			if(builder.length()>0){
				builder.append(", ");
			}
			builder.append(algorithm);
		}
		String updated = builder.toString();
		
		try{
			Security.setProperty(TLS_DISABLED_ALGORITHMS_PROPERTY, updated);
			logger.debug("Disabled algorithms list updated from {} to {}", original, updated);
		} catch(SecurityException e){
			logger.error("Unabled to update security property {} from {} to {} due to SecurityManager settings", new Object[]{
					TLS_DISABLED_ALGORITHMS_PROPERTY,
					original, updated
			});
		}
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
