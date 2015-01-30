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
public class MeetingInstructor extends org.jooq.impl.TableImpl<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord> {

	private static final long serialVersionUID = 1422818690;

	/**
	 * The reference instance of <code>APP.MEETING_INSTRUCTOR</code>
	 */
	public static final io.devyse.scheduler.model.jooq.tables.MeetingInstructor MEETING_INSTRUCTOR = new io.devyse.scheduler.model.jooq.tables.MeetingInstructor();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord> getRecordType() {
		return io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord.class;
	}

	/**
	 * The column <code>APP.MEETING_INSTRUCTOR.ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>APP.MEETING_INSTRUCTOR.MEETING_ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord, java.lang.Integer> MEETING_ID = createField("MEETING_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>APP.MEETING_INSTRUCTOR.INSTRUCTOR_ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord, java.lang.Integer> INSTRUCTOR_ID = createField("INSTRUCTOR_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>APP.MEETING_INSTRUCTOR</code> table reference
	 */
	public MeetingInstructor() {
		this("MEETING_INSTRUCTOR", null);
	}

	/**
	 * Create an aliased <code>APP.MEETING_INSTRUCTOR</code> table reference
	 */
	public MeetingInstructor(java.lang.String alias) {
		this(alias, io.devyse.scheduler.model.jooq.tables.MeetingInstructor.MEETING_INSTRUCTOR);
	}

	private MeetingInstructor(java.lang.String alias, org.jooq.Table<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord> aliased) {
		this(alias, aliased, null);
	}

	private MeetingInstructor(java.lang.String alias, org.jooq.Table<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, io.devyse.scheduler.model.jooq.App.APP, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord, java.lang.Integer> getIdentity() {
		return io.devyse.scheduler.model.jooq.Keys.IDENTITY_MEETING_INSTRUCTOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord> getPrimaryKey() {
		return io.devyse.scheduler.model.jooq.Keys.MEETING_INSTRUCTOR_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord>>asList(io.devyse.scheduler.model.jooq.Keys.MEETING_INSTRUCTOR_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<io.devyse.scheduler.model.jooq.tables.records.MeetingInstructorRecord, ?>>asList(io.devyse.scheduler.model.jooq.Keys.MEETING_INSTRUCTOR_FKEY_MEETING_ID, io.devyse.scheduler.model.jooq.Keys.MEETING_INSTRUCTOR_FKEY_INSTRUCTOR_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public io.devyse.scheduler.model.jooq.tables.MeetingInstructor as(java.lang.String alias) {
		return new io.devyse.scheduler.model.jooq.tables.MeetingInstructor(alias, this);
	}

	/**
	 * Rename this table
	 */
	public io.devyse.scheduler.model.jooq.tables.MeetingInstructor rename(java.lang.String name) {
		return new io.devyse.scheduler.model.jooq.tables.MeetingInstructor(name, null);
	}
}