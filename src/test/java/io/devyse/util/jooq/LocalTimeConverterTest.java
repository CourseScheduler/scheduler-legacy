/**
 * @(#) LocalTimeConverterTest.java
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

import java.time.LocalTime;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test of the LocalTimeConverter that tests to ensure that the converter properly
 * converts between java.time.LocalTime instances and String instances for persistence
 * in the database.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
@Test(	groups = {"unit","util","jooq","jooq.LocalTimeConverter"})
public class LocalTimeConverterTest {

	/**
	 * Provide testing data for the LocalTimeConverter unit tests methods.
	 * 
	 * The reciprocality requirement of {@link org.jooq.Converter} is satisfied if
	 * the to String test and the to LocalTime test both pass using the same dataset.
	 * 
	 * @return the testing data for the LocalTimeConverter
	 */
	@DataProvider(name = "localTime.values")
	public Object[][] dayOfWeekValuesProvider(){
		return new Object[][]{
				{null, null},
				{"23:59:59.999999999", LocalTime.MAX},
				{"00:00", LocalTime.MIN},
				{"12:00", LocalTime.of(12, 0, 0)},
				{"22:59:15", LocalTime.of(22, 59, 15)}
		};
	}

	/**
	 * Validate that the LocalTimeConverter properly converts from the LocalTime class to the String
	 * class used for database persistence
	 * 
	 * @param expected the expected string value as a result of the LocalTime conversion
	 * @param source the source LocalTime value for the conversion
	 */
	@Test(dataProvider = "localTime.values", groups = {"jooq.LocalTimeConverter.to"})
	public void confirmConversionToString(String expected, LocalTime source){
		LocalTimeConverter converter = new LocalTimeConverter();
		
		String converted = converter.to(source);
		Assert.assertEquals(converted, expected, "Converted LocalTime value does not equal expected String value");
	}
	
	/**
	 * Validate that the LocalTimeConverter properly converts from the String class to the LocalTime 
	 * calss used for the application data model
	 * 
	 * @param source the source String value for the conversion
	 * @param expected the expected LocalTime value as a result of the LocalTime conversion
	 */
	@Test(dataProvider = "localTime.values", groups = {"jooq.LocalTimeConverter.from"})
	public void confirmConversionToLocalTime(String source, LocalTime expected){
		LocalTimeConverter converter = new LocalTimeConverter();
		
		LocalTime converted = converter.from(source);
		Assert.assertEquals(converted, expected, "Converted String value does not equal expected LocalTime value");
	}
}
