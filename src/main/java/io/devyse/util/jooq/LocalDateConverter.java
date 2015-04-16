/**
 * @(#) LocalDateConverter.java
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
package io.devyse.util.jooq;

import java.time.LocalDate;
import java.util.Objects;

import org.jooq.Converter;

/**
 * Provide a mechanism to automatically convert between LocalTime for the application
 * data model to String for the database data model.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class LocalDateConverter implements Converter<String, LocalDate> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new LocalDateConverter which converts LocalDate instances to 
	 * strings for persistence in the database
	 */
	public LocalDateConverter() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jooq.Converter#from(java.lang.Object)
	 */
	@Override
	public LocalDate from(String arg0) {
		if (arg0 == null) return null;
		return LocalDate.parse(arg0);
	}

	/* (non-Javadoc)
	 * @see org.jooq.Converter#fromType()
	 */
	@Override
	public Class<String> fromType() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see org.jooq.Converter#to(java.lang.Object)
	 */
	@Override
	public String to(LocalDate arg0) {
		return Objects.toString(arg0);
	}

	/* (non-Javadoc)
	 * @see org.jooq.Converter#toType()
	 */
	@Override
	public Class<LocalDate> toType() {
		return LocalDate.class;
	}

}
