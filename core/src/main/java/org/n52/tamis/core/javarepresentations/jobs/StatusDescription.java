package org.n52.tamis.core.javarepresentations.jobs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Java representation of the status description document.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("StatusInfo")
public class StatusDescription {

	@JsonProperty("JobID")
	private String jobId;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("Progress")
	private String progress;

	@JsonProperty("Outputs")
	private String outputs;

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

	public String getOutputs() {
		return outputs;
	}

	public void setOutputs(String outputs) {
		this.outputs = outputs;
	}

}
