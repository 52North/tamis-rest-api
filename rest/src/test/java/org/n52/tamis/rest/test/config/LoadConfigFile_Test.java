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
package org.n52.tamis.rest.test.config;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class LoadConfigFile_Test {

	private static final String TEST_CONFIG_FILE_WPS_PROXY = "/wpsProxy_example.properties";
	private static final String WPS_PROXY_CONFIG_PARAMETER_NAME_BASE_URL = "wpsProxy_baseUrl";

	private static final String EXPECTED_WPS_BASE_URL = "localhost:8080/testapp/base/url";

	@Test
	public void test() throws IOException {
		Properties prop = new Properties();

		InputStream testPropertiesInput = getClass().getResourceAsStream(TEST_CONFIG_FILE_WPS_PROXY);

		prop.load(testPropertiesInput);

		String extractedWpsBaseUrl = prop.getProperty(WPS_PROXY_CONFIG_PARAMETER_NAME_BASE_URL);
		
		assertEquals(EXPECTED_WPS_BASE_URL, extractedWpsBaseUrl);

	}

}
