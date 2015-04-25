/**
 * @(#) LocalDateConverterTest.java
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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test of the LocalDateConverter that tests to ensure that the converter properly
 * converts between java.time.LocalTime instances and String instances for persistence
 * in the database.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
@Test(	groups = {"unit","util","jooq","jooq.LocalDateConverter"})
public class LocalDateConverterTest {

	/**
	 * Provide testing data for the LocalDateConverter unit tests methods.
	 * 
	 * The reciprocality requirement of {@link org.jooq.Converter} is satisfied if
	 * the to String test and the to LocalDate test both pass using the same dataset.
	 * 
	 * @return the testing data for the LocalDateConverter
	 */
	@DataProvider(name = "localDate.values")
	public Object[][] dayOfWeekValuesProvider(){
		return new Object[][]{
				{null, null},
				{"+999999999-12-31", LocalDate.MAX},
				{"-999999999-01-01", LocalDate.MIN},
				{"2015-04-12", LocalDate.of(2015, 4, 12)},
				{"2008-08-15", LocalDate.of(2008, 8, 15)}
		};
	}

	/**
	 * Validate that the LocalDateConverter properly converts from the LocalDate class to the String
	 * class used for database persistence
	 * 
	 * @param expected the expected string value as a result of the LocalDate conversion
	 * @param source the source LocalDate value for the conversion
	 */
	@Test(dataProvider = "localDate.values", groups = {"jooq.LocalDateConverter.to"})
	public void confirmConversionToString(String expected, LocalDate source){
		LocalDateConverter converter = new LocalDateConverter();
		
		String converted = converter.to(source);
		Assert.assertEquals(converted, expected, "Converted LocalDate value does not equal expected String value");
	}
	
	/**
	 * Validate that the LocalDateConverter properly converts from the String class to the LocalDate 
	 * class used for the application data model
	 * 
	 * @param source the source String value for the conversion
	 * @param expected the expected LocalDate value as a result of the LocalDate conversion
	 */
	@Test(dataProvider = "localDate.values", groups = {"jooq.LocalDateConverter.from"})
	public void confirmConversionToLocalDate(String source, LocalDate expected){
		LocalDateConverter converter = new LocalDateConverter();
		
		LocalDate converted = converter.from(source);
		Assert.assertEquals(converted, expected, "Converted String value does not equal expected LocalDate value");
	}
}
