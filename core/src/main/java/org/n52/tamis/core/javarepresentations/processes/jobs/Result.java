package org.n52.tamis.core.javarepresentations.processes.jobs;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java representation of a WPS result.
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class Result {
	
	@JsonProperty("JobID")
	private String jobId;
	
	private List<ResultOutput> outputs = new ArrayList<>();

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public List<ResultOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<ResultOutput> outputs) {
		this.outputs = outputs;
	}

	@Override
	public String toString() {
		return "Result [jobId=" + jobId + ", outputs=" + outputs + "]";
	}

}
