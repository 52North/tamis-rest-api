package org.n52.tamis.core.javarepresentations.processes.jobs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java representation of the contents of a status description document.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@JsonInclude(Include.NON_NULL)
//@JsonDeserialize(using = StatusDescriptionDeserializer.class)
public class StatusInfo {

	@JsonProperty("JobID")
	private String jobId;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("Progress")
	private String progress;

	@JsonProperty("Output")
	private String output;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String outputs) {
		this.output = outputs;
	}

	@Override
	public String toString() {
		return "StatusDescription [jobId=" + jobId + ", status=" + status + ", progress=" + progress + ", outputs="
				+ output + "]";
	}

}
