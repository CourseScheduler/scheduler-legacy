/**
 * This class is generated by jOOQ
 */
package io.devyse.scheduler.model.jooq.tables.pojos;

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
public class University implements java.io.Serializable {

	private static final long serialVersionUID = 1480733983;

	private java.lang.Integer id;
	private java.lang.String  name;

	public University() {}

	public University(
		java.lang.Integer id,
		java.lang.String  name
	) {
		this.id = id;
		this.name = name;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getName() {
		return this.name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}
}
