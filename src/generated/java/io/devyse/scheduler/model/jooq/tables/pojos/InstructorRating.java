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
public class InstructorRating implements java.io.Serializable {

	private static final long serialVersionUID = 1734198671;

	private java.lang.Integer id;
	private java.lang.Integer instructorId;
	private java.lang.Double  rating;
	private java.lang.String  description;

	public InstructorRating() {}

	public InstructorRating(
		java.lang.Integer id,
		java.lang.Integer instructorId,
		java.lang.Double  rating,
		java.lang.String  description
	) {
		this.id = id;
		this.instructorId = instructorId;
		this.rating = rating;
		this.description = description;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getInstructorId() {
		return this.instructorId;
	}

	public void setInstructorId(java.lang.Integer instructorId) {
		this.instructorId = instructorId;
	}

	public java.lang.Double getRating() {
		return this.rating;
	}

	public void setRating(java.lang.Double rating) {
		this.rating = rating;
	}

	public java.lang.String getDescription() {
		return this.description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}
}
