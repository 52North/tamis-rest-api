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
package org.n52.tamis.core.json.serialize.processes;

import java.io.IOException;
import java.util.List;

import org.n52.tamis.core.javarepresentations.processes.ProcessDescription_forProcessList;
import org.n52.tamis.core.javarepresentations.processes.Processes_Tamis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Class to serialize and inctance of {@link Processes_Tamis}.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ProcessesSerializer extends JsonSerializer<Processes_Tamis> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessesSerializer.class);

	/**
	 * Serializes the list of processes as an array of processes!
	 */
	@Override
	public void serialize(Processes_Tamis processes, JsonGenerator jsonGenerator, SerializerProvider serProvider)
			throws IOException, JsonProcessingException {
		logger.info("Start serialization of process overview!");

		List<ProcessDescription_forProcessList> processesList = processes.getProcesses();

		ProcessDescription_forProcessList[] processesArray = new ProcessDescription_forProcessList[processesList
				.size()];
		processesArray = processesList.toArray(processesArray);

		ObjectMapper objMapper = new ObjectMapper();
		objMapper.writeValue(jsonGenerator, processesArray);

		logger.info("Serialization of processes overview ended!");

	}

}
