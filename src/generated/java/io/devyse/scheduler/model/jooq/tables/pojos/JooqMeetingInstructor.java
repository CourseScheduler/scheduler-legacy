/**
 * This class is generated by jOOQ
 */
package io.devyse.scheduler.model.jooq.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JooqMeetingInstructor implements Serializable {

	private static final long serialVersionUID = -1898500562;

	private Integer id;
	private Integer meetingId;
	private Integer instructorId;

	public JooqMeetingInstructor() {}

	public JooqMeetingInstructor(JooqMeetingInstructor value) {
		this.id = value.id;
		this.meetingId = value.meetingId;
		this.instructorId = value.instructorId;
	}

	public JooqMeetingInstructor(
		Integer id,
		Integer meetingId,
		Integer instructorId
	) {
		this.id = id;
		this.meetingId = meetingId;
		this.instructorId = instructorId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(Integer meetingId) {
		this.meetingId = meetingId;
	}

	public Integer getInstructorId() {
		return this.instructorId;
	}

	public void setInstructorId(Integer instructorId) {
		this.instructorId = instructorId;
	}
}
