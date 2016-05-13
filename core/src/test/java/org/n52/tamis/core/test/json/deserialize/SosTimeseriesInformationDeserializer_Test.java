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
import org.n52.tamis.core.javarepresentations.processes.execute.SosTimeseriesInformation;
import org.n52.tamis.core.json.deserialize.processes.execute.SosTimeseriesInformationDeserializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SosTimeseriesInformationDeserializer_Test {

	private static final String SOS_TIMESERIES_INFORMATION_EXAMPLE_JSON = "/SosTimeseriesInformation_example.json";
	private InputStream input = null;
	private JsonParser jsonParser = null;

	private SosTimeseriesInformationDeserializer sosTimeseriesInformationDeserializer = new SosTimeseriesInformationDeserializer();

	/**
	 * Parses the example document located at
	 * "src/test/resources/SosTimeseriesInformation_example.json" and
	 * deserializes its content into an instance of
	 * {@link SosTimeseriesInformation}
	 */
	@Test
	public void test() {
		try {

			input = this.getClass().getResourceAsStream(SOS_TIMESERIES_INFORMATION_EXAMPLE_JSON);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonFactory jsonFactory = objectMapper.getFactory();

			this.jsonParser = jsonFactory.createParser(input);

			SosTimeseriesInformation sosTimeseriesInformation = sosTimeseriesInformationDeserializer
					.deserialize(jsonParser, null);

			/*
			 * Assert that values of the intantiated SosTimeseriesInformation
			 * object match the expected parameters from the example document
			 */
			Assert.assertNotNull(sosTimeseriesInformation);

			/*
			 * feature of interest
			 */
			Assert.assertEquals("sta_a4bff7499d440d9bb67ad73e048c5a47",
					sosTimeseriesInformation.getFeatureOfInterest());

			/*
			 * SOS URL
			 */
			Assert.assertEquals("http://pegelonline.wsv.de/webservices/gis/gdi-sos",
					sosTimeseriesInformation.getSosUrl());

			/*
			 * offering id
			 */
			Assert.assertEquals("off_c599e9b089385d08aa00b7c9c4c6f3ef", sosTimeseriesInformation.getOfferingId());

			/*
			 * procedure
			 */
			Assert.assertEquals("pro_e878d48e49ebc2a156e2810470bae9a2", sosTimeseriesInformation.getProcedure());

			/*
			 * observedProperty
			 */
			Assert.assertEquals("phe_f60bcfb1f6ac3d37210b5d757a1bf48e", sosTimeseriesInformation.getObservedProperty());

			/*
			 * temporal filter cannot be set via JsonDeserializer since the
			 * document does not contain this information.! Thus, cannot be set!
			 */

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
