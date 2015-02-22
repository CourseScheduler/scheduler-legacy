/**
 * This class is generated by jOOQ
 */
package io.devyse.scheduler.model.jooq.tables;

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
public class InstructorRating extends org.jooq.impl.TableImpl<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord> {

	private static final long serialVersionUID = -493708271;

	/**
	 * The reference instance of <code>APP.INSTRUCTOR_RATING</code>
	 */
	public static final io.devyse.scheduler.model.jooq.tables.InstructorRating INSTRUCTOR_RATING = new io.devyse.scheduler.model.jooq.tables.InstructorRating();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord> getRecordType() {
		return io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord.class;
	}

	/**
	 * The column <code>APP.INSTRUCTOR_RATING.ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>APP.INSTRUCTOR_RATING.INSTRUCTOR_ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord, java.lang.Integer> INSTRUCTOR_ID = createField("INSTRUCTOR_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>APP.INSTRUCTOR_RATING.RATING</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord, java.lang.Double> RATING = createField("RATING", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>APP.INSTRUCTOR_RATING.DESCRIPTION</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord, java.lang.String> DESCRIPTION = createField("DESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR.length(255).defaulted(true), this, "");

	/**
	 * Create a <code>APP.INSTRUCTOR_RATING</code> table reference
	 */
	public InstructorRating() {
		this("INSTRUCTOR_RATING", null);
	}

	/**
	 * Create an aliased <code>APP.INSTRUCTOR_RATING</code> table reference
	 */
	public InstructorRating(java.lang.String alias) {
		this(alias, io.devyse.scheduler.model.jooq.tables.InstructorRating.INSTRUCTOR_RATING);
	}

	private InstructorRating(java.lang.String alias, org.jooq.Table<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord> aliased) {
		this(alias, aliased, null);
	}

	private InstructorRating(java.lang.String alias, org.jooq.Table<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, io.devyse.scheduler.model.jooq.App.APP, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord, java.lang.Integer> getIdentity() {
		return io.devyse.scheduler.model.jooq.Keys.IDENTITY_INSTRUCTOR_RATING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord> getPrimaryKey() {
		return io.devyse.scheduler.model.jooq.Keys.INSTRUCTOR_RATING_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord>>asList(io.devyse.scheduler.model.jooq.Keys.INSTRUCTOR_RATING_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRatingRecord, ?>>asList(io.devyse.scheduler.model.jooq.Keys.INSTRUCTOR_RATING_FKEY_INSTRUCTOR_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public io.devyse.scheduler.model.jooq.tables.InstructorRating as(java.lang.String alias) {
		return new io.devyse.scheduler.model.jooq.tables.InstructorRating(alias, this);
	}

	/**
	 * Rename this table
	 */
	public io.devyse.scheduler.model.jooq.tables.InstructorRating rename(java.lang.String name) {
		return new io.devyse.scheduler.model.jooq.tables.InstructorRating(name, null);
	}
}