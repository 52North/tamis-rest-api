package org.n52.tamis.core.javarepresentations.processes.execute;

import org.n52.tamis.core.javarepresentations.AbstractOutput;
import org.n52.tamis.core.json.serialize.processes.execute.ExecuteOutputSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Java representation of single output element of HTTP POST body of WPS execute
 * request.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@JsonSerialize(using = ExecuteOutputSerializer.class)
public class ExecuteOutput extends AbstractOutput {

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ExecuteOutput [type=" + type + "]";
	}

}