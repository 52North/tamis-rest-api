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
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.n52.tamis.core.javarepresentations.processes.ProcessDescription_forProcessList;
import org.n52.tamis.core.javarepresentations.processes.Processes_Tamis;
import org.n52.tamis.core.json.deserialize.processes.ProcessesDeserializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class to test the custom {@link ProcessesDeserializer}.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ProcessesDeserializer_Test {

	private static final String EXTENDED_PROCESSES_EXAMPLE_JSON = "/extendedProcesses_example.json";
	private InputStream input = null;
	private JsonParser jsonParser = null;

	private ProcessesDeserializer processesDeserializer = new ProcessesDeserializer();

	/**
	 * Parses the example document located at
	 * "src/test/resources/extendedProcesses_example.json" and deserializes its
	 * content into an instance of {@link Processes_Tamis}
	 */
	@Test
	public void test() {
		try {

			input = this.getClass().getResourceAsStream(EXTENDED_PROCESSES_EXAMPLE_JSON);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonFactory jsonFactory = objectMapper.getFactory();

			this.jsonParser = jsonFactory.createParser(input);

			Processes_Tamis processes_short = processesDeserializer.deserialize(jsonParser, null);

			/*
			 * Assert that values of the intantiated Processes_Tamis object
			 * match the expected parameters from the example document
			 */
			Assert.assertNotNull(processes_short);

			List<ProcessDescription_forProcessList> processDescriptions = processes_short.getProcesses();

			Assert.assertEquals(2, processDescriptions.size());

			ProcessDescription_forProcessList firstProcessDescription = processDescriptions.get(0);
			ProcessDescription_forProcessList secondProcessDescription = processDescriptions.get(1);

			/*
			 * assertions for first process description
			 */
			// in the example document there was no "abstract" element, thus,
			// description should be equal to label
			Assert.assertEquals(firstProcessDescription.getLabel(), firstProcessDescription.getDescription());
			Assert.assertEquals("org.n52.tamis.algorithm.interpolation", firstProcessDescription.getId());
			Assert.assertEquals("TAMIS Interpolation Process", firstProcessDescription.getLabel());

			/*
			 * assertions for second process description
			 */
			Assert.assertEquals(secondProcessDescription.getLabel(), secondProcessDescription.getDescription());
			Assert.assertEquals("org.n52.tamis.algorithm.soakageregressionmodel", secondProcessDescription.getId());
			Assert.assertEquals("TAMIS Soakage Regression Model", secondProcessDescription.getLabel());

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
