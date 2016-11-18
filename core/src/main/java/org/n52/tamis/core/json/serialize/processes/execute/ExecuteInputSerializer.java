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
package org.n52.tamis.core.json.serialize.processes.execute;

import java.io.IOException;

import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializes the simple JSON HTTP POST input element of the request body.
 * 
 * Inputs are distuingished by their value parameter. If value contains an HTTP
 * address, the value will be converted to a "Reference"-WPS-input element.
 * Otherwise the input will be converted to a "Data"-WPS-input element.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ExecuteInputSerializer extends JsonSerializer<ExecuteInput> {

	private final static Logger logger = LoggerFactory.getLogger(ExecuteInputSerializer.class);

	@Override
	public void serialize(ExecuteInput input_simple, JsonGenerator jsonGenerator, SerializerProvider serProvider)
			throws IOException, JsonProcessingException {

		logger.info("Start serialization of input \"{}\"", input_simple);

		String inputValue = input_simple.getValue();

		/*
		 * usually, any Web links should be mapped to a WPS "Reference" element.
		 * But if the URL is a SOS (possibly even timeseries REST) URL, then the
		 * WPS expects it as Data!!!
		 * 
		 * so: 1. if url contains "timeseries" then map it to Data.
		 * 
		 * 2. else write the input as WPS "Reference" element
		 */
		if (inputValue.contains("http")) {

			logger.debug("detected \"http\" inside value of the input.");

			if (inputValue.contains("timeseries") || inputValue.contains("SOS") || inputValue.contains("sos")) {
				/*
				 * although this is a Web link, we will serialize it as "Data",
				 * since it is a SOS URL, which is expected as "Data" by the WPS
				 */
				logger.info("SOS-URL detected: \"{}\"", inputValue);
				writeAsData(jsonGenerator, input_simple);
			} else
				writeAsReference(jsonGenerator, input_simple);
		} else {
			writeAsData(jsonGenerator, input_simple);
		}

		logger.info("serialization of input ended.");
	}

	private void writeAsData(JsonGenerator jsonGenerator, ExecuteInput input) throws IOException {
		/*
		 * write the input as WPS "Data" element
		 * 
		 * expected structure looks like:
		 * 
		 * { "Data": { "_mimeType": "text/xml", "_schema":
		 * "http://schemas.opengis.net/gml/3.2.1/base/feature.xsd",
		 * "__text": "value" }, 
		 * "_id": "target-variable-point" 
		 * }
		 * 
		 * 
		 */
		logger.info("Input \"{}\" is serialized as WPS \"Data\".", input);

		jsonGenerator.writeStartObject();

		jsonGenerator.writeObjectFieldStart("LiteralData");

		jsonGenerator.writeStringField("_text", input.getValue());//TODO duplicate call to input.getValue()..

		/*
		 * TODO parameters "_mimeType" and "_schema"?
		 */
		// jsonGenerator.writeStringField("_mimeType", "application/om+xml;
		// version=2.0");
		// jsonGenerator.writeStringField("_schema",
		// "http://schemas.opengis.net/om/2.0/observation.xsd");

		jsonGenerator.writeEndObject();

		jsonGenerator.writeStringField("_id", input.getId());

		jsonGenerator.writeEndObject();
	}

	private void writeAsReference(JsonGenerator jsonGenerator, ExecuteInput input) throws IOException {
		/*
		 *  expected structure looks like:
		* 
			 * "Reference": { "_href":
			 * "http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0",
			 * "_mimeType": "application/om+xml; version=2.0", "_schema":
			 * "http://schemas.opengis.net/om/2.0/observation.xsd" }, "_id":
			 * "gw1" }
			 * 
		 */
		
		logger.info("Input \"{}\" is serialized as WPS \"Reference\".", input);
		
		jsonGenerator.writeStartObject();

		jsonGenerator.writeObjectFieldStart("Reference");

		// jsonGenerator.writeStartObject();

		jsonGenerator.writeStringField("_href", input.getValue());

		/*
		 * parameters "_mimeType" and "_schema" can be set to SOS values, if
		 * the link is a SOS request!
		 * 
		 * Else we do not know anything about the mimeType. Thus we cannot
		 * set it.
		 */

		jsonGenerator.writeEndObject();

		jsonGenerator.writeStringField("_id", input.getId());

		jsonGenerator.writeEndObject();
	}

}
