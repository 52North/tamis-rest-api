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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteInput;
import org.n52.tamis.core.json.serialize.processes.execute.ExecuteInputSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ExecuteInputSerializer_Test {

	ExecuteInput sosInput;
	ExecuteInput dataInput;
	
	ExecuteInputSerializer input_WpsProxySerializer;
	ObjectMapper mapper;

	@Before
	public void setupMapper(){
		input_WpsProxySerializer = new ExecuteInputSerializer();

		mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addSerializer(ExecuteInput.class, input_WpsProxySerializer);
		mapper.registerModule(module);
	}
	
	@Before
	public void setupSosInput() {
		sosInput = new ExecuteInput();

		sosInput.setId("sosInputID");
		sosInput.setValue(
				"http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0");
	}

	@Before
	public void setupDataInput() {
		dataInput = new ExecuteInput();

		dataInput.setId("dataInputID");
		dataInput.setValue(
				"[0.03550405161598, -0.01860639146241, -0.01860639146241, -0.03550405161598, 385795.23669382796000, 5667086.67852447180000]");
	}

	@Test
	public void testReferenceInputSerialization() {

		try {
			String sosJsonOutput = mapper.writeValueAsString(sosInput);

			JsonNode parsedJsonReference = mapper.readTree(sosJsonOutput);
			JsonNode sosNode = parsedJsonReference.get("LiteralData");

			/*
			 * the expected structure of the parsed JSON node looks like:
			 * 
			 *  {
                        "Data": {
                            "_text": "http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0"
                        },
                        "_id": "gw1"
                    }

			 */
			
			Assert.assertTrue(sosNode.has("_text"));
			Assert.assertTrue(parsedJsonReference.has("_id"));

			
			Assert.assertEquals(
					"http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0",
					sosNode.get("_text").asText());
			Assert.assertEquals("sosInputID", parsedJsonReference.get("_id").asText());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDataInputSerialization() {

		try {
			String dataJsonOutput = mapper.writeValueAsString(dataInput);

			JsonNode parsedJsonData = mapper.readTree(dataJsonOutput);
			JsonNode dataNode = parsedJsonData.get("LiteralData");
			
			Assert.assertTrue(dataNode.has("_text"));
			Assert.assertTrue(parsedJsonData.has("_id"));

			Assert.assertEquals(
					"[0.03550405161598, -0.01860639146241, -0.01860639146241, -0.03550405161598, 385795.23669382796000, 5667086.67852447180000]",
					dataNode.get("_text").asText());
			Assert.assertEquals("dataInputID", parsedJsonData.get("_id").asText());

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
