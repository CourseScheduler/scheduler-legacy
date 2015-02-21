/**
 * @(#) HashCodeQualityHelper.java
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

import java.util.Collection;
import java.util.Iterator;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.testng.asserts.SoftAssert;

/**
 * Helper class for confirming if the hashcode calculation for a given
 * Object stands up to basic collision tests and requirements. These
 * requirements are simply:
 * 		a maximum number of unique source object can generate a single hashcode
 * 		the average number of collisions per unique hashcode
 *
 * @author Mike Reinhold
 * @since 4.13.0
 * 
 */
public class HashCodeQualityHelper {

	/**
	 * Funtional interface defining an object generator based on a Random
	 * source for initializing fields. This will yield varietous, if not
	 * realistic, object data for testing hashcode implementations
	 *
	 * @author Mike Reinhold
	 */
	public interface RandomGenerator{
		public Object generate(Random source);
	}

	/**
	 * PRNG seed for the hash code generator used in the hashcode quality test
	 */
	public static final long RANDOM_GENERATOR_SEED = 1024L;

	/**
	 * Number of DateTimeBlock objects to generate in testing the hashcode quality
	 */
	public static final int SAMPLE_SIZE = 1000000;

	/**
	 * Maximum number of per hashcode collisions. If any hashcode occurs more than
	 * the number specified here, the test will fail
	 */
	public static final int MAX_COLLISIONS_PER_HASH = 3;

	/**
	 * Maximum number of average hashcode collisions. If the average number of collisions
	 * is greater than the number specified here, the test will fail
	 */
	public static final int AVG_COLLISIONS_PER_SET = 2;
	
	/**
	 * Verify that the hashCode method of the objects under test exhibit some level
	 * of desired distribution and collision resistance. 
	 *
	 * @param instanceSupplier the RandomGenerator method to create instances
	 * @param sampleSize the number of objects to hash
	 * @param maxHashCollisionLimit maximum number of per hash collisions 
	 * @param avgHashCollisionLimit average number of per set collisions
	 */
	public static void confirmHashCodeQuality(Iterator<Object> iterator, int sampleSize, int maxHashCollisionLimit, int avgHashCollisionLimit){
		Stream<Object> stream = StreamSupport.stream(
				Spliterators.spliterator(iterator, sampleSize, Spliterator.SIZED),
				true
		);
		
		confirmHashCodeQuality(stream, maxHashCollisionLimit, avgHashCollisionLimit);
	}
		
	/**
	 * Verify that the hashCode method of the objects under test exhibit some level
	 * of desired distribution and collision resistance.
	 *
	 * @param stream the stream of objects to test for hash quality
	 * @param maxHashCollisionLimit maximum number of per hash collisions 
	 * @param avgHashCollisionLimit average number of per set collisions
	 */
	public static void confirmHashCodeQuality(Stream<Object> stream, int maxHashCollisionLimit, int avgHashCollisionLimit){
		Map<Integer, Long> hashCodes = stream.parallel()
				.distinct()
				.collect(Collectors.groupingByConcurrent(Object::hashCode,Collectors.counting()));
		
		LongSummaryStatistics stats = hashCodes.values().parallelStream()
				.mapToLong(Long::longValue)
				.summaryStatistics();
		
		long maxCollisions = stats.getMax();
		double averageCollisions = stats.getAverage();
		
		SoftAssert hcq = new SoftAssert();
		
		hcq.assertTrue(maxCollisions < maxHashCollisionLimit, "Maximum number of collisions for individual hashcode exceeded allowed value");
		hcq.assertTrue(averageCollisions < avgHashCollisionLimit, "Average number of collisions per hashcode exceeded allowed value");
		
		hcq.assertAll();
	}
	
	/**
	 * Verify that the hashCode method of the objects under test exhibit some level
	 * of desired distribution and collision resistance. 
	 *
	 * @param collection an iterator of the objects to test
	 * @param seed the PRNG seed
	 * @param maxHashCollisionLimit maximum number of per hash collisions 
	 * @param avgHashCollisionLimit average number of per set collisions
	 */
	public static void confirmHashCodeQuality(Collection<Object> collection, int maxHashCollisionLimit, int avgHashCollisionLimit){
		confirmHashCodeQuality(collection.iterator(), collection.size(), maxHashCollisionLimit, avgHashCollisionLimit);
	}
	
	/**
	 * Verify that the hashCode method of the objects under test exhibit some level
	 * of desired distribution and collision resistance. 
	 *
	 * @param instanceSupplier the RandomGenerator method
	 * @param sampleSize the number of samples to generate
	 */
	public static void confirmHashCodeQuality(Iterator<Object> iterator, int sampleSize){
		confirmHashCodeQuality(iterator, sampleSize, MAX_COLLISIONS_PER_HASH, AVG_COLLISIONS_PER_SET);
	}
	
	/**
	 * Verify that the hashCode method of the objects under test exhibit some level
	 * of desired distribution and collision resistance. 
	 *
	 * @param instanceSupplier the RandomGenerator method
	 */
	public static void confirmHashCodeQuality(Iterator<Object> iterator){
		confirmHashCodeQuality(iterator, SAMPLE_SIZE, MAX_COLLISIONS_PER_HASH, AVG_COLLISIONS_PER_SET);
	}
	
	/**
	 * Verify that the hashCode method of the objects under test exhibit some level
	 * of desired distribution and collision resistance. 
	 *
	 * @param iterator an iterator of the objects to test
	 */
	public static void confirmHashCodeQuality(Collection<Object> collection){
		confirmHashCodeQuality(collection, MAX_COLLISIONS_PER_HASH, AVG_COLLISIONS_PER_SET);
	}
	
	/**
	 * Confirm the hashCode quality for instances built using the specified generator. 
	 * Generates the specified sample size
	 *
	 * @param generator the object generator for building instances
	 * @param sampleSize the number of objects to instantiate for testing hashes
	 */
	public static void confirmHashCodeQuality(RandomGenerator generator, int sampleSize){
		confirmHashCodeQuality(new Iterator<Object>(){
			
			private int count = 0;
			private Random random = new Random(RANDOM_GENERATOR_SEED);
			
			@Override
			public boolean hasNext() {
				return count < sampleSize;
			}

			@Override
			public Object next() {
				if(count < sampleSize) { count++; return generator.generate(random); }
				throw new NoSuchElementException("Sample Size exceeded");
			}
			
		});
	}
	
	/**
	 * Confirm the hashCode quality for instances built using the specified generator. 
	 * Uses the default sample size..
	 *
	 * @param generator the object generator for building instances
	 */
	public static void confirmHashCodeQuality(RandomGenerator generator){
		confirmHashCodeQuality(generator, SAMPLE_SIZE);
	}
	
}
