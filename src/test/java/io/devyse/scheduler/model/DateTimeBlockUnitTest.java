/**
 * @(#) DateTimeBlockUnitTest.java
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
package io.devyse.scheduler.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


/**
 * Unit tests for the AbstractDateTimeBlock base class to confirm the default
 * functionality has appropriate semantics.
 *
 * @author Mike Reinhold
 * @since 4.12.8
 * 
 */
@Test(groups = {"unit","interface","DateTimeBlock.basic"})
public class DateTimeBlockUnitTest {
	
	private static final long TEST_DURATION = 60;
	private static final long TEST_LENGTH = (long)(1.5*TEST_DURATION);
	private static final long TEST_PERIOD = 1;
	
	private static final DayOfWeek TEST_DAY = DayOfWeek.THURSDAY;
	private static final LocalTime TEST_START_TIME = LocalTime.NOON;
	private static final LocalTime TEST_END_TIME = LocalTime.NOON.plusMinutes(TEST_LENGTH);
	private static final LocalDate TEST_START_DATE = LocalDate.of(2014, 04, 30);
	private static final LocalDate TEST_END_DATE = TEST_START_DATE.plusDays(1);
	private static final ZoneOffset TEST_ZONE_1 = ZoneOffset.of("+06:00");	//Base time zone for the tests - eg. America/Chicago
	private static final ZoneOffset TEST_ZONE_2 = ZoneOffset.of("+05:00");	//Must be "earlier" than zone 1 - eg. America/Detroit
	private static final ZoneOffset TEST_ZONE_3 = ZoneOffset.of("+07:00");	//Must be "later" than zone 1 - eg. America/Denver
	
	private static final int ZONE_1_2_OFFSET = TEST_ZONE_1.getTotalSeconds()-TEST_ZONE_2.getTotalSeconds();
	private static final int ZONE_1_3_OFFSET = TEST_ZONE_3.getTotalSeconds()-TEST_ZONE_1.getTotalSeconds();
	
	/**
	 * Semantics of the DateTimeBlocks are as follows:
	 * 
	 * 		a1 & a2 are both references to the same instance
	 *  	a1 & a3 have the same field data, but are separate instances
	 * 		a1 & a4 refer to the same time, but a4 is in an earlier time zone (later local time)
	 * 		a1 & a5 refer to the same time, but a5 is in a later time zone (earlier local time)
	 *  	
	 * 		b1 has same field data as a but with an earlier day
	 * 		b2 has same field data as a but with a later day
	 * 
	 * 		c1 has same field data as a but with an earlier start time
	 * 		c2 has same field data as a but with a later start time
	 * 
	 * 		d1 has same field data as a but with an earlier end time
	 * 		d2 has same field data as a but with a later end time
	 * 
	 * 		e1 has same field data as a but with an earlier time zone
	 * 		e2 has same field data as a but with a later time zone
	 * 
	 * 		f1 has same field data as a but with an earlier start date
	 * 		f2 has same field data as a but with a later start date
	 * 
	 * 		g1 has same field data as a but with an earlier end date
	 * 		g2 has same field data as a but with a later end date
	 * 
	 * 		h1 has same field data as a but occurring immediately before (end time aligned to a's start time)
	 * 
	 * 		i1 has same field data as a but occurring in a date range prior to a
	 * 		i2 has same field data as a but occurring in a date range ending on the start of a
	 * 		i3 has same field data as a but occurring in a date range that contains a
	 */
	private SimpleDateTimeBlock a1, a2, a3, a4, a5, b1, b2, c1, c2, d1, d2, e1, e2, f1, f2, g1, g2, h1, i1, i2, i3;
		
