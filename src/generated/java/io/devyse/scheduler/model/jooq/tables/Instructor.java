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
public class Instructor extends org.jooq.impl.TableImpl<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord> {

	private static final long serialVersionUID = -1978688146;

	/**
	 * The reference instance of <code>APP.INSTRUCTOR</code>
	 */
	public static final io.devyse.scheduler.model.jooq.tables.Instructor INSTRUCTOR = new io.devyse.scheduler.model.jooq.tables.Instructor();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord> getRecordType() {
		return io.devyse.scheduler.model.jooq.tables.records.InstructorRecord.class;
	}

	/**
	 * The column <code>APP.INSTRUCTOR.ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>APP.INSTRUCTOR.NAME</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord, java.lang.String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(255).defaulted(true), this, "");

	/**
	 * The column <code>APP.INSTRUCTOR.TERM_DATASET_ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord, java.lang.Integer> TERM_DATASET_ID = createField("TERM_DATASET_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>APP.INSTRUCTOR</code> table reference
	 */
	public Instructor() {
		this("INSTRUCTOR", null);
	}

	/**
	 * Create an aliased <code>APP.INSTRUCTOR</code> table reference
	 */
	public Instructor(java.lang.String alias) {
		this(alias, io.devyse.scheduler.model.jooq.tables.Instructor.INSTRUCTOR);
	}

	private Instructor(java.lang.String alias, org.jooq.Table<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord> aliased) {
		this(alias, aliased, null);
	}

	private Instructor(java.lang.String alias, org.jooq.Table<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, io.devyse.scheduler.model.jooq.App.APP, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord, java.lang.Integer> getIdentity() {
		return io.devyse.scheduler.model.jooq.Keys.IDENTITY_INSTRUCTOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord> getPrimaryKey() {
		return io.devyse.scheduler.model.jooq.Keys.INSTRUCTOR_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord>>asList(io.devyse.scheduler.model.jooq.Keys.INSTRUCTOR_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<io.devyse.scheduler.model.jooq.tables.records.InstructorRecord, ?>>asList(io.devyse.scheduler.model.jooq.Keys.INSTRUCTOR_FKEY_TERM_DATASET_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public io.devyse.scheduler.model.jooq.tables.Instructor as(java.lang.String alias) {
		return new io.devyse.scheduler.model.jooq.tables.Instructor(alias, this);
	}

	/**
	 * Rename this table
	 */
	public io.devyse.scheduler.model.jooq.tables.Instructor rename(java.lang.String name) {
		return new io.devyse.scheduler.model.jooq.tables.Instructor(name, null);
	}
}
