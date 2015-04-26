/**
 * This class is generated by jOOQ
 */
package io.devyse.scheduler.model.jooq.tables.daos;

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
public class JooqSectionDao extends org.jooq.impl.DAOImpl<io.devyse.scheduler.model.jooq.tables.records.SectionRecord, io.devyse.scheduler.model.jooq.tables.pojos.JooqSection, java.lang.Integer> {

	/**
	 * Create a new JooqSectionDao without any configuration
	 */
	public JooqSectionDao() {
		super(io.devyse.scheduler.model.jooq.tables.Section.SECTION, io.devyse.scheduler.model.jooq.tables.pojos.JooqSection.class);
	}

	/**
	 * Create a new JooqSectionDao with an attached configuration
	 */
	public JooqSectionDao(org.jooq.Configuration configuration) {
		super(io.devyse.scheduler.model.jooq.tables.Section.SECTION, io.devyse.scheduler.model.jooq.tables.pojos.JooqSection.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected java.lang.Integer getId(io.devyse.scheduler.model.jooq.tables.pojos.JooqSection object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>ID IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqSection> fetchById(java.lang.Integer... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.Section.SECTION.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>ID = value</code>
	 */
	public io.devyse.scheduler.model.jooq.tables.pojos.JooqSection fetchOneById(java.lang.Integer value) {
		return fetchOne(io.devyse.scheduler.model.jooq.tables.Section.SECTION.ID, value);
	}

	/**
	 * Fetch records that have <code>CRN IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqSection> fetchByCrn(java.lang.String... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.Section.SECTION.CRN, values);
	}

	/**
	 * Fetch records that have <code>COURSE_NUMBER IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqSection> fetchByCourseNumber(java.lang.String... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.Section.SECTION.COURSE_NUMBER, values);
	}

	/**
	 * Fetch records that have <code>DESCRIPTION IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqSection> fetchByDescription(java.lang.String... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.Section.SECTION.DESCRIPTION, values);
	}

	/**
	 * Fetch records that have <code>NAME IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqSection> fetchByName(java.lang.String... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.Section.SECTION.NAME, values);
	}

	/**
	 * Fetch records that have <code>SECTION_NUMBER IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqSection> fetchBySectionNumber(java.lang.String... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.Section.SECTION.SECTION_NUMBER, values);
	}

	/**
	 * Fetch records that have <code>TERM_DATA_SET_ID IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqSection> fetchByTermDataSetId(java.lang.Integer... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.Section.SECTION.TERM_DATA_SET_ID, values);
	}
}