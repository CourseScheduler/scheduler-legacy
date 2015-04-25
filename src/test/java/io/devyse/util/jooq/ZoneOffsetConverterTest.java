/**
 * @(#) ZoneOffsetConverterTest.java
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

import java.time.ZoneOffset;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test of the ZoneOffsetConverter that tests to ensure that the converter properly
 * converts between java.time.ZoneOffset instances and String instances for persistence
 * in the database.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 *
 */
@Test(	groups = {"unit","util","jooq","jooq.ZoneOffsetConverter"})
public class ZoneOffsetConverterTest {

	/**
	 * Provide testing data for the ZoneOffsetConverter unit tests methods.
	 * 
	 * The reciprocality requirement of {@link org.jooq.Converter} is satisfied if
	 * the to String test and the to ZoneOffset test both pass using the same dataset.
	 * 
	 * @return the testing data for the ZoneOffsetConverter
	 */
	@DataProvider(name = "zoneOffset.values")
	public Object[][] dayOfWeekValuesProvider(){
		return new Object[][]{
				{null, null},
				{"+18:00", ZoneOffset.MAX},
				{"-18:00", ZoneOffset.MIN},
				{"Z", ZoneOffset.UTC},
				{"+08:00", ZoneOffset.ofHours(8)},
				{"-05:29:59", ZoneOffset.ofHoursMinutesSeconds(-5, -29, -59)}
		};
	}

	/**
	 * Validate that the ZoneOffsetConverter properly converts from the ZoneOffset class to the String
	 * class used for database persistence
	 * 
	 * @param expected the expected string value as a result of the ZoneOffset conversion
	 * @param source the source ZoneOffset value for the conversion
	 */
	@Test(dataProvider = "zoneOffset.values", groups = {"jooq.ZoneOffsetConverter.to"})
	public void confirmConversionToString(String expected, ZoneOffset source){
		ZoneOffsetConverter converter = new ZoneOffsetConverter();
		
		String converted = converter.to(source);
		Assert.assertEquals(converted, expected, "Converted ZoneOffset value does not equal expected String value");
	}
	
	/**
	 * Validate that the ZoneOffsetConverter properly converts from the String class to the ZoneOffset 
	 * class used for the application data model
	 * 
	 * @param source the source String value for the conversion
	 * @param expected the expected ZoneOffset value as a result of the ZoneOffset conversion
	 */
	@Test(dataProvider = "zoneOffset.values", groups = {"jooq.ZoneOffsetConverter.from"})
	public void confirmConversionToZoneOffset(String source, ZoneOffset expected){
		ZoneOffsetConverter converter = new ZoneOffsetConverter();
		
		ZoneOffset converted = converter.from(source);
		Assert.assertEquals(converted, expected, "Converted String value does not equal expected ZoneOffset value");
	}
}
