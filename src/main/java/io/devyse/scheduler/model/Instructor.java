/**
 * 
 */
package io.devyse.scheduler.model;

import java.util.Objects;

/**
 * Represent an instructor
 * 
 * @author Mike Reinhold
 * @since 4.12.8
 *
 */
public interface Instructor extends Comparable<Instructor> {
	
	/**
	 * Retrieve the instructor name
	 * 
	 * @return the instructor name
	 */
	public String getName();
	
	/**
	 * The Instructor is tied to the Term dataset which contains the course data which references
	 * this instructor.
	 * 
	 * @return the dataset of the term which contains the Instructor
	 */
	public TermDataSet getTermDataSet();
	
	//TODO other methods?

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public default boolean equals(Term other) {
		return	false//TODO equals stub
		;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public default int getHashCode() {
		return Objects.hash(
				//TODO hash stub
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public default int compareTo(Instructor other) {
		//TODO method stub
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public static String toString(Term term){
		//TODO method stub
		return null;
	}
}
