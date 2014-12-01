/**
 * 
 */
package io.devyse.scheduler.retrieval;

import io.devyse.scheduler.model.Term;

import java.util.Collection;

/**
 * @author mreinhold
 *
 */
public abstract class AbstractTermSelector implements TermSelector {

	/**
	 * The term that has been previously selected, or null if {@link #selectTerm(Collection)} 
	 * has not been called
	 */
	private Term term;
	
	/**
	 * 
	 */
	public AbstractTermSelector() {
		super();
		
		term = null; 			//term must be null initially
	}

	/* (non-Javadoc)
	 * @see io.devyse.scheduler.retrieval.TermSelector#getTerm()
	 */
	@Override
	public Term getTerm() {
		return term;
	}

	/**
	 * Store the term for future recall via {@link #getTerm()}
	 * 
	 * @param term the term previously selected in {@link #selectTerm(Collection)}
	 */
	protected void setTerm(Term term){
		this.term = term;
	}
}
