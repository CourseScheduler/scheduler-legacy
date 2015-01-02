/**
 * @(#) SectionUnitTest.java
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

import java.util.Arrays;
import java.util.Random;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Unit tests for the Section and AbstractSection classes and base functionality
 *
 * @author Mike Reinhold
 *
 */
@Test(	groups = {"unit","interface","Section.basic"},
		dependsOnGroups = {"Meeting.basic", "Term.basic"} 
)
public class SectionUnitTest {
	
	/**
	 * Small, medium, and large values for use in building Sections
	 */
	private static final String less = "1";
	private static final String middle = "5";
	private static final String more = "9";
	private static final Term lesserTerm = new SimpleTerm(new SimpleUniversity("1"), "1", "1");
	private static final Term middleTerm = new SimpleTerm(new SimpleUniversity("5"), "5", "5");
	private static final Term greaterTerm = new SimpleTerm(new SimpleUniversity("9"), "9", "9");
	
	/**
	 * Sections for use in testing base functions
	 * 
	 * Real datasets have the natural constraint that no crn can be reused for multiple
	 * combinations of courseId & sectionId because a crn is unique for an entire term.
	 * As a result, courseId and sectionId fields are not considered for uniqueness and
	 * are not tested in the equals or hashcode unit tests. However, the compareTo method
	 * uses the courseId and sectionId in place of the crn
	 * 
	 * 		s1 = middleTerm, middle, middle, middle
	 * 		s2 = s1
	 * 		s3 = middleTerm, middle, middle, middle
	 * 		s4 = lesserTerm, middle, middle, middle
	 * 		s5 = greaterTerm, middle, middle, middle
	 * 		s6 = middleTerm, less, middle, middle
	 * 		s7 = middleTerm, more, middle, middle
	 *  	s8 = middleTerm, middle, less, middle
	 *  	s9 = middleTerm, middle, more, middle
	 *  	s10 = middleTerm, middle, middle, less
	 *  	s11 = middleTerm, middle, middle, more
	 */
	private Section s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11;
	
	/**
	 * Prepare the test instances and necessary stubs for use in the tests
	 *
	 */
	@BeforeClass
	public void setup() {
		s1 = new SimpleSection(middleTerm, middle, middle, middle);
		s2 = s1;
		s3 = new SimpleSection(middleTerm, middle, middle, middle);
		s4 = new SimpleSection(lesserTerm, middle, middle, middle);
		s5 = new SimpleSection(greaterTerm, middle, middle, middle);
		s6 = new SimpleSection(middleTerm, less, middle, middle);
		s7 = new SimpleSection(middleTerm, more, middle, middle);
		s8 = new SimpleSection(middleTerm, more, less, middle);
		s9 = new SimpleSection(middleTerm, more, more, middle);
		s10 = new SimpleSection(middleTerm, more, middle, less);
		s11 = new SimpleSection(middleTerm, more, middle, more);
	}

