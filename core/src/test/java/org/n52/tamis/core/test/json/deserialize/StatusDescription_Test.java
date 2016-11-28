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
import org.n52.tamis.core.javarepresentations.processes.jobs.StatusDescription;
import org.n52.tamis.core.javarepresentations.processes.jobs.StatusInfo;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StatusDescription_Test {
	
	private static final String STATUS_DESCRIPTION_EXAMPLE_JSON = "/statusDescription_example.json";

	private InputStream input = null;

	@Test
	public void testStatusDescription() throws JsonParseException, IOException {
		input = this.getClass().getResourceAsStream(STATUS_DESCRIPTION_EXAMPLE_JSON);
		
		StatusDescription statusDescription = new ObjectMapper().readValue(input, StatusDescription.class);
		
		StatusInfo statusInfo = statusDescription.getStatusInfo();
		
		Assert.assertEquals("3c07a1be-1038-474c-a14b-1a693e20640c", statusInfo.getJobId());
		
		Assert.assertEquals("Successful", statusInfo.getStatus());
		
		Assert.assertEquals("http://geoprocessing.demo.52north.org:8080/RestfulWPSProxy/processes/org.n52.wps.server.algorithm.test.EchoProcess/jobs/3c07a1be-1038-474c-a14b-1a693e20640c/outputs", statusInfo.getOutput());

	}

}
