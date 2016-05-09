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
package org.n52.tamis.core.test.json.deserialize;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.n52.tamis.core.javarepresentations.capabilities.Capabilities_Tamis;
import org.n52.tamis.core.json.deserialize.capabilities.CapabilitiesDeserializer;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class to test the custom {@link CapabilitiesDeserializer}.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class CapabilitiesDeserializer_Test {

	/*
	 * The constant service id is used, when a single WPS instance exists.
	 */
	private static final String SERVICE_ID_CONSTANT = "1";
	private static final String EXTENDED_CAPABILITIES_EXAMPLE_JSON = "/extendedCapabilities_example.json";

	private InputStream input = null;
	private JsonParser jsonParser = null;

	private CapabilitiesDeserializer capabilitiesDeserializer = new CapabilitiesDeserializer();

	/**
	 * Parses the example document located at
	 * "src/test/resources/extendedCapabilities_example.json" and deserializes
	 * its content into an instance of {@link Capabilities_Tamis}
	 */
	@Test
	public void test() {
		try {

			input = this.getClass().getResourceAsStream(EXTENDED_CAPABILITIES_EXAMPLE_JSON);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonFactory jsonFactory = objectMapper.getFactory();

			this.jsonParser = jsonFactory.createParser(input);

			Capabilities_Tamis capabilities_short = capabilitiesDeserializer.deserialize(jsonParser, null);

			/*
			 * Assert that values of the intantiated Capabilities_Tamis object
			 * match the expected parameters from the example document
			 */
			Assert.assertNotNull(capabilities_short);
			Assert.assertEquals("52°North WPS 4.0.0-SNAPSHOT", capabilities_short.getLabel());
			Assert.assertEquals("contact@52north.org", capabilities_short.getContact());
			Assert.assertEquals(String.valueOf(SERVICE_ID_CONSTANT), capabilities_short.getId().toString());
			Assert.assertEquals("WPS", capabilities_short.getType());
			Assert.assertEquals(URL_Constants_TAMIS.getCapabilitiesURL(SERVICE_ID_CONSTANT),
					capabilities_short.getUrl());

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