	/**
	 * Prepare the test instances for use in the tests.
	 * 
	 * **IMPORTANT - currently, DateTimeBlock is defined to be immutable
	 * 					as a result, instances are safe to reuse
	 * 					without reinitializing them 
	 * 
	 */
	@BeforeClass
	public void setUp() {
		a1 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		a2 = a1;
		a3 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		a4 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME.minusSeconds(ZONE_1_2_OFFSET), TEST_END_TIME.plusSeconds(ZONE_1_2_OFFSET), TEST_ZONE_2, TEST_START_DATE, TEST_END_DATE);
		a5 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME.plusSeconds(ZONE_1_3_OFFSET), TEST_END_TIME.plusSeconds(ZONE_1_3_OFFSET), TEST_ZONE_3, TEST_START_DATE, TEST_END_DATE);
		
		b1 = new SimpleDateTimeBlock(TEST_DAY.minus(1), TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		b2 = new SimpleDateTimeBlock(TEST_DAY.plus(1), TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		
		c1 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME.minusMinutes(TEST_DURATION), TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		c2 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME.plusMinutes(TEST_DURATION), TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		
		d1 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME.minusMinutes(TEST_DURATION), TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		d2 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME.plusMinutes(TEST_DURATION), TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		
		e1 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_2, TEST_START_DATE, TEST_END_DATE);
		e2 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_3, TEST_START_DATE, TEST_END_DATE);
		
		f1 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE.minusDays(1), TEST_END_DATE);
		f2 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE.plusDays(1), TEST_END_DATE);
		
		g1 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE.minusDays(TEST_PERIOD));
		g2 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE.plusDays(TEST_PERIOD));
		
		h1 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME.minusMinutes(TEST_LENGTH), TEST_END_TIME.minusMinutes(TEST_LENGTH), TEST_ZONE_1, TEST_START_DATE, TEST_END_DATE);
		
		i1 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE.minusDays(2*TEST_PERIOD), TEST_START_DATE.minusDays(TEST_PERIOD));
		i2 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE.minusDays(TEST_PERIOD), TEST_START_DATE);
		i3 = new SimpleDateTimeBlock(TEST_DAY, TEST_START_TIME, TEST_END_TIME, TEST_ZONE_1, TEST_START_DATE.minusDays(TEST_PERIOD), TEST_END_DATE.plusDays(TEST_PERIOD));
	}
		
	/**
	 * Confirm the semantics of reference and object equality.
	 * 
	 * Note:
	 * 	"Sameness" is via instance equality (via ==)
	 * 	"Equality" is object semantic equality (via equals)
	 * 	UTC equivalent times in different zones are not equal {@link java.time.chrono.ChronoZonedDateTime#equals(Object)}
	 * 
	 */
	@Test
	public void confirmEquality() {
		SoftAssert eq = new SoftAssert();
		
		eq.assertSame(a1, a2, "References to same instance should be the same");
		eq.assertEquals(a1, a2, "References to same instance should be equal");
		eq.assertEquals(a1, a3, "Instances with same uniqueness fields should be equal");
		
		eq.assertNotEquals(a1, null, "Non-null instance should not be equal to null");		
		eq.assertNotEquals(a1, a4, "Instances with UTC equivalent times in different zones should not be equal");
		eq.assertNotEquals(a1, b1, "Instances with varying days of week should not be equal");
		eq.assertNotEquals(a1, c1, "Instances with varying start times should not be equal");
		eq.assertNotEquals(a1, d1, "Instances with varying end times should not be equal");
		eq.assertNotEquals(a1, e1, "Instances with varying time zones should not be equal");
		eq.assertNotEquals(a1, f1, "Instances with varying start dates should not be equal");
		eq.assertNotEquals(a1, g1, "Instances with varying end dates should not be equal");
						
		eq.assertAll();
	}

	/**
	 * Confirm hashCode semantics is consistent with equals()
	 * 
	 * Note: 
	 * 	It is not required that "unequal" objects return different hashcodes,
	 * 	however "equal" and "same" objects must return the same hashcode. That said,
	 * 	including a test for "variety" between hashcodes so that all instances don't 
	 * 	return the same value (would kill performance of data structures that use the
	 * 	hashcode)
	 *
	 */
	@Test
	public void confirmHashCode() {
		SoftAssert hc = new SoftAssert();
		
		hc.assertEquals(a1.hashCode(), a2.hashCode(), "References to same instance should have same hashcode. Hashcode not consistent across calls");
		hc.assertEquals(a1.hashCode(), a3.hashCode(), "Instances with same uniqueness fields should have same hashcode. Hashcode not consistent with equals()");
		
		//ensure that our sample dataset, which has variety in its field content,
		//has some variation in its hashcode. No variation in hash would be bad 
		//this is not sufficient on its own, a good hash should have a uniform, 
		//non-clustering distribution
		int a1Code = a1.hashCode();
		boolean variety = a1Code == b1.hashCode() &&
				a1Code == b2.hashCode() &&
				a1Code == c1.hashCode() &&
				a1Code == c2.hashCode() &&
				a1Code == d1.hashCode() &&
				a1Code == d2.hashCode() &&
				a1Code == e1.hashCode() &&
				a1Code == e2.hashCode() &&
				a1Code == f1.hashCode() &&
				a1Code == f2.hashCode() &&
				a1Code == g1.hashCode() &&
				a1Code == g2.hashCode()
		;
		hc.assertFalse(variety, "Hashcode should return a variety of v, ZoneOffset.ofHours(generator.nextInt(19))alues for instance with varying uniqueness fields");
				
		hc.assertAll();
	}
	
	/**
	 * Confirm the quality of the hashCode method meets some minimum standards - 
	 * will avoid some too many instances hashing to the same value for a single
	 * hash as well as that the average number of collisions per hash is under
	 * a specified value.
	 */
	@Test
	public void confirmHashCodeQuality_RandomData(){
		HashCodeQualityHelper.confirmHashCodeQuality( 
				(Random r) -> {return DateTimeBlockUnitTest.generateDateTimeBlock(r);}
		);
	}
	
	/**
	 * Confirm the quality of the hashCode method meets some minimum standards - 
	 * will avoid some too many instances hashing to the same value for a single
	 * hash as well as that the average number of collisions per hash is under
	 * a specified value.
	 */
	@Test
	public void confirmHashCodeQuality_RealisticData(){
		HashCodeQualityHelper.confirmHashCodeQuality(Arrays.asList(
				a1, a2, a3, a4, a5, b1, b2, c1, c2, d1, d2, e1, e2, f1, f2, g1, g2, h1, i1, i2, i3
		));
	}
	
	/**
	 * Generate a DateTimeBlock based on the current state of a Random
	 *
	 * @param generator a Random for use in building the DateTimeBlocks
	 * @return the next DateTimeBlock
	 */
	public static DateTimeBlock generateDateTimeBlock(Random generator){
		return new SimpleDateTimeBlock(
				DayOfWeek.of(Math.abs(generator.nextInt() % DayOfWeek.values().length)+1),
				LocalTime.of(Math.abs(generator.nextInt(24)), 
						Math.abs(generator.nextInt(60)), Math.abs(generator.nextInt(60)), 
						Math.abs(generator.nextInt(1000000000))),
				LocalTime.of(Math.abs(generator.nextInt(24)), 
						Math.abs(generator.nextInt(60)), Math.abs(generator.nextInt(60)), 
						Math.abs(generator.nextInt(1000000000))),
				ZoneOffset.ofHours(generator.nextInt(19)),
				LocalDate.ofEpochDay(Math.abs(generator.nextInt(1000000000))),
				LocalDate.ofEpochDay(Math.abs(generator.nextInt(1000000000)))
		);
	}
	
	/**
	 * Confirm the semantics of compareTo is consistent with equals() 
	 * and has appropriate object semantics. Only exception to consistency
	 * with equals() is where object semantics yield equal times but equals()
	 * yields different instances: same UTC time represented in 2 different
	 * zones yield compareTo == 0 but equals == false.
	 * 
	 * Note:
	 * 	Order for AbstractDateTimeBlock implies the following:
	 * 		Start date is the first ordering element
	 * 		Sunday < Monday < Tuesday < Wednesday < Thursday < Friday < Saturday
	 * 		Start and end time are compared first as UTC then with local time to preserve consistency with equals
	 * 		Start time takes priority over end time
	 * 		End time only matters when start time is the same
	 *		End date is the last ordering component
	 */
	@Test
	public void confirmCompareTo() {
		SoftAssert ct = new SoftAssert();
		
		ct.assertEquals(a1.compareTo(a2), 0, "References to same instance should be equal");
		ct.assertEquals(a2.compareTo(a1), 0, "References to same instance should be equal, regardless of comparison direction");
		ct.assertEquals(a1.compareTo(a3), 0, "CompareTo should be consistent with equals() for equal instances");
		ct.assertEquals(a3.compareTo(a1), 0, "CompareTo should be consistent with equals() for equal instance, regardless of comparison direction");
		
		ct.assertNotEquals(a1.compareTo(b1), 0, "CompareTo should be consistent with equals() for non-equal instance");
		ct.assertNotEquals(b1.compareTo(a1), 0, "CompareTo should be consistent with equals() for non-equal instance, regardless of comparison direction");
		ct.assertEquals(Math.signum(a1.compareTo(b1)), Math.signum(-b1.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (lesser day of week)");
		ct.assertEquals(Math.signum(a1.compareTo(b2)), Math.signum(-b2.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (greater day of week)");
		ct.assertEquals(Math.signum(a1.compareTo(c1)), Math.signum(-c1.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (lesser start time)");
		ct.assertEquals(Math.signum(a1.compareTo(c2)), Math.signum(-c2.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (greater start time)");
		ct.assertEquals(Math.signum(a1.compareTo(d1)), Math.signum(-d1.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (lesser end time))");
		ct.assertEquals(Math.signum(a1.compareTo(d2)), Math.signum(-d2.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (greater end time)");
		ct.assertEquals(Math.signum(a1.compareTo(e1)), Math.signum(-e1.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (lesser zone)");
		ct.assertEquals(Math.signum(a1.compareTo(e2)), Math.signum(-e2.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (greater zone)");
		ct.assertEquals(Math.signum(a1.compareTo(f1)), Math.signum(-f1.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (lesser start date)");
		ct.assertEquals(Math.signum(a1.compareTo(f2)), Math.signum(-f2.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (greater start date)");
		ct.assertEquals(Math.signum(a1.compareTo(g1)), Math.signum(-g1.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (lesser end date)");
		ct.assertEquals(Math.signum(a1.compareTo(g2)), Math.signum(-g2.compareTo(a1)), "CompareTo should reverse sign for reversed comparison direction of non-equal instances (greater end date)");
				
		ct.assertEquals(Math.signum(a1.compareTo(b1)), 1.0f, "Positive result expected for instance with lesser day of week");
		ct.assertEquals(Math.signum(a1.compareTo(c1)), 1.0f, "Positive result expected for instance with lesser start time");
		ct.assertEquals(Math.signum(a1.compareTo(d1)), 1.0f, "Positive result expected for instance with lesser end time");
		ct.assertEquals(Math.signum(a1.compareTo(e1)), -1.0f, "Negative result expected for instance with lesser zone");	//timezones are sorted descending
		ct.assertEquals(Math.signum(a1.compareTo(f1)), 1.0f, "Positive result expected for instance with lesser start date");
		ct.assertEquals(Math.signum(a1.compareTo(g1)), 1.0f, "Positive result expected for instance with lesser end date");
		ct.assertEquals(Math.signum(a1.compareTo(a4)), 1.0f, "Positive result expected for instance with same UTC but lesser zone");

		ct.assertEquals(Math.signum(a1.compareTo(b2)), -1.0f, "Negative result expected for instance with greater day of week");
		ct.assertEquals(Math.signum(a1.compareTo(c2)), -1.0f, "Negative result expected for instance with greater start time");
		ct.assertEquals(Math.signum(a1.compareTo(d2)), -1.0f, "Negative result expected for instance with greater end time");
		ct.assertEquals(Math.signum(a1.compareTo(e2)), 1.0f, "Positive result expected for instance with greater zone");	//timezones are sorted descending
		ct.assertEquals(Math.signum(a1.compareTo(f2)), -1.0f, "Negative result expected for instance with greater start date");
		ct.assertEquals(Math.signum(a1.compareTo(g2)), -1.0f, "Negative result expected for instance with greater end date");
		ct.assertEquals(Math.signum(a1.compareTo(a5)), -1.0f, "Negative result expected for instance with same UTC but greater zone");
		
		ct.assertEquals(Math.signum(b1.compareTo(a1)), Math.signum(a1.compareTo(b2)), "Transitivity expected for instances differing on day of week");
		ct.assertEquals(Math.signum(c1.compareTo(a1)), Math.signum(a1.compareTo(c2)), "Transitivity expected for instances differing on start");
		ct.assertEquals(Math.signum(d1.compareTo(a1)), Math.signum(a1.compareTo(d2)), "Transitivity expected for instances differing on end");
		ct.assertEquals(Math.signum(e1.compareTo(a1)), Math.signum(a1.compareTo(e2)), "Transitivity expected for instances differing on zone");
		
		ct.assertAll();
	}
	
	/**
	 * Confirm that the duration between start and end times is calculated properly
	 *
	 */
	@Test
	public void confirmDuration() {
		Assert.assertEquals(a1.getDuration().toMinutes(), (long)(1.5*TEST_DURATION), "Duration between start and end times did not match expected number of minutes");
	}
	
	/**
	 * Confirm that the duration between stand and end dates is calculated properly
	 *
	 */
	@Test
	public void confirmPeriod() {
		Assert.assertEquals(a1.getPeriod().getDays(), TEST_PERIOD, "Period between start and end dates did not match expected number of days");
	}
	
	/**
	 * Confirm that day of the week overlap checks function properly
	 * 
	 * Days of the week "overlap" if both DateTimeBlocks have the same
	 * day of the week
	 *
	 */
	@Test
	public void confirmDayOfWeekOverlap() {
		SoftAssert ol = new SoftAssert();
		
		//day of week matches should overlap
		ol.assertEquals(a1.dayOfWeekOverlapsWith(a2), true, "DateTimeBlocks with same day of week should overlap");
		ol.assertEquals(a1.dayOfWeekOverlapsWith(a3), true, "DateTimeBlocks with same day of week should overlap");
		ol.assertEquals(a1.dayOfWeekOverlapsWith(a4), true, "DateTimeBlocks with same day of week should overlap");
		ol.assertEquals(a1.dayOfWeekOverlapsWith(a5), true, "DateTimeBlocks with same day of week should overlap");

		//day of week differing should not overlap
		ol.assertEquals(a1.dayOfWeekOverlapsWith(b1), false, "DateTimeBlocks on different days should not overlap");
		ol.assertEquals(b1.dayOfWeekOverlapsWith(a1), false, "DateTimeBlocks on different days should not overlap");
		ol.assertEquals(a1.dayOfWeekOverlapsWith(b2), false, "DateTimeBlocks on different days should not overlap");
		ol.assertEquals(b2.dayOfWeekOverlapsWith(a1), false, "DateTimeBlocks on different days should not overlap");

		ol.assertAll();
	}
	
	/**
	 * Confirm that date range overlap checks function properly
	 * 
	 * Date ranges "overlap" if any date is shared between the two
	 * date ranges.
	 *
	 */
	@Test
	public void confirmDateRangeOverlap() {
		SoftAssert ol = new SoftAssert();
		
		//same date range should overlap
		ol.assertEquals(a1.dateOverlapsWith(a2), true, "DateTimeBlocks with same date range should overlap");
		ol.assertEquals(a1.dateOverlapsWith(a3), true, "DateTimeBlocks with same date range should overlap");
		ol.assertEquals(a1.dateOverlapsWith(a4), true, "DateTimeBlocks with same date range should overlap");
		ol.assertEquals(a1.dateOverlapsWith(a5), true, "DateTimeBlocks with same date range should overlap");

		//check different (non-sharing) ranges don't overlap
		ol.assertEquals(a1.dateOverlapsWith(i1), false, "DateTimeBlocks with non-sharing date range should not overlap");
		ol.assertEquals(i1.dateOverlapsWith(a1), false, "DateTimeBlocks with non-sharing date range should not overlap");
		
		//check start/end date aligning ranges (where the start of one is the end of another) overlap
		ol.assertEquals(a1.dateOverlapsWith(i2), true, "DateTimeBlocks with aligned start & end dates should overlap");
		ol.assertEquals(i2.dateOverlapsWith(a1), true, "DateTimeBlocks with aligned start & end dates should overlap");
		
		//check offset ranges that share dates overlap
		ol.assertEquals(a1.dateOverlapsWith(i3), true, "DateTimeBlocks with shared date range should overlap");
		ol.assertEquals(i3.dateOverlapsWith(a1), true, "DateTimeBlocks with shared date range should overlap");
		
		ol.assertAll();
	}
	
	/**
	 * Confirm that time block overlap checks function properly
	 * 
	 * Time blocks "overlap" if two DateTimeBlocks share a start or end time or
	 * if the start time for one of the two DateTimeBlocks is in between the
	 * start and end time of the other period.
	 * 
	 * The start time of one period is allowed to be the same as the end
	 * time of another period without being considered as "overlapping"
	 * (this represents a "zero-passing period" - a potentially valid 
	 * scenario for some universities)
	 */
	@Test
	public void confirmTimeBlockOverlap() {
		SoftAssert ol = new SoftAssert();

		//time blocks with shared start or end should overlap
		ol.assertEquals(a1.timeOverlapsWith(a2), true, "DateTimeBlocks with same time block should overlap");
		ol.assertEquals(a1.timeOverlapsWith(a3), true, "DateTimeBlocks with same time block should overlap");
		ol.assertEquals(a1.timeOverlapsWith(a4), true, "DateTimeBlocks with semantically equivalent time blocks should overlap");
		ol.assertEquals(a1.timeOverlapsWith(a5), true, "DateTimeBlocks with semantically equivalent time blocks should overlap");

		//shared start should overlap (shared end is same as next set - start is inclusive)
		ol.assertEquals(a1.timeOverlapsWith(d1), true, "DateTimeBlocks that share only a start time overlap");
		ol.assertEquals(a1.timeOverlapsWith(d2), true, "DateTimeBlocks that share only a start time overlap");
		
		//time block is inclusive of other time block's start or other time block is inclusive of first block's start should overlap
		ol.assertEquals(a1.timeOverlapsWith(c1), true, "This DateTimeBlock with start time between other DateTimeBlock start and end times should overlap");
		ol.assertEquals(a1.timeOverlapsWith(c2), true, "Other DateTimeBlock with start time between this DateTimeBlock start and end times should overlap");
		
		//neither start time is inclusive of the other block (and not the same as other start time) should not overlap
		ol.assertEquals(d1.timeOverlapsWith(c2), false, "Neither start nor end time of other DateTimeBlock between this DateTimeBlock start and end times should not overlap");
		ol.assertEquals(c2.timeOverlapsWith(d1), false, "Neither start nor end time of other DateTimeBlock between this DateTimeBlock start and end times should not overlap");

		//start time of one matches end time of another should not overlap - zero passing periods are allowed
		ol.assertEquals(a1.timeOverlapsWith(h1), false, "Periods that share a end or start time only should not overlap (zero passing period allowed)");
		ol.assertEquals(h1.timeOverlapsWith(a1), false, "Periods that share a end or start time only should not overlap (zero passing period allowed)");
		
		ol.assertAll();
	}
	
	/**
	 * Confirm that DateTimeBlock overlaps are properly detected.
	 * 
	 * DateTimeBlocks overlap if they occur on the same day and there is at least
	 * one minute that is present in both DateTimeBlocks (timeline - local time + timezone) 
	 *
	 */
	@Test
	public void confirmOverlap() {
		SoftAssert ol = new SoftAssert();
		
		//all fiends match should overlap
		ol.assertEquals(a1.overlapsWith(a2), true, "DateTimeBlocks with same fields should overlap");
		ol.assertEquals(a1.overlapsWith(a3), true, "DateTimeBlocks with same fields should overlap");
		ol.assertEquals(a1.overlapsWith(a4), true, "DateTimeBlocks with semantically equivalent fields should overlap");
		ol.assertEquals(a1.overlapsWith(a5), true, "DateTimeBlocks with semantically equivalent fields should overlap");
		
		//check different day of week makes DateTimeBlock not overlap
		ol.assertEquals(a1.overlapsWith(b1), false, "DateTimeBlocks with different day of week should not overlap");
		
		//check different date range makes DateTimeBlock not overlap
		ol.assertEquals(a1.overlapsWith(i1), false, "DateTimeBlocks with different date range should not overlap");
		
		//check different time block makes DateTimeBlock not overlap
		ol.assertEquals(a1.overlapsWith(h1), false, "DateTimeBlocks with different time block should not overlap");;
						
		ol.assertAll();
	}
}
