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
	
	/*
	 * The constant service id is used, when a single WPS instance exists.
	 */
	private static final String SERVICE_ID_CONSTANT = "1";
	private static final String STATUS_DESCRIPTION_EXAMPLE_JSON = "/statusDescription_example.json";

	private InputStream input = null;
	
//	private StatusDescriptionDeserializer statusDescrDeser = new StatusDescriptionDeserializer();

	@Test
	public void test() throws JsonParseException, IOException {
		input = this.getClass().getResourceAsStream(STATUS_DESCRIPTION_EXAMPLE_JSON);
		
		StatusDescription statusDescription = new ObjectMapper().readValue(input, StatusDescription.class);
		
		StatusInfo statusInfo = statusDescription.getStatusInfo();
		
		Assert.assertEquals("3c07a1be-1038-474c-a14b-1a693e20640c", statusInfo.getJobId());
		
		Assert.assertEquals("Successful", statusInfo.getStatus());
		
		Assert.assertEquals("http://geoprocessing.demo.52north.org:8080/RestfulWPSProxy/processes/org.n52.wps.server.algorithm.test.EchoProcess/jobs/3c07a1be-1038-474c-a14b-1a693e20640c/outputs", statusInfo.getOutput());

	}

}
