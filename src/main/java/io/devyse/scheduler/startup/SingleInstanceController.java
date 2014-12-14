/**
 * @(#) SingleInstanceController.java
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

import javax.jnlp.ServiceManager;
import javax.jnlp.SingleInstanceListener;
import javax.jnlp.SingleInstanceService;
import javax.jnlp.UnavailableServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import Scheduler.Main;

/**
 * Manage the processing of the command line arguments that are passed to 
 * the any secondary instances of the application. 
 * 
 * @author Mike Reinhold
 * @since 4.12.7
 */
public class SingleInstanceController implements SingleInstanceListener {
	
	/**
	 * Service name for the JNLP provided SingleInstanceService
	 */
	public static final String JNLP_SINGLE_INSTANCE_SERVICE_NAME = "javax.jnlp.SingleInstanceService";
	
	/**
	 * Static logger
	 */
	private static Logger logger = LoggerFactory.getLogger(SingleInstanceController.class);
	
	/* (non-Javadoc)
	 * @see javax.jnlp.SingleInstanceListener#newActivation(java.lang.String[])
	 */
	@Override
	public void newActivation(String[] args) {
		//In this case, the String[] args provided here are the parameters provided to the new
		//instance of the application. Instead of calling Main, the JNLP SingleInstanceService
		//redirects the call here instead.
		
		//process the command line arguments with JCommander
		Parameters parameters = new Parameters();
		new JCommander(parameters, args);
		
		//open schedule files specified at start up
		Main.openScheduleFiles(parameters.getOpenFiles());
	}
	
	/**
	 * Register a SingleInstanceController as a listener in order to control additional instances
	 */
	public static void register(){
		try {
			final SingleInstanceService service = (SingleInstanceService) ServiceManager.lookup(JNLP_SINGLE_INSTANCE_SERVICE_NAME);
			final SingleInstanceController controller = new SingleInstanceController();
			service.addSingleInstanceListener(controller);
			
			//register a shutdown hook for removing the single instance listener
			Runtime.getRuntime().addShutdownHook(new Thread("SIS Controller") {
				@Override
				public void run() {
					try {
						service.removeSingleInstanceListener(controller);
					} catch (Exception e) {
						logger.error("Unable to deregister the single instance listener", e);
					}
				}
			});
		} catch (UnavailableServiceException | NoClassDefFoundError e) {
			logger.error("Unable to register as a single instance listener", e);
		}
	}
}
