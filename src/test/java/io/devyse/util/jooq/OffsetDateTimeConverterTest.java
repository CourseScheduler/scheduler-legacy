/**
 * @(#) OffsetDateTimeConverterTest.java
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
import java.time.ZoneOffset;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test of the OffsetDateTimeConverter that tests to ensure that the converter properly
 * converts between java.time.OffsetDateTime instances and String instances for persistence
 * in the database.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
@Test(	groups = {"unit","util","jooq","jooq.OffsetDateTimeConverter"})
public class OffsetDateTimeConverterTest {

	/**
	 * Provide testing data for the OffsetDateTimeConverter unit tests methods.
	 * 
	 * The reciprocality requirement of {@link org.jooq.Converter} is satisfied if
	 * the to String test and the to OffsetDateTime test both pass using the same dataset.
	 * 
	 * @return the testing data for the OffsetDateTimeConverter
	 */
	@DataProvider(name = "offsetDateTime.values")
	public Object[][] dayOfWeekValuesProvider(){
		return new Object[][]{
				{null, null},
				{"+999999999-12-31T23:59:59.999999999-18:00", OffsetDateTime.MAX},
				{"-999999999-01-01T00:00+18:00", OffsetDateTime.MIN},
				{"2015-04-19T12:00Z", OffsetDateTime.of(2015, 4, 19, 12, 0, 0, 0, ZoneOffset.UTC)},
				{"2008-08-15T22:59:17.123456789-05:00", OffsetDateTime.of(2008, 8, 15, 22, 59, 17, 123456789, ZoneOffset.ofHours(-5))}
		};
	}

	/**
	 * Validate that the OffsetDateTimeConverter properly converts from the OffsetDateTime class to the String
	 * class used for database persistence
	 * 
	 * @param expected the expected string value as a result of the OffsetDateTime conversion
	 * @param source the source OffsetDateTime value for the conversion
	 */
	@Test(dataProvider = "offsetDateTime.values", groups = {"jooq.OffsetDateTimeConverter.to"})
	public void confirmConversionToString(String expected, OffsetDateTime source){
		OffsetDateTimeConverter converter = new OffsetDateTimeConverter();
		
		String converted = converter.to(source);
		Assert.assertEquals(converted, expected, "Converted OffsetDateTime value does not equal expected String value");
	}
	
	/**
	 * Validate that the OffsetDateTimeConverter properly converts from the String class to the OffsetDateTime 
	 * class used for the application data model
	 * 
	 * @param source the source String value for the conversion
	 * @param expected the expected OffsetDateTime value as a result of the OffsetDateTime conversion
	 */
	@Test(dataProvider = "offsetDateTime.values", groups = {"jooq.OffsetDateTimeConverter.from"})
	public void confirmConversionToOffsetDateTime(String source, OffsetDateTime expected){
		OffsetDateTimeConverter converter = new OffsetDateTimeConverter();
		
		OffsetDateTime converted = converter.from(source);
		Assert.assertEquals(converted, expected, "Converted String value does not equal expected OffsetDateTime value");
	}
}
