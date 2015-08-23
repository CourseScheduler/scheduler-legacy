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
import java.util.ArrayList;
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
	 * Configure the enabled/disabled HTTPS protocols by adding and removing protocols 
	 * to/from the {@value #HTTPS_PROTOCOLS_PROPERTY} system property.
	 * 
	 * Note: algorithms may still need to be enabled via {@link #configureAlgorithms(String[], String[])}
	 * Note: cipher suites may still need to be enabled via {@link #configureCipherSuites(String[], String[])}
	 * 
	 * @param enable HTTPS protocols to enable by adding to the list
	 * @param disable HTTPS protocols to disable by removing from the list
	 */
	public static void configureProtocols(List<String> enable, List<String> disable){
		String original = System.getProperty(HTTPS_PROTOCOLS_PROPERTY);
		logger.debug("Default enabled HTTPS protocols in {}: <{}>", HTTPS_PROTOCOLS_PROPERTY, original);
		
		List<String> httpsProtocols = splitSecurityPropertyValue(original);
		
		try {
			SSLContext defaultContext = SSLContext.getDefault();
			
			SSLParameters defaultParameters = defaultContext.getDefaultSSLParameters();
			String[] defaultProtocols = defaultParameters.getProtocols();
			
			httpsProtocols.addAll(Arrays.asList(defaultProtocols));
			logger.debug("Default enabled HTTPS protocols from SSL context: <{}>", (Object)defaultProtocols);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Enable to retrieve default enabled HTTPS protocols from SSL Context", e);
		}
		
		logger.debug("Removing HTTPS protocols from enabled list: <{}>", (Object)disable);
		httpsProtocols.removeAll(disable);
		
		logger.debug("Adding HTTPS protocols to enabled list: <{}>", (Object)enable);
		httpsProtocols.addAll(enable);
		
		String updated = buildSecurityPropertyValue(httpsProtocols);
		
		logger.info("Updating enabled HTTPS protocols from {} to {}", original, updated);
		System.setProperty(HTTPS_PROTOCOLS_PROPERTY, updated);
	}
	
	//todo handle jdk.tls.client.protocols, same as https.protocols
	
	/**
	 * Configure the enabled/disabled SSL/TLS algorithms by adding or removing algorithms
	 * to/from the {@value #TLS_DISABLED_ALGORITHMS_PROPERTY} security property.
	 * 
	 * Note: protocols may still need to be enabled via {@link #configureProtocols(String[], String[])}
	 * Note: cipher suites may still need to be enabled via {@link #configureCipherSuites(String[], String[])}
	 * 
	 * @param enable SSL/TLS algorithms to enable by removing from the list
	 * @param disable SSL/TLS algorithms to disable by adding to the list
	 */
	public static void configureAlgorithms(List<String> enable, List<String> disable){
		String original = Security.getProperty(TLS_DISABLED_ALGORITHMS_PROPERTY);
		logger.debug("Default disabled algorithms in {}: <{}>", TLS_DISABLED_ALGORITHMS_PROPERTY, original);
		
		List<String> disabledAlgorithms = splitSecurityPropertyValue(original);
		
		logger.debug("Removing algorithms from disabled list: <{}>", (Object) enable);
		disabledAlgorithms.removeAll(enable);
		
		logger.debug("Adding algorithms to disabled list: <{}>", (Object)disable);
		disabledAlgorithms.addAll(disable);
		
		String updated = buildSecurityPropertyValue(disabledAlgorithms);
		
		try{
			Security.setProperty(TLS_DISABLED_ALGORITHMS_PROPERTY, updated);
			logger.info("Disabled algorithms list updated from {} to {}", original, updated);
		} catch(SecurityException e){
			logger.error("Unabled to update disabled algoritms in {} from <{}> to <{}> due to SecurityManager settings", new Object[]{
					TLS_DISABLED_ALGORITHMS_PROPERTY,
					original, updated
			});
		}
	}
	
	/**
	 * Configure the enabled/disabled SSL/TLS cipher suites by adding or removing cipher
	 * suites to/from the default SSLContext default SSLParameters
	 * 
	 * Note: protocols may still need to be enabled via {@link #configureProtocols(String[], String[])}
	 * Note: algorithms may still need to be enabled via {@link #configureAlgorithms(String[], String[])}
	 * 
	 * @param enable SSL/TLS cipher suites to enable by adding
	 * @param disable SSL/TLS cipher suites to disable by removing
	 */
	public static void configureCipherSuites(List<String> enable, List<String> disable){
		try {
			SSLContext defaultContext = SSLContext.getDefault();
			SSLParameters defaultParameters = defaultContext.getDefaultSSLParameters();
			
			String[] original = defaultParameters.getCipherSuites();
			logger.debug("Default enabled cipher suites: {}", (Object)original);
			
			List<String> ciphers = new ArrayList<>(Arrays.asList(original));
			
			logger.debug("Removing ciphers from enabled cipher list: <{}>", (Object)disable);
			ciphers.removeAll(disable);
			
			logger.debug("Adding ciphers to enabled cipher list: <{}>", (Object)enable);
			ciphers.addAll(enable);
			
			String[] updated = ciphers.toArray(new String[ciphers.size()]);
			logger.info("Updating cipher suites from <{}> to <{}>", (Object)original, (Object)updated);
			
			defaultParameters.setCipherSuites(updated);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Unable to retrieve default SSL context", e);
		} catch (IllegalArgumentException e){
			logger.error("One or more cipher suites could not be enabled because it is not supported by this runtime.", e);
		}
	}
	
	/**
	 * Split a security property value in the standard comma separated value (with optional constraint)
	 * string format to a list of values for easier handling
	 * 
	 * @param propertyValue the original property value in constrained comma separated value format
	 * @return a list of values, parsed from the original format
	 */
	protected static List<String> splitSecurityPropertyValue(String propertyValue){
		if(propertyValue == null){ return new ArrayList<>(); }
		
		/*
		 * Security properties used to configure the available and disabled protocols, algorithms, and 
		 * cipher suites conform to a specific format of comma separated values. Whitespace may be 
		 * before or after the comma. Additionally, individual entries may contain whitespace or other
		 * special characters, especially when constraints on the algorithms are used.
		 * 
		 * For example:
		 * The disabledAlgorithms property follows this premise and is used to store the algorithms
		 * that are disabled due to security policy or are deprecated for security sake, but not yet
		 * removed so thta they may still be used for compatibility sake. Constraints may be placed
		 * on the algorithm's key size such as demonstrated below
		 * 
		 * Example: jdk.tls.disabledAlgorithms=RSA, DSA, HDE, EC keySize < 256
		 */
		return new ArrayList<>(Arrays.asList(propertyValue.split("\\s*,\\s*")));
	}
	
	/**
	 * Build an updated security property value using the standard comma separated value format (with
	 * optional constraints still allowed)
	 * 
	 * @param values the values to use for the security property
	 * @return the updated security property value
	 */
	protected static String buildSecurityPropertyValue(List<String> values){
		/*
		 * We need to rebuild the updated property using the same format as the original
		 */
		StringBuilder builder = new StringBuilder();
		for(String value:  values){
			if(builder.length()>0){
				builder.append(",");
			}
			builder.append(value);
		}
		return builder.toString();
	}
	
}
