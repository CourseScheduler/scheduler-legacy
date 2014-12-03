/**
 * @(#) KeenEngine.java
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

package io.devyse.scheduler.analytics.keen;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import Scheduler.Main;
import io.keen.client.java.JavaKeenClientBuilder;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenLogging;
import io.keen.client.java.KeenProject;

/**
 * Keen IO Analytics engine that processes application events and sends them to Keen IO
 * as events for analytics
 * 
 * @author Mike Reinhold
 * @since 4.12.5
 */
public class KeenEngine {
	
	/**
	 * Keen configuration file containing the project ID, read key, and write key. This file
	 * is retrieved using the KeenEngine classloader.
	 * 
	 * Value: {@value}
	 */
	protected static final String KEEN_DEFAULT_CONFIG_FILE = "config/keen.properties";
	
	/**
	 * Keen configuration file property that contains the project ID to which analytic events
	 * should be written.
	 * 
	 * Value: {@value}
	 */
	protected static final String KEEN_PROJECT_ID = "keen.project.id";

	/**
	 * Keen configuration file property that contains the write key which should be used to
	 * write events to the project.
	 * 
	 * Value: {@value}
	 */
	protected static final String KEEN_PROJECT_WRITE_KEY = "keen.project.write";

	/**
	 * Keen configuration file property that contains the read key which should be used to
	 * read events from the project.
	 * 
	 * Value: {@value}
	 */
	protected static final String KEEN_PROJECT_READ_KEY = "keen.project.read";
	
	/**
	 * Depending on the user's internet connection and current load on the Keen IO service,
	 * posting a backlog of events can take some time. The KeenEngine registers a shutdown
	 * hook to allow the Keen asynchronous executor service to continue processing events 
	 * up to this timeout (defined in seconds)
	 * 
	 * Value: {@value}
	 */
	protected static final long KEEN_SHUTDOWN_CLEANUP_TIMEOUT = 15L;

	/**
	 * The Java system properties (retrieved using {@link java.lang.System#getProperties()}) 
	 * are added to the Keen client's global properties map so that they are automatically 
	 * included with each event. Each Java system property is stored under the context
	 * specified by this variable.
	 * 
	 * Value: {@value}
	 */
	protected static final String KEEN_GLOBAL_SYSTEM_PREFIX = "system";

	/**
	 * The unique user identifier event property in the global properties.
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_GLOBAL_USER_ID = "user.id";

	/**
	 * The application version event property in the global properties.
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_GLOBAL_APP_VERSION = "scheduler.version";

	/**
	 * The application directory event property in the global properties.
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_GLOBAL_APP_DIR = "scheduler.home";

	/**
	 * The default KeenEngine which can be used to publish events
	 */
	private static KeenEngine defaultEngine;
	
	/**
	 * The Keen Client which handles the details of writing and reading events from
	 * the Keen IO service.
	 */
	private KeenClient keen;

	/**
	 * Indicator for if the KeenEngine was successfully initialized
	 */
	private boolean initialized;
	
	/**
	 * Create a new KeenEngine
	 */
	protected KeenEngine(){
		super();
		
		this.setKeen(new JavaKeenClientBuilder().build());
		this.setInitialized(false);
	}

	/**
	 * @return if the KeenEngine is initialized
	 */
	public boolean isInitialized() {
		return this.initialized;
	}

	/**
	 * @param initialized set the initialized status of the KeenEngine
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	/**
	 * @return the Keen IO Client
	 */
	protected KeenClient getKeen() {
		return this.keen;
	}

	/**
	 * @param keen the Keen IO Client to set
	 */
	protected void setKeen(KeenClient keen) {
		this.keen = keen;
	}
	
	/**
	 * Initialize the KeenEngine using the specified configuration file
	 * 
	 * @param config the path to the configuration file
	 * @param timeout the shutdown hook timeout, in seconds
	 */
	protected void initialize(String config, long timeout){
		try{
			//enable logging and debug to better track issues during Keen setup
			KeenLogging.enableLogging();
			this.getKeen().setDebugMode(true);
			
			configureKeenClient(config);
			configureShutdownHook(timeout);
			configureGlobalProperties();
			
			this.setInitialized(true);
		}catch(Exception e){
			System.out.println("Unable to initialize Keen IO Analytics: " + e);
			this.setInitialized(false);
		}
	}
	
