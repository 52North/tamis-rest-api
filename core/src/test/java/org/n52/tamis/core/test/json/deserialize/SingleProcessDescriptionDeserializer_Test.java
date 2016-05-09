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
import org.n52.tamis.core.javarepresentations.processes.ProcessDescriptionInput;
import org.n52.tamis.core.javarepresentations.processes.ProcessDescriptionOutput;
import org.n52.tamis.core.javarepresentations.processes.ProcessDescription_singleProcess;
import org.n52.tamis.core.json.deserialize.processes.SingleProcessDescriptionDeserializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SingleProcessDescriptionDeserializer_Test {

	private static final String EXTENDED_SINGLE_PROCESS_DESCRIPTION_EXAMPLE_JSON = "/extendedSingleProcessDescription_example.json";
	private InputStream input = null;
	private JsonParser jsonParser = null;

	private SingleProcessDescriptionDeserializer processesDeserializer = new SingleProcessDescriptionDeserializer();

	/**
	 * Parses the example document located at
	 * "src/test/resources/extendedSingleProcessDescription_example.json" and
	 * deserializes its content into an instance of
	 * {@link ProcessDescription_singleProcess}
	 */
	@Test
	public void test() {
		try {

			input = this.getClass().getResourceAsStream(EXTENDED_SINGLE_PROCESS_DESCRIPTION_EXAMPLE_JSON);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonFactory jsonFactory = objectMapper.getFactory();

			this.jsonParser = jsonFactory.createParser(input);

			ProcessDescription_singleProcess singleProcessDescription_short = processesDeserializer
					.deserialize(jsonParser, null);

			/*
			 * Assert that values of the intantiated
			 * ProcessDescription_singleProcess object match the expected
			 * parameters from the example document
			 */
			Assert.assertNotNull(singleProcessDescription_short);

			/*
			 * id
			 */
			Assert.assertEquals("org.n52.tamis.algorithm.interpolation", singleProcessDescription_short.getId());

			/*
			 * label
			 */
			Assert.assertEquals("TAMIS Interpolation Process", singleProcessDescription_short.getLabel());

			/*
			 * inputs
			 */
			List<ProcessDescriptionInput> inputs = singleProcessDescription_short.getInputs();

			Assert.assertEquals(3, inputs.size());

			/*
			 * first Input
			 */
			ProcessDescriptionInput firstInput = inputs.get(0);
			Assert.assertEquals("target-grid", firstInput.getId());
			Assert.assertEquals("Target Grid", firstInput.getLabel());
			Assert.assertEquals("text/xml | application/x-netcdf", firstInput.getType());
			Assert.assertEquals("true", firstInput.getRequired());

			/*
			 * second Input
			 */
			ProcessDescriptionInput secondInput = inputs.get(1);
			Assert.assertEquals("interpolation-method", secondInput.getId());
			Assert.assertEquals("Interpolation Method", secondInput.getLabel());
			Assert.assertEquals("text/plain | text/xml", secondInput.getType());
			Assert.assertEquals("true", secondInput.getRequired());

			/*
			 * third Input
			 */
			ProcessDescriptionInput thirdInput = inputs.get(2);
			Assert.assertEquals("input-values", thirdInput.getId());
			Assert.assertEquals("Input Values", thirdInput.getLabel());
			Assert.assertEquals("application/om+xml; version=2.0", thirdInput.getType());
			Assert.assertEquals("true", thirdInput.getRequired());

			/*
			 * output(s)
			 */
			List<ProcessDescriptionOutput> outputs = singleProcessDescription_short.getOutputs();
			Assert.assertEquals(1, outputs.size());
			ProcessDescriptionOutput firstOutput = outputs.get(0);
			Assert.assertEquals("interpolated-values", firstOutput.getId());
			Assert.assertEquals("Interpolated Values", firstOutput.getLabel());
			Assert.assertEquals("application/geotiff | application/x-netcdf", firstOutput.getType());

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
