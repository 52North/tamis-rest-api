package org.n52.tamis.core.javarepresentations.processes.jobs.result;

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
public class ResultOutput {

	@JsonProperty("ID")
	private String id;
	
	@JsonProperty("Data")
	private ResultData data;
	
	@JsonProperty("Reference")
	private ResultReference reference;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ResultReference getReference() {
		return reference;
	}

	public void setReference(ResultReference reference) {
		this.reference = reference;
	}

	public ResultData getData() {
		return data;
	}

	public void setData(ResultData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResultOutput [id=" + id + ", data=" + data + ", reference=" + reference + "]";
	}

}