	/**
	 * Configure the Keen API client using the specified configuration file path.
	 * 
	 * @param config the path to the Keen IO client configuration file
	 * @throws IOException if there is an issue loading the configuration file
	 */
	protected void configureKeenClient(String config) throws IOException{
		try{
			ClassLoader loader = KeenEngine.class.getClassLoader();
			
			//TODO change the resource to be passed in as a parameter to the program or via an env variable
			Properties keenProperties = new Properties();
			try{
				keenProperties.load(loader.getResourceAsStream(config));
			}catch(Exception e){
				//TODO log that we couldn't load the config file via the classloader resource mechanism
				keenProperties.load(new FileInputStream(config));
			}
			
			KeenProject keenProject = new KeenProject(
				keenProperties.getProperty(KEEN_PROJECT_ID),
				keenProperties.getProperty(KEEN_PROJECT_WRITE_KEY),
				null					// KEEN_PROJECT_READ_KEY - not currently used
			);
			
			this.getKeen().setDefaultProject(keenProject);
		} catch(IOException e){
			System.out.println("Unable to load keen configuration file (" + KEEN_DEFAULT_CONFIG_FILE + "): " + e);
			throw e;
		}
	}
	
	/**
	 * Configure a shutdown hook in the Java runtime to allow the Keen client to flush
	 * any queued events prior to the JVM exiting completely.
	 * 
	 * @param timeout the timeout, in seconds, which the executor service will have to allow event publishing
	 */
	protected void configureShutdownHook(final long timeout){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				ExecutorService keenExecutor = (ExecutorService)keen.getPublishExecutor();
				keenExecutor.shutdown();
				try {
					keenExecutor.awaitTermination(timeout, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Configure the Keen global properties which will be included automatically in each event.
	 */
	protected void configureGlobalProperties(){
		Map<String, Object> global = new HashMap<>();
		
		addGlobalJavaProperties(global);
		addGlobalApplicationProperties(global);
		addGlobalUserProperties(global);
		
		//convert the map from a standard, "flat" map to a nested map as required by Keen
		global = KeenUtils.createNestedMap(global);
		
		this.getKeen().setGlobalProperties(global);
	}
	
	/**
	 * Add the Java system properties to the map of global properties
	 * 
	 * @param global the flat map of global properties
	 */
	protected void addGlobalJavaProperties(Map<String, Object> global){
		Map<String, Object> system = new HashMap<>();
		
		for(Object key: System.getProperties().keySet()){
			system.put(key.toString(), System.getProperty(key.toString()));
		}
		
		global.put(KEEN_GLOBAL_SYSTEM_PREFIX, system);
	}
	
	/**
	 * Add the Application properties to the map of global properties
	 * 
	 * @param global the flat map of global properties
	 */
	protected void addGlobalApplicationProperties(Map<String, Object> global){
		global.put(KEEN_GLOBAL_APP_VERSION, Main.getApplicationVersion());
		global.put(KEEN_GLOBAL_APP_DIR, Main.getApplicationDirectory());
	}
	
	/**
	 * Add the user properties to the map of global properties
	 * 
	 * @param global the flat map of global properties
	 */
	protected void addGlobalUserProperties(Map<String, Object> global){
		UUID identifier = Main.getPreferences().getIdentifier();
		if(identifier == null){
			identifier = UUID.randomUUID();
			Main.getPreferences().setIdentifier(identifier);
			Main.getPreferences().save();
		}
		
		global.put(KEEN_GLOBAL_USER_ID, identifier);
	}
	
	/**
	 * Register the specified event into the specified collection
	 * 
	 * @param collection the name of the collection in the Keen project
	 * @param event a flat map of the event properties
	 */
	public void registerEvent(String collection, Map<String, Object> event){
		try{
			this.getKeen().addEventAsync(collection, KeenUtils.createNestedMap(event));
		}catch(Exception e){
			//this will happen if analytics failed to initialize properly
		}
	}
	
	/**
	 * Retrieve the default KeenEngine singleton that can be reused within an application.
	 * This static method will initialize the default KeenEngine if it is not already prepared
	 * 
	 * @return the default KeenEngine
	 */
	public static KeenEngine getDefaultKeenEngine(){
		if(defaultEngine == null){
			defaultEngine = new KeenEngine();
			defaultEngine.initialize(KEEN_DEFAULT_CONFIG_FILE, KEEN_SHUTDOWN_CLEANUP_TIMEOUT);
		}
		return defaultEngine;
	}
}
