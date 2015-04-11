/**
 * This class is generated by jOOQ
 */
package io.devyse.scheduler.model.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.1"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TermRecord extends org.jooq.impl.UpdatableRecordImpl<io.devyse.scheduler.model.jooq.tables.records.TermRecord> implements org.jooq.Record4<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> {

	private static final long serialVersionUID = -1444068190;

	/**
	 * Setter for <code>APP.TERM.ID</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>APP.TERM.ID</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>APP.TERM.UNIVERSITY_ID</code>.
	 */
	public void setUniversityId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>APP.TERM.UNIVERSITY_ID</code>.
	 */
	public java.lang.Integer getUniversityId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>APP.TERM.NAME</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>APP.TERM.NAME</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>APP.TERM.INTERNAL_IDENTIFIER</code>.
	 */
	public void setInternalIdentifier(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>APP.TERM.INTERNAL_IDENTIFIER</code>.
	 */
	public java.lang.String getInternalIdentifier() {
		return (java.lang.String) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return io.devyse.scheduler.model.jooq.tables.Term.TERM.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return io.devyse.scheduler.model.jooq.tables.Term.TERM.UNIVERSITY_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return io.devyse.scheduler.model.jooq.tables.Term.TERM.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return io.devyse.scheduler.model.jooq.tables.Term.TERM.INTERNAL_IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getUniversityId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getInternalIdentifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermRecord value2(java.lang.Integer value) {
		setUniversityId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermRecord value3(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermRecord value4(java.lang.String value) {
		setInternalIdentifier(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.String value3, java.lang.String value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TermRecord
	 */
	public TermRecord() {
		super(io.devyse.scheduler.model.jooq.tables.Term.TERM);
	}

	/**
	 * Create a detached, initialised TermRecord
	 */
	public TermRecord(java.lang.Integer id, java.lang.Integer universityId, java.lang.String name, java.lang.String internalIdentifier) {
		super(io.devyse.scheduler.model.jooq.tables.Term.TERM);

		setValue(0, id);
		setValue(1, universityId);
		setValue(2, name);
		setValue(3, internalIdentifier);
	}
}
