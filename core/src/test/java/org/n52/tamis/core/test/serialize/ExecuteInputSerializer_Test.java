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

	ExecuteInput referenceInput;
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
	public void setupReferenceInput() {
		referenceInput = new ExecuteInput();

		referenceInput.setId("referenceInputID");
		referenceInput.setValue(
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
			String referenceJsonOutput = mapper.writeValueAsString(referenceInput);
			System.out.println(referenceJsonOutput);

			JsonNode parsedJsonReference = mapper.readTree(referenceJsonOutput);
			JsonNode referenceNode = parsedJsonReference.get("Reference");

			/*
			 * the expected structure of the parsed JSON node looks like:
			 * 
			 *  {
                        "Reference": {
                            "_href": "http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0",
                            "_mimeType": "application/om+xml; version=2.0",
                            "_schema": "http://schemas.opengis.net/om/2.0/observation.xsd"
                        },
                        "_id": "gw1"
                    }

			 */
			
			Assert.assertTrue(referenceNode.has("_href"));
			Assert.assertTrue(parsedJsonReference.has("_id"));
			Assert.assertTrue(referenceNode.has("_schema"));
			Assert.assertTrue(referenceNode.has("_mimeType"));

			
			Assert.assertEquals(
					"http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0",
					referenceNode.get("_href").asText());
			Assert.assertEquals("referenceInputID", parsedJsonReference.get("_id").asText());
			Assert.assertEquals("http://schemas.opengis.net/om/2.0/observation.xsd",
					referenceNode.get("_schema").asText());
			Assert.assertEquals("application/om+xml; version=2.0", referenceNode.get("_mimeType").asText());
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
			System.out.println(dataJsonOutput);

			JsonNode parsedJsonData = mapper.readTree(dataJsonOutput);
			JsonNode dataNode = parsedJsonData.get("Data");

			/*
			 * the expected structure of the parsed JSON node looks like:
			 * 
			 *  {
                        "Reference": {
                            "_href": "http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0",
                            "_mimeType": "application/om+xml; version=2.0",
                            "_schema": "http://schemas.opengis.net/om/2.0/observation.xsd"
                        },
                        "_id": "gw1"
                    }

			 */
			
			Assert.assertTrue(dataNode.has("_text"));
			Assert.assertTrue(parsedJsonData.has("_id"));
//			Assert.assertTrue(dataNode.has("_schema"));
//			Assert.assertTrue(dataNode.has("_mimeType"));

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
