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
package org.n52.tamis.core.javarepresentations.execute;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is a Java representation of the HTTP POST input for a WPS execute
 * request.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Execute_HttpPostInput {

	private List<ExecuteInput> inputs;

	public Execute_HttpPostInput() {
	}

	public List<ExecuteInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<ExecuteInput> inputs) {
		this.inputs = inputs;
	}

	@Override
	public String toString() {
		return "Execute_HttpPostInput [inputs=" + inputs + "]";
	}

}
