/**
 * @(#) DayOfWeekConverterTest.java
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

import java.time.DayOfWeek;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test of the DayOfWeekConverter that tests to ensure that the converter properly
 * converts between java.time.DayOfWeek instances and Integer instances for persistence
 * in the database.
 * 
 * @author Mike Reinhold
 * @since 4.13.0
 */
@Test(	groups = {"unit","util","jooq","jooq.DayOfWeekConverter"})
public class DayOfWeekConverterTest {

	/**
	 * Provide testing data for the DayOfWeekConverter unit tests methods.
	 * 
	 * The reciprocality requirement of {@link org.jooq.Converter} is satisfied if
	 * the to Integer test and the to DayOfWeek test both pass using the same dataset.
	 * 
	 * @return the testing data for the DayOfweekConverter
	 */
	@DataProvider(name = "dayOfWeek.values")
	public Object[][] dayOfWeekValuesProvider(){
		return new Object[][]{
				{null, null},
				{1, DayOfWeek.MONDAY},
				{2, DayOfWeek.TUESDAY},
				{3, DayOfWeek.WEDNESDAY},
				{4, DayOfWeek.THURSDAY},
				{5, DayOfWeek.FRIDAY},
				{6, DayOfWeek.SATURDAY},
				{7, DayOfWeek.SUNDAY}	
		};
	}
	
	/**
	 * Validate that the DayOfWeekConverter properly converts from the DayOfWeek class
	 * to the Integer class used for the database persistence.
	 * 
	 * @param expected the Integer value that is the expected result of the conversion
	 * @param source the DayOfWeek value that is the source for the conversion
	 */
	@Test(dataProvider = "dayOfWeek.values", groups = {"jooq.DayOfWeekConverter.to"})
	public void confirmConversionToInteger(Integer expected, DayOfWeek source){
		DayOfWeekConverter converter = new DayOfWeekConverter();
		
		Integer converted = converter.to(source);
		Assert.assertEquals(converted, expected, "Converted DayOfWeek value does not equal expected Integer value");
	}
	
	/**
	 * Validate that the DayOfWeekConverter properly converts from the Integer class
	 * to the DayOfWeek class used in the application data model
	 * 
	 * @param source the Integer value that is the source for the conversion
	 * @param expected the DayOfWeek value that is the expected result of the conversion
	 */
	@Test(dataProvider = "dayOfWeek.values", groups = {"jooq.DayOfWeekConverter.from"})
	public void confirmConversionToDayOfWeek(Integer source, DayOfWeek expected){
		DayOfWeekConverter converter = new DayOfWeekConverter();
		
		DayOfWeek converted = converter.from(source);
		Assert.assertEquals(converted, expected, "Converted Integer value does not equal expected DayOfWeek value");
	}
}
