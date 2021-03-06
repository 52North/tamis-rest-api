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
package org.n52.tamis.core.test.serialize;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteInput;
import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteOutput;
import org.n52.tamis.core.javarepresentations.processes.execute.Execute_HttpPostBody;
import org.n52.tamis.core.json.serialize.processes.execute.ExecuteInputSerializer;
import org.n52.tamis.core.json.serialize.processes.execute.ExecuteOutputSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ExecuteInputBodySerializer_Test {

	Execute_HttpPostBody executeBody;

	ExecuteInput referenceInput;
	ExecuteInput dataInput;

	ExecuteOutput executeOutput;

	ExecuteInputSerializer input_Serializer;
	ExecuteOutputSerializer output_Serializer;
	ObjectMapper mapper;

	public void setupMapper() {
		input_Serializer = new ExecuteInputSerializer();
		output_Serializer = new ExecuteOutputSerializer();

		mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addSerializer(ExecuteInput.class, input_Serializer);
		module.addSerializer(ExecuteOutput.class, output_Serializer);
		mapper.registerModule(module);
	}

	public void setupReferenceInput() {
		referenceInput = new ExecuteInput();

		referenceInput.setId("referenceInputID");
		referenceInput.setValue(
				"http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0");
	}

	public void setupDataInput() {
		dataInput = new ExecuteInput();

		dataInput.setId("dataInputID");
		dataInput.setValue(
				"[0.03550405161598, -0.01860639146241, -0.01860639146241, -0.03550405161598, 385795.23669382796000, 5667086.67852447180000]");
	}

	public void setupExecuteOutput() {
		executeOutput = new ExecuteOutput();

		executeOutput.setId("executeOutputID");
		executeOutput.setType("application/json");
	}

	@Before
	public void setupExecuteBody() {
		setupMapper();
		setupDataInput();
		setupReferenceInput();
		setupExecuteOutput();
		
		executeBody = new Execute_HttpPostBody();

		executeBody.setProcessId("example.processId");
		executeBody.setInputs(Arrays.asList(referenceInput, dataInput));
		executeBody.setOutputs(Arrays.asList(executeOutput));
		executeBody.setVersion("1.0.0");
	}

	@Test
	public void testExecuteBodySerialization() {

		try {
			String execBodyJsonOutput = mapper.writeValueAsString(executeBody);
			System.out.println(execBodyJsonOutput);

			JsonNode parsedJsonExecBody = mapper.readTree(execBodyJsonOutput);
			JsonNode executeNode = parsedJsonExecBody.get("Execute");

			/*
			 * the expected structure of the parsed JSON node looks like:
			 * 
			 * { "Execute": { "Identifier":
			 * "org.n52.wps.server.algorithm.test.EchoProcess", "Input": [ {
			 * "Reference": { "_mimeType": "text/xml", "_href":
			 * "http://geoprocessing.demo.52north.org:8080/geoserver/wfs?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetFeature&TYPENAME=topp:tasmania_roads&SRS=EPSG:4326&OUTPUTFORMAT=GML3"
			 * }, "_id": "complexInput" } ], "output":[{ "_mimeType":
			 * "text/xml", "_id": "complexOutput", "_transmission": "reference"
			 * }], "_service": "WPS", "_version": "2.0.0"} }
			 * 
			 */

			Assert.assertTrue(executeNode.has("Identifier"));
			Assert.assertTrue(executeNode.has("Input"));
			Assert.assertTrue(executeNode.has("output"));
			Assert.assertTrue(executeNode.has("_service"));
			Assert.assertTrue(executeNode.has("_version"));

			Assert.assertEquals("example.processId", executeNode.get("Identifier").asText());
			Assert.assertEquals("WPS", executeNode.get("_service").asText());

			JsonNode inputs = executeNode.get("Input");

			Assert.assertTrue(inputs.isArray());
			Assert.assertTrue(inputs.size() == 2);
			/*
			 * the actual structure of Input content is tested in a different
			 * JUnit test case (ExecuteInputSerialier_Test)
			 */

			/*
			 * test output
			 */
			JsonNode outputs = executeNode.get("output");
			Assert.assertTrue(outputs.isArray());

			JsonNode singleOutput = outputs.get(0);
			Assert.assertEquals("application/json", singleOutput.get("_mimeType").asText());
			Assert.assertEquals("executeOutputID", singleOutput.get("_id").asText());

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
