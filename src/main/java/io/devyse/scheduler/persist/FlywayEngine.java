/**
 * @(#) FlywayEngine.java
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
package io.devyse.scheduler.persist;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.MigrationVersion;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Flyway engine which will control the database bootstrapping and preparation process
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public class FlywayEngine {

	public static final String DEFAULT_FLYWAY_CONFIGURATION = "config/flyway.properties";
	
	/**
	 * Static logger 
	 */
	private static final XLogger logger = XLoggerFactory.getXLogger(FlywayEngine.class);
	
	/**
	 * Flyway instance that will manage database setup and preparation
	 */
	private Flyway flyway;
	
	/**
	 * @return the flyway
	 */
	private Flyway getFlyway() {
		return flyway;
	}

	/**
	 * @param flyway the flyway to set
	 */
	private void setFlyway(Flyway flyway) {
		this.flyway = flyway;
	}

	/**
	 * 
	 */
	public FlywayEngine() {
		super();
		
		this.setFlyway(new Flyway());
	}
	
	public void initializeDataStore(DataSource source){
		//TODO custom configuration
		Properties configuration = new Properties();
		try {
			logger.debug("Attempting to load default flyway configuration from {}", DEFAULT_FLYWAY_CONFIGURATION);
			configuration.load(ClassLoader.getSystemResourceAsStream(DEFAULT_FLYWAY_CONFIGURATION));
			initializeDataStore(configuration, source);
		} catch (IOException e) {
			logger.error("Unable to find default flyway configuration at {}", DEFAULT_FLYWAY_CONFIGURATION);
			throw new RuntimeException(e);
		}	
	}
	
	protected void initializeDataStore(Properties configuration, DataSource source){
		logger.info("Preparing to flyway migrate data source {} using configuration: {}", source, configuration);
		this.getFlyway().configure(configuration);
		this.getFlyway().setDataSource(source);
		
		//print out the migration statuses if debug is enabled
		if(logger.isDebugEnabled()){
			MigrationInfoService info = this.getFlyway().info();
			for(MigrationInfo migration: info.all()){
				logger.debug("{} {} {} {} {} {} {} {}", 
					migration.getVersion(), 
					migration.getType(),
					migration.getState(), 
					migration.getScript(), 
					migration.getExecutionTime(), 
					migration.getInstalledOn(), 
					migration.getChecksum(), 
					migration.getDescription()
				);
			}
		}
		
		logger.info("Executing flyway validation and migration");
		this.getFlyway().migrate();
		
	}
}
