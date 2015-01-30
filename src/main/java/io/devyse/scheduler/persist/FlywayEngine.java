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

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
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
	protected FlywayEngine() {
		super();
		
		this.setFlyway(new Flyway());
	}
	
	protected void initializeDataStore(DataSource source){
		this.getFlyway().setDataSource(source);
		
		//TODO custom configuration
		
		this.getFlyway().migrate();
		
		this.getFlyway().validate();
	}
}
