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
package org.n52.tamis.core.javarepresentations.processes;

import java.util.ArrayList;
import java.util.List;

import org.n52.tamis.core.json.deserialize.processes.SingleProcessDescriptionDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = SingleProcessDescriptionDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDescription_singleProcess extends AbstractProcessDescription {

	private List<ProcessDescriptionInput> inputs;

	private List<ProcessDescriptionOutput> outputs;

	public ProcessDescription_singleProcess() {

		this.inputs = new ArrayList<ProcessDescriptionInput>();
		this.outputs = new ArrayList<ProcessDescriptionOutput>();
	}

	public List<ProcessDescriptionInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<ProcessDescriptionInput> inputs) {
		this.inputs = inputs;
	}

	public void addInput(ProcessDescriptionInput input) {
		this.inputs.add(input);
	}

	public List<ProcessDescriptionOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<ProcessDescriptionOutput> outputs) {
		this.outputs = outputs;
	}

	public void addInput(ProcessDescriptionOutput output) {
		this.outputs.add(output);
	}

	@Override
	public String toString() {
		return "ProcessDescription_singleProcess [id=" + this.getId() + ", label=" + this.getLabel() + ", description="
				+ this.getDescription() + ", inputs=" + inputs + ", outputs=" + outputs + "]";
	}

}
