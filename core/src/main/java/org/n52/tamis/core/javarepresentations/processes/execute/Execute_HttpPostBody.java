/**
 * Copyright (C) 2016-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.tamis.core.javarepresentations.processes.execute;

import java.util.List;

import org.n52.tamis.core.json.serialize.processes.execute.ExtendedExecuteHttpPostBodySerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This class is a Java representation of the HTTP POST input, received from a
 * client, for a WPS execute request.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = ExtendedExecuteHttpPostBodySerializer.class)
public class Execute_HttpPostBody {
	
	@JsonIgnore
	public static final String service = "WPS";

	private List<ExecuteInput> inputs;
	
	private List<ExecuteOutput> outputs;
	
	@JsonIgnore
	private String processId;
	
	@JsonIgnore
	private String version = "2.0.0" ;

	public Execute_HttpPostBody() {
	}

	public List<ExecuteInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<ExecuteInput> inputs) {
		this.inputs = inputs;
	}

	public List<ExecuteOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<ExecuteOutput> outputs) {
		this.outputs = outputs;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Execute_HttpPostBody [inputs=" + inputs + ", outputs=" + outputs + ", processId=" + processId
				+ ", version=" + version + "]";
	}

}
