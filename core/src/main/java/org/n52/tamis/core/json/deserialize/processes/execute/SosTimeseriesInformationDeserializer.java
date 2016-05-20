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
package org.n52.tamis.core.json.deserialize.processes.execute;

import java.io.IOException;

import org.n52.tamis.core.javarepresentations.processes.execute.SosTimeseriesInformation;
import org.n52.tamis.core.json.deserialize.processes.ProcessesDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class to deserialize the JSON output of a timeseries-REST-api request
 * (/api/v1/timeseries/{id}?expanded=true) into an instance of
 * {@link SosTimeseriesInformation}
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class SosTimeseriesInformationDeserializer extends JsonDeserializer<SosTimeseriesInformation> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessesDeserializer.class);

	@Override
	public SosTimeseriesInformation deserialize(JsonParser jsonParser, DeserializationContext deserContext)
			throws IOException, JsonProcessingException {
		logger.info("Start deserialization of SOS timeseries output.");

		SosTimeseriesInformation sosInformation = new SosTimeseriesInformation();

		// initialization
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode node = codec.readTree(jsonParser);

		/*
		 * feature of interest
		 */
		String featureOfInterest = node.get("station").get("properties").get("id").asText();
		sosInformation.setFeatureOfInterest(featureOfInterest);

		/*
		 * SOS base URL
		 */
		String baseUrl = node.get("parameters").get("service").get("serviceUrl").asText();
		sosInformation.setSosUrl(baseUrl);

		/*
		 * offering id
		 */
		String offeringId = node.get("parameters").get("offering").get("id").asText();
		sosInformation.setOfferingId(offeringId);

		/*
		 * procedure
		 */
		String procedure = node.get("parameters").get("procedure").get("id").asText();
		sosInformation.setProcedure(procedure);

		/*
		 * observed property
		 */
		String observedProperty = node.get("parameters").get("phenomenon").get("id").asText();
		sosInformation.setObservedProperty(observedProperty);

		/*
		 * temporal filter cannot be set. This must happen later!
		 */

		logger.info("Deserialization of SOS timeseries output ended. Following object was constructed: \"{}\".",
				sosInformation);

		return sosInformation;
	}

}
