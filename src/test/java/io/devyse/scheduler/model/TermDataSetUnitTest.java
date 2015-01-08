/**
 * @(#) TermDataSetUnitTest.java
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

import io.devyse.scheduler.model.simple.SimpleTermDataSet;
import io.devyse.scheduler.model.stub.StubTerm;
import io.devyse.scheduler.model.stub.StubVersion;

import java.util.Random;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Unit tests for the AbstractTermDataSet base class to confirm the default
 * functionality has appropriate semantics.
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
@Test(	groups = {"unit","interface","TermDataSet.basic"})
public class TermDataSetUnitTest {

	/**
	 * Term instances for use in the unit tests
	 * 
	 * t0 < t1 < t2
	 */
	private Term t0, t1, t2;
	
	/**
	 * Version instance for use in the unit tests
	 * 
	 * v0 < v1 < v2
	 */
	private Version v0, v1, v2;
	
	/**
	 * Version instances for use in the unit tests
	 * 
	 * d0 == d0b -> (t0)(v0)
	 * d0 equals d0c 
	 * d0 < d1 < d2 by term -> (v0)(t0, t1, t2)
	 * d0 < d3 < d4 by version (t0)(v0, v1, v2)
	 */
	private TermDataSet d0, d0b, d0c, d1, d2, d3, d4;
	
	/**
	 * Generate a random TermDataSet using the specified random generator
	 * 
	 * @param generator the random generator to build the TermDataSet
	 * 
	 * @return a random TermDataSet object
	 */
	public static TermDataSet generateTermDataSet(Random generator){
		return SimpleTermDataSet.newTermDataSet(
			StubTerm.newRandomTerm(generator),
			StubVersion.newRandomVersion(generator)
		);
	}

	/**
	 * Prepare the class for unit testing
	 */
	@BeforeClass
	public void setup(){
		setupTerms();
		setupVersions();
		setupTermDataSets();
	}
	
	/**
	 * Create stub Term objects for use in constructing TermDataSets for testing
	 */
	private void setupTerms(){
		t0 = StubTerm.newTerm(1);
		t1 = StubTerm.newTerm(5);
		t2 = StubTerm.newTerm(9);
	}
	
	/**
	 * Create stub Version objects for use in constructing TermDataSets for testing
	 */
	private void setupVersions(){
		v0 = StubVersion.newVersion(1);
		v1 = StubVersion.newVersion(5);
		v2 = StubVersion.newVersion(9);
	}
	
	/**
	 * Construct the unit under test instances using the Simple TermDataSet
	 * 
	 * d0 == d0b -> (t0)(v0)
	 * d0 equals d0c 
	 * d0 < d1 < d2 by term -> (v0)(t0, t1, t2)
	 * d0 < d3 < d4 by version (t0)(v0, v1, v2)
	 */
	private void setupTermDataSets(){
		//all equivalent uniqueness fields		 
		d0 = SimpleTermDataSet.newTermDataSet(t0, v0);
		d0b = d0;
		d0c = SimpleTermDataSet.newTermDataSet(t0, v0);
		
		//varying on Term
		d1 = SimpleTermDataSet.newTermDataSet(t1, v0);
		d2 = SimpleTermDataSet.newTermDataSet(t2, v0);
		
		//varying on Version
		d3 = SimpleTermDataSet.newTermDataSet(t0, v1);
		d4 = SimpleTermDataSet.newTermDataSet(t0, v2);
	}
	
	/**
	 * Confirm the semantics of reference and object equality.
	 * 
	 * Note:
	 * 	"Sameness" is via instance equality (via ==)
	 * 	"Equality" is object semantic equality (via equals)
	 */
	@Test
	public void confirmEquals(){
		SoftAssert eq = new SoftAssert();

		//TermDataSet is equal to another if the Terms are equal and the Versions are equal
		eq.assertSame(d0, d0b, "References to the same instance should be the same");
		eq.assertEquals(d0, d0b, "References to the same instance should be equal");
		eq.assertEquals(d0, d0c, "Instances with same uniqueness fields should be equal");
		
		//TermDataSet is not equal under any other condition
		eq.assertNotEquals(d0, null, "Non-null instance should not be equal to null");		
		eq.assertNotEquals(d0, d1, "Instances with varying date time blocks should not be equal");
		eq.assertNotEquals(d0, d3, "Instances with varying sections should not be equal");
		
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
	public void confirmHashCode(){
		SoftAssert hc = new SoftAssert();

		//hashcode semantics should be consistent with equals()
		hc.assertEquals(d0.hashCode(), d0b.hashCode(), "References to same instance should have same hashcode");
		hc.assertEquals(d0.hashCode(), d0c.hashCode(), "Equal instances should have same hashcode");
		
		//ensure that our sample dataset, which has variety in its field content,
		//has some variation in its hashcode. No variation in hash would be bad
		//this is not sufficient on its own, a good hash should have a uniform, 
		//non-clustering distribution
		boolean variety = 
				d0.hashCode() == d1.hashCode() &&
				d0.hashCode() == d2.hashCode() &&
				d0.hashCode() == d3.hashCode() &&
				d0.hashCode() == d4.hashCode()
		;
		hc.assertFalse(variety, "Hashcode should return a variety of values for instance with varying uniqueness fields");
		
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
				(Random r) -> {return SimpleTermDataSet.newRandomTermDataSet(r);}
		);
	}

	/**
	 * Confirm the semantics of compareTo is consistent with equals() 
	 * and has appropriate object semantics. 
	 */
	@Test
	public void confirmCompareTo() {
		SoftAssert ct = new SoftAssert();

		//compareTo should be consistent with equals
		ct.assertEquals(d0.compareTo(d0b), 0, "References to same instance should be equal");
		ct.assertEquals(d0b.compareTo(d0), 0, "References to same instance should be equal, regardless of comparison direction");
		ct.assertEquals(d0.compareTo(d0c), 0, "Instances with same fields should be equal");
		ct.assertEquals(d0c.compareTo(d0), 0, "Instances with same fields should be equal, regardless of comparison direction");
		
		//compareTo by Term first and Version second
		ct.assertEquals(Math.signum(d0.compareTo(d1)), -1.0f, "Negative result expected for instance with greater term");
		ct.assertEquals(Math.signum(d1.compareTo(d0)), 1.0f, "Positive result expected for instance with lesser term");
		ct.assertEquals(Math.signum(d0.compareTo(d2)), -1.0f, "Negative result expected for instance with greater term");
		ct.assertEquals(Math.signum(d2.compareTo(d0)), 1.0f, "Positive result expected for instance with lesser term");
		
		ct.assertEquals(Math.signum(d0.compareTo(d3)), -1.0f, "Negative result expected for instance with greater version");
		ct.assertEquals(Math.signum(d3.compareTo(d0)), 1.0f, "Positive result expected for instance with lesser version");
		ct.assertEquals(Math.signum(d3.compareTo(d4)), -1.0f, "Negative result expected for instance with greater version");
		ct.assertEquals(Math.signum(d4.compareTo(d3)), 1.0f, "Positive result expected for instance with lesser version");

		ct.assertEquals(Math.signum(d2.compareTo(d1)), Math.signum(-d1.compareTo(d2)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (lesser term)");
		ct.assertEquals(Math.signum(d0.compareTo(d1)), Math.signum(-d1.compareTo(d0)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (greater term)");
		ct.assertEquals(Math.signum(d4.compareTo(d3)), Math.signum(-d3.compareTo(d4)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (lesser version)");
		ct.assertEquals(Math.signum(d0.compareTo(d3)), Math.signum(-d3.compareTo(d0)), "CompareTo should reverse sign for reversed direction of comparison of non-equal instances (greater version)");
		
		ct.assertEquals(Math.signum(d0.compareTo(d1)), Math.signum(d1.compareTo(d2)), "Transitivity expected for instances varying on term");
		ct.assertEquals(Math.signum(d0.compareTo(d3)), Math.signum(d3.compareTo(d4)), "Transitivity expected for instances varying on version");
		
		ct.assertAll();
	}
}
