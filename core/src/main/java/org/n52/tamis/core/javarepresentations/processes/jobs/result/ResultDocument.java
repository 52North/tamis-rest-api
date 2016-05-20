package org.n52.tamis.core.javarepresentations.processes.jobs.result;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java representation of a WPS result document
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ResultDocument {
	@JsonProperty("Result")
	private Result result;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ResultDocument [result=" + result + "]";
	}
	
}
