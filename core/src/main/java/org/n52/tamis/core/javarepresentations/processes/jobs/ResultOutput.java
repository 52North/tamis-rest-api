package org.n52.tamis.core.javarepresentations.processes.jobs;

import org.n52.tamis.core.javarepresentations.AbstractOutput;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java representation of a WPS output description inside a result document.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@JsonInclude(Include.NON_NULL)
public class ResultOutput extends AbstractOutput {

	@JsonProperty("Data")
	private String data;

	@JsonProperty("Output")
	private ResultOutput output;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ResultOutput getOutput() {
		return output;
	}

	public void setOutput(ResultOutput output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "ResultOutput [data=" + data + ", output=" + output + "]";
	}

}
