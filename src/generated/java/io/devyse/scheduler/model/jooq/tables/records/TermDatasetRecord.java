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
public class TermDatasetRecord extends org.jooq.impl.UpdatableRecordImpl<io.devyse.scheduler.model.jooq.tables.records.TermDatasetRecord> implements org.jooq.Record3<java.lang.Integer, java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = -1867740932;

	/**
	 * Setter for <code>APP.TERM_DATASET.ID</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>APP.TERM_DATASET.ID</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>APP.TERM_DATASET.TERM_ID</code>.
	 */
	public void setTermId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>APP.TERM_DATASET.TERM_ID</code>.
	 */
	public java.lang.Integer getTermId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>APP.TERM_DATASET.VERSION_ID</code>.
	 */
	public void setVersionId(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>APP.TERM_DATASET.VERSION_ID</code>.
	 */
	public java.lang.Integer getVersionId() {
		return (java.lang.Integer) getValue(2);
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
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.Integer, java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.Integer, java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return io.devyse.scheduler.model.jooq.tables.TermDataset.TERM_DATASET.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return io.devyse.scheduler.model.jooq.tables.TermDataset.TERM_DATASET.TERM_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return io.devyse.scheduler.model.jooq.tables.TermDataset.TERM_DATASET.VERSION_ID;
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
		return getTermId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getVersionId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermDatasetRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermDatasetRecord value2(java.lang.Integer value) {
		setTermId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermDatasetRecord value3(java.lang.Integer value) {
		setVersionId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermDatasetRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Integer value3) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TermDatasetRecord
	 */
	public TermDatasetRecord() {
		super(io.devyse.scheduler.model.jooq.tables.TermDataset.TERM_DATASET);
	}

	/**
	 * Create a detached, initialised TermDatasetRecord
	 */
	public TermDatasetRecord(java.lang.Integer id, java.lang.Integer termId, java.lang.Integer versionId) {
		super(io.devyse.scheduler.model.jooq.tables.TermDataset.TERM_DATASET);

		setValue(0, id);
		setValue(1, termId);
		setValue(2, versionId);
	}
}