	/**
	 * Confirm the semantics of reference and object equality.
	 * 
	 * Note:
	 * 	"Sameness" is via instance equality (via ==)
	 * 	"Equality" is object semantic equality (via equals)
	 */
	@Test
	public void confirmEquals() {
		SoftAssert eq = new SoftAssert();
		
		eq.assertSame(s1, s2, "References to same instance should be same");
		eq.assertEquals(s1, s2, "References to same instance should be equal");
		eq.assertEquals(s1, s3, "Instances with same uniqueness fields should be equal");

		eq.assertNotEquals(s1, null, "Non-null instance should not be equal to null");		
		eq.assertNotEquals(s1, s4, "Instances with varying term should not be equal");
		eq.assertNotEquals(s1, s6, "Instances with varying section identifier should not be equal");
		
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
	 */
	@Test
	public void confirmHashCode() {
		SoftAssert hc = new SoftAssert();
		
		hc.assertEquals(s1, s2, "References to same instance should have same hashcode");
		hc.assertEquals(s1, s3, "Equal instances should have same hashcode");
		
		//ensure that our sample dataset, which has variety in its field content,
		//has some variation in its hashcode. No variation in hash would be bad
		//this is not sufficient on its own, a good hash should have a uniform, 
		//non-clustering distribution
		boolean variety = 
				s1.hashCode() == s4.hashCode() &&
				s1.hashCode() == s5.hashCode() &&
				s1.hashCode() == s6.hashCode() &&
				s1.hashCode() == s7.hashCode()
		;
		hc.assertFalse(variety, "Hashcode should return a variety of values for instances with varying uniqueness fields");
				
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
				(Random r) -> {return SectionUnitTest.generateSection(r);}
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
				s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11
		));
	}
	
	/**
	 * Generate a Term based on the current state of a Random
	 *
	 * @param generator a Random for use in building the Term
	 * @return the next Term
	 */
	public static Section generateSection(Random generator){
		return new SimpleSection(
				TermUnitTest.generateTerm(generator),
				Long.toHexString(generator.nextLong()),
				Long.toHexString(generator.nextLong()), 
				Long.toHexString(generator.nextLong())
		);
	}

	/**
	 * Confirm the semantics of compareTo is consistent with equals() 
	 * and has appropriate object semantics.
	 * 
	 * Note:
	 *  Order for Section implies the following precedence:
	 *  	Term
	 *  	CourseId
	 *  	SectionId
	 */
	@Test
	public void confirmCompareTo() {
		SoftAssert ct = new SoftAssert();
		
		ct.assertEquals(s1.compareTo(s2), 0, "References to same instance should be equal");
		ct.assertEquals(s2.compareTo(s1), 0, "References to same instance should be equal, regardless of comparison direction");
		ct.assertEquals(s1.compareTo(s3), 0, "Instances with same fields should be equal");
		ct.assertEquals(s2.compareTo(s1), 0, "Instances with same fields should be equal, regardless of comparison direction");
		
		//check term ordering and reversability
		ct.assertEquals(Math.signum(s1.compareTo(s4)), 1.0f, "Positive result expected for instance with lesser term");
		ct.assertEquals(Math.signum(s4.compareTo(s1)), -1.0f, "Negative result expected for instance with greater term");
		ct.assertEquals(Math.signum(s1.compareTo(s5)), -1.0f, "Negative result expected for instance with greater term");
		ct.assertEquals(Math.signum(s5.compareTo(s1)), 1.0f, "Positive result expected for instance with lesser term");
		
		//check course id ordering and reversability 
		ct.assertEquals(Math.signum(s1.compareTo(s8)), 1.0f, "Positive result expected for instance with lesser course id");
		ct.assertEquals(Math.signum(s8.compareTo(s1)), -1.0f, "Negative result expected for instance with greater course id");
		ct.assertEquals(Math.signum(s1.compareTo(s9)), -1.0f, "Negative result expected for instance with greater course id");
		ct.assertEquals(Math.signum(s9.compareTo(s1)), 1.0f, "Positive result expected for instance with lesser course id");
		
		//check section id ordering and reversability 
		ct.assertEquals(Math.signum(s1.compareTo(s10)), 1.0f, "Positive result expected for instance with lesser section id");
		ct.assertEquals(Math.signum(s10.compareTo(s1)), -1.0f, "Negative result expected for instance with greater section id");
		ct.assertEquals(Math.signum(s1.compareTo(s11)), -1.0f, "Negative result expected for instance with greater section id");
		ct.assertEquals(Math.signum(s11.compareTo(s1)), 1.0f, "Positive result expected for instance with lesser section id");
		
		//check transitivity on term and id
		ct.assertEquals(Math.signum(s4.compareTo(s1)), Math.signum(s1.compareTo(s5)), "Transitivity expected for instances varying on term");
		ct.assertEquals(Math.signum(s8.compareTo(s1)), Math.signum(s1.compareTo(s9)), "Transitivity expected for instances varying on course id");
		ct.assertEquals(Math.signum(s10.compareTo(s1)), Math.signum(s1.compareTo(s11)), "Transitivity expected for instances varying on section id");
		
		ct.assertAll();
	}
}
