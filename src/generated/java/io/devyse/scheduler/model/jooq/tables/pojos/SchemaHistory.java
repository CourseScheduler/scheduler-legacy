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
public class SchemaHistory implements java.io.Serializable {

	private static final long serialVersionUID = -1782574176;

	private java.lang.Integer  versionRank;
	private java.lang.Integer  installedRank;
	private java.lang.String   version;
	private java.lang.String   description;
	private java.lang.String   type;
	private java.lang.String   script;
	private java.lang.Integer  checksum;
	private java.lang.String   installedBy;
	private java.sql.Timestamp installedOn;
	private java.lang.Integer  executionTime;
	private java.lang.Boolean  success;

	public SchemaHistory() {}

	public SchemaHistory(
		java.lang.Integer  versionRank,
		java.lang.Integer  installedRank,
		java.lang.String   version,
		java.lang.String   description,
		java.lang.String   type,
		java.lang.String   script,
		java.lang.Integer  checksum,
		java.lang.String   installedBy,
		java.sql.Timestamp installedOn,
		java.lang.Integer  executionTime,
		java.lang.Boolean  success
	) {
		this.versionRank = versionRank;
		this.installedRank = installedRank;
		this.version = version;
		this.description = description;
		this.type = type;
		this.script = script;
		this.checksum = checksum;
		this.installedBy = installedBy;
		this.installedOn = installedOn;
		this.executionTime = executionTime;
		this.success = success;
	}

	public java.lang.Integer getVersionRank() {
		return this.versionRank;
	}

	public void setVersionRank(java.lang.Integer versionRank) {
		this.versionRank = versionRank;
	}

	public java.lang.Integer getInstalledRank() {
		return this.installedRank;
	}

	public void setInstalledRank(java.lang.Integer installedRank) {
		this.installedRank = installedRank;
	}

	public java.lang.String getVersion() {
		return this.version;
	}

	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	public java.lang.String getDescription() {
		return this.description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getType() {
		return this.type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

	public java.lang.String getScript() {
		return this.script;
	}

	public void setScript(java.lang.String script) {
		this.script = script;
	}

	public java.lang.Integer getChecksum() {
		return this.checksum;
	}

	public void setChecksum(java.lang.Integer checksum) {
		this.checksum = checksum;
	}

	public java.lang.String getInstalledBy() {
		return this.installedBy;
	}

	public void setInstalledBy(java.lang.String installedBy) {
		this.installedBy = installedBy;
	}

	public java.sql.Timestamp getInstalledOn() {
		return this.installedOn;
	}

	public void setInstalledOn(java.sql.Timestamp installedOn) {
		this.installedOn = installedOn;
	}

	public java.lang.Integer getExecutionTime() {
		return this.executionTime;
	}

	public void setExecutionTime(java.lang.Integer executionTime) {
		this.executionTime = executionTime;
	}

	public java.lang.Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(java.lang.Boolean success) {
		this.success = success;
	}
}
