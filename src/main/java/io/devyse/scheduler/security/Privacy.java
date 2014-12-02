/**
 * @(#) Privacy.java
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Methods and utilities for ensuring that user private data remains private.
 * 
 * @author Mike Reinhold
 * @since 4.12.5
 */
public class Privacy {

	/**
	 * Map of private data values to placeholders for use during private data redaction.
	 */
	private static Map<String, String> redactValues;

	/**
	 * Initialization of the redacted values map
	 */
	static {
		redactValues = new HashMap<>();
		
		redactValues.put(System.getProperty("user.name"), "{user.name}");
	}
	
	/**
	 * Redact private information out of the provided string and replace it with
	 * a placeholder. The redacted string is returned.
	 * 
	 * @param string the string from which to redact private information
	 * @return the redacted form of the input string
	 */
	public static String redactPrivateInformation(String string){
		String result = string;
		for(Entry<String, String> item: redactValues.entrySet()){
			result = result.replaceAll(item.getKey(), item.getValue());
		}
		return result;
	}
	
}
