/**
 * @(#) KeenUtils.java
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

import io.devyse.scheduler.security.Privacy;
import io.keen.client.java.KeenLogging;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Keen utility methods for building events and mapping data to events
 * 
 * @author Mike Reinhold
 * @since 4.12.5
 */
class KeenUtils {
	
	/**
	 * Static logger
	 */
	private static final XLogger logger = XLoggerFactory.getXLogger(KeenUtils.class);

	/**
	 * Keen IO internally uses JSON to represent events. The Keen IO Java API requires that a
	 * nested map, essentially identical in structure to a JSON document, is sent as an event.
	 * Since flat maps are not allowed, the map keys within each level cannot include ".". 
	 * 
	 * Value: {@value}
	 */
	protected static final String MAP_SCOPING_DELIMITER = ".";
	
	/**
	 * The key used to represent the value of an entry at the current scope or level. This is 
	 * modeled after DNS usage of "@" as the entry for the current domain or scope.
	 * 
	 * This is used automatically by the nested map methods if there are other values at the 
	 * same level in the nested map.
	 * 
	 * Value: {@value}
	 */
	protected static final String MAP_LEVEL_VALUE_KEY = "@";
	
	/**
	 * Convert the specified flat map into a nested map for use in a Keen event.
	 * 
	 * @param flatMap the flat map of event data
	 * 
	 * @return a nested map containing the same event data as the flat map 
	 */
	protected static Map<String, Object> createNestedMap(Map<String, Object> flatMap){
		Map<String, Object> nestedMap = new HashMap<String, Object>();
		
		for(Entry<String, Object> entry: flatMap.entrySet()){
			Object value = entry.getValue();
			try{
				//if the value at this key is a map, we need to nest that submap as well
				@SuppressWarnings("unchecked")	//catch cast exception below
				Map<String, Object> subMap = (Map<String, Object>)value;
				value = createNestedMap(subMap);
			}catch(ClassCastException e){}
			
			addNestedMapEntry(nestedMap, entry.getKey(), value);
		}
		return nestedMap;
	}
	
	/**
	 * Add the value into the nested map via the key. A nested map is similar to a JSON
	 * document. 
	 * 
	 * @param map the nested map in which to insert the value at the key
	 * @param key the key specifying the location in the nested map in which to insert the value
	 * @param value the object to insert into the nested map
	 */
	protected static void addNestedMapEntry(Map<String, Object> map, String key, Object value){
		if(key.contains(MAP_SCOPING_DELIMITER)){	//check if the key contains multiple levels
			int left = key.indexOf(MAP_SCOPING_DELIMITER);
			String parent = key.substring(0, left);
			String child = key.substring(left+1, key.length());
			nestMapEntry(map, parent, child, value);
		}else{
			redactAndAddMapEntry(map, key, value);
		}
	}
	
	/**
	 * Add the value into the nested map under the parent level using the specified child key. 
	 * 
	 * @param map the nested map containing the parent scope directly
	 * @param parent the key for the parent scope
	 * @param child the key into the parent scope 
	 * @param value the object to insert into the nested map
	 */
	protected static void nestMapEntry(Map<String, Object> map, String parent, String child, Object value){
		try{
			//check if the value associated with the parent scope is a map
			@SuppressWarnings("unchecked")	//ok to suppress, exception caught and handled below
			Map<String, Object> subMap = (Map<String,Object>)map.get(parent);

			//if it doesn't exist, create it and insert it as the parent
			if(subMap == null){
				subMap = new HashMap<String, Object>();
				map.put(parent, subMap);
			}
			
			//add the value into the submap using the child key
			addNestedMapEntry(subMap, child, value);
		}catch(ClassCastException e){
			nestRemapEntry(map, parent, child, value);
		}
	}
	
	/**
	 * Remap an existing value at the parent key into the root value of a new scope inserted into the main
	 * map at the parent key. The value will be inserted into the new scope at the child key.
	 * 
	 * @param map containing the parent entry which needs to be remapped as the root of a new scope
	 * @param parent the key at this level of the nested map
	 * @param child the key for the new value in the new scope
	 * @param value the object being inserted into the new scope
	 */
	protected static void nestRemapEntry(Map<String, Object> map, String parent, String child, Object value){
		//retrieve the current value so we can remap it
		Object current = map.remove(parent);
		
		//create the new scope and map it into the parent key
		Map<String, Object> subMap = new HashMap<String,Object>();
		map.put(parent, subMap);
		
		//readd the original entry at the root of the new scope and the new value into the child key of the scope
		redactAndAddMapEntry(subMap, MAP_LEVEL_VALUE_KEY, current);
		addNestedMapEntry(subMap, child, value);
	}
	
	/**
	 * Redact the value, if possible, and add it into the map using the specified key.
	 * 
	 * @param map the nested map which will contain the object
	 * @param key the key in the nested map for the value
	 * @param value the object being inserted into the scope
	 */
	protected static void redactAndAddMapEntry(Map<String, Object> map, String key, Object value){
		if(value instanceof String){			
			//at this point in time, only String can have private information redacted
			map.put(key, Privacy.redactPrivateInformation(value.toString()));
		}else{
			map.put(key, value);
		}
	}
	
	/**
	 * The method unregisters Keen's default JUL log handler to allow the application to have more control over
	 * how the KeenClient logs.
	 * 
	 * @since 4.12.6
	 */
	protected static void disableKeenDefaultLogHandler(){
		try{
			Class<KeenLogging> clazz = KeenLogging.class;
			Field loggerField = clazz.getDeclaredField("LOGGER");
			Field handlerField = clazz.getDeclaredField("HANDLER");
			
			loggerField.setAccessible(true);
			handlerField.setAccessible(true);
			
			java.util.logging.Logger keenLogger = (java.util.logging.Logger)loggerField.get(null);
			java.util.logging.Handler keenHandler = (java.util.logging.Handler)handlerField.get(null);
			
			keenLogger.removeHandler(keenHandler);
			logger.debug("Removed KeenClient default JUL log handler {} from KeenLogging logger {}", keenHandler, keenLogger);
		}catch(Exception e){
			logger.error("Unable to remove KeenClient default JUL log handler from KeenLogging logger", e);
		}
	}
}
