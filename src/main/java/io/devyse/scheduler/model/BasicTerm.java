/**
 * 
 */
package io.devyse.scheduler.model;

import java.util.Objects;

/**
 * Basic term implementation
 * 
 * @author Mike Reinhold
 *
 */
public class BasicTerm implements Term {

	/**
	 * Term identifier, usually a code defining the year and semester
	 */
	private String id;

	/**
	 * Term name, usually text declaring the year and semester
	 */
	private String name;
	
	/**
	 * Build an term based on the id and name
	 */
	public BasicTerm(String id, String name) {
		super();
		this.setId(id);
		this.setName(name);
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.model.Term#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param id the id to set
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Term other) {
		return this.getId().equals(other.getId())
		;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int getHashCode() {
		return Objects.hash(
				this.getId()
		);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Term other) {
		int result = this.getId().compareTo(other.getId());
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.getName());
		
		return sb.toString();
	}
}
