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
public class InstructorRatingRecord extends org.jooq.impl.UpdatableRecordImpl<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord> implements org.jooq.Record4<java.lang.Integer, java.lang.Integer, java.lang.Double, java.lang.String> {

	private static final long serialVersionUID = 1942188216;

	/**
	 * Setter for <code>APP.INSTRUCTOR_RATING.ID</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>APP.INSTRUCTOR_RATING.ID</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>APP.INSTRUCTOR_RATING.INSTRUCTOR_ID</code>.
	 */
	public void setInstructorId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>APP.INSTRUCTOR_RATING.INSTRUCTOR_ID</code>.
	 */
	public java.lang.Integer getInstructorId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>APP.INSTRUCTOR_RATING.RATING</code>.
	 */
	public void setRating(java.lang.Double value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>APP.INSTRUCTOR_RATING.RATING</code>.
	 */
	public java.lang.Double getRating() {
		return (java.lang.Double) getValue(2);
	}

	/**
	 * Setter for <code>APP.INSTRUCTOR_RATING.DESCRIPTION</code>.
	 */
	public void setDescription(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>APP.INSTRUCTOR_RATING.DESCRIPTION</code>.
	 */
	public java.lang.String getDescription() {
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
	public org.jooq.Row4<java.lang.Integer, java.lang.Integer, java.lang.Double, java.lang.String> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.Integer, java.lang.Double, java.lang.String> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return io.devyse.scheduler.model.jooq.tables.InstructorRating.INSTRUCTOR_RATING.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return io.devyse.scheduler.model.jooq.tables.InstructorRating.INSTRUCTOR_RATING.INSTRUCTOR_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Double> field3() {
		return io.devyse.scheduler.model.jooq.tables.InstructorRating.INSTRUCTOR_RATING.RATING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return io.devyse.scheduler.model.jooq.tables.InstructorRating.INSTRUCTOR_RATING.DESCRIPTION;
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
		return getInstructorId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Double value3() {
		return getRating();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getDescription();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InstructorRatingRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InstructorRatingRecord value2(java.lang.Integer value) {
		setInstructorId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InstructorRatingRecord value3(java.lang.Double value) {
		setRating(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InstructorRatingRecord value4(java.lang.String value) {
		setDescription(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InstructorRatingRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Double value3, java.lang.String value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached InstructorRatingRecord
	 */
	public InstructorRatingRecord() {
		super(io.devyse.scheduler.model.jooq.tables.InstructorRating.INSTRUCTOR_RATING);
	}

	/**
	 * Create a detached, initialised InstructorRatingRecord
	 */
	public InstructorRatingRecord(java.lang.Integer id, java.lang.Integer instructorId, java.lang.Double rating, java.lang.String description) {
		super(io.devyse.scheduler.model.jooq.tables.InstructorRating.INSTRUCTOR_RATING);

		setValue(0, id);
		setValue(1, instructorId);
		setValue(2, rating);
		setValue(3, description);
	}
}