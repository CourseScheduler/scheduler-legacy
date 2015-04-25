/**
 * @(#) OffsetDateTimeConverter.java
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

import java.time.OffsetDateTime;
import java.util.Objects;

import org.jooq.Converter;

/**
 * Provide a mechanism to automatically convert between OffsetDateTime for the application
 * data model to String for the database data model.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
public class OffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new OffsetDateTimeConverter which converts OffsetDateTime instances
	 * to String for persistence in the database.
	 */
	public OffsetDateTimeConverter() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jooq.Converter#from(java.lang.Object)
	 */
	@Override
	public OffsetDateTime from(String arg0) {
		if(arg0 == null) return null;
		return OffsetDateTime.parse(arg0);
	}

	/* (non-Javadoc)
	 * @see org.jooq.Converter#fromType()
	 */
	@Override
	public Class<String> fromType() {
		return String.class;
	}

	/**
	 * @return a value as a String such as 2014-12-03T10:15:30+01:00
	 * @see OffsetDateTime#toString()
	 * 
	 * @see org.jooq.Converter#to(java.lang.Object)
	 */
	@Override
	public String to(OffsetDateTime arg0) {
		if(arg0 == null) return null;
		return Objects.toString(arg0, null);
	}

	/* (non-Javadoc)
	 * @see org.jooq.Converter#toType()
	 */
	@Override
	public Class<OffsetDateTime> toType() {
		return OffsetDateTime.class;
	}

}
