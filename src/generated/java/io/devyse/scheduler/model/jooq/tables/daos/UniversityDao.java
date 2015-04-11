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
public class UniversityDao extends org.jooq.impl.DAOImpl<io.devyse.scheduler.model.jooq.tables.records.UniversityRecord, io.devyse.scheduler.model.jooq.tables.pojos.JooqUniversity, java.lang.Integer> {

	/**
	 * Create a new UniversityDao without any configuration
	 */
	public UniversityDao() {
		super(io.devyse.scheduler.model.jooq.tables.University.UNIVERSITY, io.devyse.scheduler.model.jooq.tables.pojos.JooqUniversity.class);
	}

	/**
	 * Create a new UniversityDao with an attached configuration
	 */
	public UniversityDao(org.jooq.Configuration configuration) {
		super(io.devyse.scheduler.model.jooq.tables.University.UNIVERSITY, io.devyse.scheduler.model.jooq.tables.pojos.JooqUniversity.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected java.lang.Integer getId(io.devyse.scheduler.model.jooq.tables.pojos.JooqUniversity object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>ID IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqUniversity> fetchById(java.lang.Integer... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.University.UNIVERSITY.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>ID = value</code>
	 */
	public io.devyse.scheduler.model.jooq.tables.pojos.JooqUniversity fetchOneById(java.lang.Integer value) {
		return fetchOne(io.devyse.scheduler.model.jooq.tables.University.UNIVERSITY.ID, value);
	}

	/**
	 * Fetch records that have <code>NAME IN (values)</code>
	 */
	public java.util.List<io.devyse.scheduler.model.jooq.tables.pojos.JooqUniversity> fetchByName(java.lang.String... values) {
		return fetch(io.devyse.scheduler.model.jooq.tables.University.UNIVERSITY.NAME, values);
	}
}
