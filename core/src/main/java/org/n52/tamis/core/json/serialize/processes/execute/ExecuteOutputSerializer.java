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

import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializes the simple JSON HTTP POST output element of the request body.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ExecuteOutputSerializer extends JsonSerializer<ExecuteOutput> {

	private final static Logger logger = LoggerFactory.getLogger(ExecuteOutputSerializer.class);

	@Override
	public void serialize(ExecuteOutput output_simple, JsonGenerator jsonGenerator, SerializerProvider serProvider)
			throws IOException, JsonProcessingException {

		logger.info("Start serialization of output \"{}\"", output_simple);
		
			/*
			 * write the input as WPS "Data" element
			 * 
			 * expected structure looks like:
			 * 
			 * 	{
            		"_mimeType": "text/xml",
            		"_id": "complexOutput",
            		"_transmission": "reference"                <-- transmission is optional, we leave it out
        		}
			 */
		
		jsonGenerator.writeStartObject();

		jsonGenerator.writeStringField("_mimeType", output_simple.getType());

		jsonGenerator.writeStringField("_id", output_simple.getId());

		jsonGenerator.writeEndObject();

		logger.info("serialization of output ended.");
		}
}
