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
public class Section extends org.jooq.impl.TableImpl<io.devyse.scheduler.model.jooq.tables.records.SectionRecord> {

	private static final long serialVersionUID = -1386500087;

	/**
	 * The reference instance of <code>APP.SECTION</code>
	 */
	public static final io.devyse.scheduler.model.jooq.tables.Section SECTION = new io.devyse.scheduler.model.jooq.tables.Section();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<io.devyse.scheduler.model.jooq.tables.records.SectionRecord> getRecordType() {
		return io.devyse.scheduler.model.jooq.tables.records.SectionRecord.class;
	}

	/**
	 * The column <code>APP.SECTION.ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>APP.SECTION.CRN</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, java.lang.String> CRN = createField("CRN", org.jooq.impl.SQLDataType.VARCHAR.length(255).defaulted(true), this, "");

	/**
	 * The column <code>APP.SECTION.COURSE_NUMBER</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, java.lang.String> COURSE_NUMBER = createField("COURSE_NUMBER", org.jooq.impl.SQLDataType.VARCHAR.length(255).defaulted(true), this, "");

	/**
	 * The column <code>APP.SECTION.DESCRIPTION</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, java.lang.String> DESCRIPTION = createField("DESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR.length(255).defaulted(true), this, "");

	/**
	 * The column <code>APP.SECTION.NAME</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, java.lang.String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(255).defaulted(true), this, "");

	/**
	 * The column <code>APP.SECTION.SECTION_NUMBER</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, java.lang.String> SECTION_NUMBER = createField("SECTION_NUMBER", org.jooq.impl.SQLDataType.VARCHAR.length(255).defaulted(true), this, "");

	/**
	 * The column <code>APP.SECTION.TERM_DATA_SET_ID</code>.
	 */
	public final org.jooq.TableField<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, java.lang.Integer> TERM_DATA_SET_ID = createField("TERM_DATA_SET_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>APP.SECTION</code> table reference
	 */
	public Section() {
		this("SECTION", null);
	}

	/**
	 * Create an aliased <code>APP.SECTION</code> table reference
	 */
	public Section(java.lang.String alias) {
		this(alias, io.devyse.scheduler.model.jooq.tables.Section.SECTION);
	}

	private Section(java.lang.String alias, org.jooq.Table<io.devyse.scheduler.model.jooq.tables.records.SectionRecord> aliased) {
		this(alias, aliased, null);
	}

	private Section(java.lang.String alias, org.jooq.Table<io.devyse.scheduler.model.jooq.tables.records.SectionRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, io.devyse.scheduler.model.jooq.App.APP, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, java.lang.Integer> getIdentity() {
		return io.devyse.scheduler.model.jooq.Keys.IDENTITY_SECTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.SectionRecord> getPrimaryKey() {
		return io.devyse.scheduler.model.jooq.Keys.SECTION_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.SectionRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<io.devyse.scheduler.model.jooq.tables.records.SectionRecord>>asList(io.devyse.scheduler.model.jooq.Keys.SECTION_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, ?>>asList(io.devyse.scheduler.model.jooq.Keys.SECTION_FKEY_TERM_DATA_SET_ID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public io.devyse.scheduler.model.jooq.tables.Section as(java.lang.String alias) {
		return new io.devyse.scheduler.model.jooq.tables.Section(alias, this);
	}

	/**
	 * Rename this table
	 */
	public io.devyse.scheduler.model.jooq.tables.Section rename(java.lang.String name) {
		return new io.devyse.scheduler.model.jooq.tables.Section(name, null);
	}
}
