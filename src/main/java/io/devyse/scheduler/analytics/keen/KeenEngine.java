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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Static logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(KeenEngine.class);
	
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
	 * The event property for the external IP address of the client
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_GLOBAL_USER_IP = "user.ip";
	
	/**
	 * The event property for the IP geo location results produced by the IP to
	 * geo location add on
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_GLOBAL_USER_GEO = "user.geo";
	
	/**
	 * The event property for the user agent string of the client system
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_GLOBAL_USER_AGENT = "user.agent";
	
	/**
	 * The event property for the user agent string details, populated by the user
	 * agent parser addon.
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_GLOBAL_USER_AGENT_DETAILS = "user.agent_details";

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
	 * The Keen IO property corresponding to the event occurrance time. By default Keen uses the time
	 * the event is received by Keen servers, however this can be overridden by the client. Must
	 * be specified as an ISO-8601 date string.
	 * 
	 * Value: {@value} (implicitly inside the keen scope of the event
	 */
	public static final String KEEN_EVENT_TIMESTAMP = "timestamp";
	
	/**
	 * A custom event identifier to provide a litmus test against the keen.id for event uniqueness
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_EVENT_ID = "event.id";
	
	/**
	 * A boolean event property to indicate if the event was transmitted realtime or if it was a
	 * queued retry
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_EVENT_REALTIME = "event.realtime";
	
	/**
	 * The Keen properties property in which the list of enabled add ons is placed.
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_EVENT_ADDONS = "addons";
	
	/**
	 * Keen IO event property value which is automatically expanded into the client's external IP address
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_AUTO_COLLECT_IP = "${keen.ip}";
	
	/**
	 * Keen IO event property value which is automatically expanded into the client's user agent string
	 * 
	 * Value: {@value}
	 */
	public static final String KEEN_AUTO_COLLECT_AGENT = "${keen.user_agent}";

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
			//enable logging to better track issues during Keen setup
			KeenLogging.enableLogging();
			
			//disable the default JUL log handler installed by Keen
			KeenUtils.disableKeenDefaultLogHandler();
			
			configureKeenClient(config);
			configureShutdownHook(timeout);
			configureGlobalProperties();
			
			this.setInitialized(true);
			logger.debug("Successfully initialized Keen IO Analytics using configuration from {}", config);
		}catch(Exception e){
			this.setInitialized(false);
			logger.error("Unable to initialize Keen IO Analytics", e);
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
			logger.error("Unable to load keen configuration file ({}): ", KEEN_DEFAULT_CONFIG_FILE, e);
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
					logger.error("Interrupted while waiting for analytics executor to terminate", e);
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
	protected static void addGlobalJavaProperties(Map<String, Object> global){
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
	protected static void addGlobalApplicationProperties(Map<String, Object> global){
		global.put(KEEN_GLOBAL_APP_VERSION, Main.getApplicationVersion());
		global.put(KEEN_GLOBAL_APP_DIR, Main.getApplicationDirectory());
	}
	
	/**
	 * Add the user properties to the map of global properties
	 * 
	 * @param global the flat map of global properties
	 */
	protected static void addGlobalUserProperties(Map<String, Object> global){
		UUID identifier = Main.getPreferences().getIdentifier();
		if(identifier == null){
			identifier = UUID.randomUUID();
			Main.getPreferences().setIdentifier(identifier);
			Main.getPreferences().save();
		}
		
		global.put(KEEN_GLOBAL_USER_ID, identifier);
		global.put(KEEN_GLOBAL_USER_IP, KEEN_AUTO_COLLECT_IP);
		global.put(KEEN_GLOBAL_USER_AGENT, KEEN_AUTO_COLLECT_AGENT);
	}
	
	/**
	 * Add custom automatic properties that are event specific to the event. For instance,
	 * a client side event id that can be used to verify the Keen IO keen.id event identifier.
	 * 
	 * @param nestedMap the nested map containing the event properties
	 */
	protected static void addEventSpecificProperties(Map<String, Object> nestedMap){
		KeenUtils.addNestedMapEntry(nestedMap, KEEN_EVENT_ID, UUID.randomUUID());
		KeenUtils.addNestedMapEntry(nestedMap, KEEN_EVENT_REALTIME, Boolean.TRUE);
	}
	
	/**
	 * Keen namespace properties get put in a separate map as part of the Java SDK call
	 * interface. Build out the Keen properties for inclusion in the event
	 * 
	 * @return the nested map properties that fall under the Keen namespace in the event
	 */
	protected static Map<String, Object> buildKeenProperties(){
		Map<String, Object> nestedMap = new HashMap<>();
		
		//Keen allows overwriting the event timestamp in order to use the client event time
		//instead of the server receipt timestamp
		Calendar current = new GregorianCalendar();
		String timestamp = DatatypeConverter.printDateTime(current);
		KeenUtils.addNestedMapEntry(nestedMap, KEEN_EVENT_TIMESTAMP, timestamp);
		
		//configure the data enrichment add-ons
		KeenUtils.addNestedMapEntry(nestedMap, KEEN_EVENT_ADDONS, buildKeenAddons());
		
		return nestedMap;
	}
	
	/**
	 * Build the Keen addon configuration for the event to perform additional collection and
	 * processing on the event.
	 * 
	 * @return the list of addons that should be active for the event
	 */
	protected static List<Map<String, Object>> buildKeenAddons(){
		List<Map<String, Object>> addOnsList = new ArrayList<>();
		
		//IP Geo Addon
		addOnsList.add(buildKeenAddon(
				"keen:ip_to_geo",
				new String[]{"input.ip"},
				new String[]{KEEN_GLOBAL_USER_IP},
				KEEN_GLOBAL_USER_GEO
		));
		
		//User Agent Addon
		addOnsList.add(buildKeenAddon(
				"keen:ua_parser",
				new String[]{"input.ua_string"},
				new String[]{KEEN_GLOBAL_USER_AGENT},
				KEEN_GLOBAL_USER_AGENT_DETAILS
		));
		
		//TODO provide mechanism for switching on the URL parser addon for download event or other events that have a URL
		
		return addOnsList;
	}
	
	/**
	 * Build a nested map to configure the addon specified in the addonName. Details on the
	 * data collection addons and data enrichment features of Keen IO can be found at 
	 * {@linkplain https://keen.io/docs/data-collection/data-enrichment/}.
	 * 
	 * The inputParameters array must be of the same length as the inputProperties array
	 * 
	 * @param addonName the addon name which this nested map will configure
	 * @param inputParameters ordered list of input parameter names
	 * @param inputProperties ordered list of properties which contain the values for the input parameters
	 * @param output the output property into which the results of the data enrichment should be placed
	 * 
	 * @return the nested map containing the configuration for the add on
	 * 
	 * @throws IllegalArgumentException if the inputParameters array is not the same length as the inputProperties array
	 */
	protected static Map<String, Object> buildKeenAddon(String addonName, String[] inputParameters, String[] inputProperties, String output){
		Map<String, Object> addon = new HashMap<>();
		
		if(inputParameters.length != inputProperties.length){
			throw new IllegalArgumentException("Length of inputParameters array and inputProperties array must match");
		}

		KeenUtils.addNestedMapEntry(addon, "name", addonName);
		KeenUtils.addNestedMapEntry(addon, "output", output);
		for(int index = 0; index < inputParameters.length; index++){
			KeenUtils.addNestedMapEntry(addon, inputParameters[index], inputProperties[index]);
		}
		return addon;
	}
	
	/**
	 * Register the specified event into the specified collection
	 * 
	 * @param collection the name of the collection in the Keen project
	 * @param event a flat map of the event properties
	 */
	public void registerEvent(String collection, Map<String, Object> event){
		try{
			Map<String, Object> nested = KeenUtils.createNestedMap(event);
			addEventSpecificProperties(nested);
			
			this.getKeen().addEventAsync(collection, nested, buildKeenProperties());
		}catch(Exception e){
			//this will happen if analytics failed to initialize properly
			logger.warn("Unable to send event due to uninitialized analytics engine", e);
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
