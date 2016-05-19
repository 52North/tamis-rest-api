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
package org.n52.tamis.core.json.deserialize.processes;

import java.io.IOException;

import org.n52.tamis.core.javarepresentations.processes.ProcessDescription_forProcessList;
import org.n52.tamis.core.javarepresentations.processes.Processes_Tamis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class to deserialize the extended processes document returned by the WPS
 * proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ProcessesDeserializer extends JsonDeserializer<Object> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessesDeserializer.class);

	@Override
	public Processes_Tamis deserialize(JsonParser jsonParser, DeserializationContext deserContext)
			throws IOException, JsonProcessingException {
		logger.info("Start deserialization of extended WPS processSummaries document.");

		// initialization
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode node = codec.readTree(jsonParser);

		// create empty shortened CapabilitiesDocument.
		Processes_Tamis processes_short = new Processes_Tamis();

		/*
		 * JSPON element "ProcessSummaries" is an array of process descriptions
		 */
		JsonNode processSummaries = node.get("ProcessSummaries");

		transformAndAddProcessSummaries(processes_short, processSummaries);

		logger.info("Deserialization ended! The following processes description instance was created: {}",
				processes_short);

		return processes_short;

	}

	/**
	 * Converts the extended JSON elements "ProcessSummaries" into instances of
	 * {@link ProcessDescription_forProcessList} instances, which are added to
	 * the processes_short instance.
	 * 
	 * @param processes_short
	 *            instance of shortened process descriptions to which the
	 *            converted process descriptions are added
	 * @param processSummaries
	 *            an array of extended process summaries
	 */
	private void transformAndAddProcessSummaries(Processes_Tamis processes_short, JsonNode processSummaries) {
		for (JsonNode processSummary : processSummaries) {

			/*
			 * empty new process description
			 */
			ProcessDescription_forProcessList processDescription = new ProcessDescription_forProcessList();

			/*
			 * id
			 */
			String id = processSummary.get("identifier").asText();
			processDescription.setId(id);

			/*
			 * label
			 */
			String label = processSummary.get("title").asText();
			processDescription.setLabel(label);

			/*
			 * description: the textual description may be found in the
			 * "abstract" element of the extended process Description. However,
			 * it might be missing!
			 */
			if (processSummary.has("abstract"))
				processDescription.setDescription(processSummary.get("abstract").asText());
			else {
				// just set the title/label as description
				processDescription.setDescription(processDescription.getLabel());
			}

			/*
			 * url
			 * 
			 * url is not set here! Insteadt URL will be set by
			 * ProcessesForwarder, since there the baseURL can be extracted from
			 * the HttpServletRequest object.
			 */

			/*
			 * add constructed process description
			 */
			processes_short.addProcess(processDescription);
		}
	}

}
