/**
 * @(#) Logging.java
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
package io.devyse.scheduler.logging;

import org.slf4j.LoggerFactory;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;


/**
 * Manages the initialization and configuration of the application logging subsystem
 * 
 * @author Mike Reinhold
 * @since 4.12.6
 */
public class Logging {

	/**
	 * Array of logger names which should be reset to "INFO" from "DEBUG"
	 * once startup initialization is complete (unless the -debug flag is
	 * set in the command line parameters as documented in {@link Parameters#debugEnabled}
	 * 
	 * Value: {@value}
	 */
	private static String[] LOGGERS_TO_RESET = {
		"Scheduler",
		"io.devyse"
	};
	
	/**
	 * The Logger Level to which the startup debug loggers should be set once startup 
	 * initialization is complete.
	 * 
	 * Value: {@value}
	 */
	private static Level LOGGER_RESET_LEVEL = Level.INFO;
	
	/**
	 * Static logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(Logging.class);
	
	/**
	 * Initialize the logging subsystem and necessary logging bridges
	 */
	public static void initialize(){
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		logger.info("JUL to SLF4J bridge initialized");
	}
	
	/**
	 * Disable the startup debug logging.
	 * 
	 * Some loggers are set to log at DEBUG level during initialization in
	 * order to aid in troubleshooting application startup. Once initialization
	 * is complete, these loggers can be set back to INFO level for normal
	 * logging volume and overhead.
	 */
	public static void disableStartupDebugLogging(){
		for(String loggerName: LOGGERS_TO_RESET){
			Logger logger = (Logger)LoggerFactory.getLogger(loggerName);
			logger.debug("Switching logger from startup level {} to regular runtime level {}",
					logger.getLevel(), LOGGER_RESET_LEVEL);
			logger.setLevel(LOGGER_RESET_LEVEL);
		}
	}
}
