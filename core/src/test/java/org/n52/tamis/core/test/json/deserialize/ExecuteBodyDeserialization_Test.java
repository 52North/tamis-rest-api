package org.n52.tamis.core.test.json.deserialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteInput;
import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteOutput;
import org.n52.tamis.core.javarepresentations.processes.execute.Execute_HttpPostBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecuteBodyDeserialization_Test {

	private static final String EXECUTE_BODY_EXAMPLE_JSON = "/executeBody_example.json";

	private InputStream input = null;

	@Test
	public void testExecuteBodyDeserialization() throws JsonParseException, JsonMappingException, IOException {
		input = this.getClass().getResourceAsStream(EXECUTE_BODY_EXAMPLE_JSON);

		Execute_HttpPostBody executeBody = new ObjectMapper().readValue(input, Execute_HttpPostBody.class);

		/*
		 * version (constant value 1.0.0)
		 */
		Assert.assertEquals("1.0.0", executeBody.getVersion());

		/*
		 * processId (is not transmitted in request body. Thus, cannot be parsed
		 * and must be null in this case!)
		 */
		Assert.assertEquals(null, executeBody.getProcessId());

		/*
		 * inputs (constant value 1.0.0)
		 */
		List<ExecuteInput> inputs = executeBody.getInputs();

		/*
		 * first input
		 */
		ExecuteInput firstInput = inputs.get(0);
		Assert.assertEquals("2", firstInput.getId());
		Assert.assertEquals("http://fluggs.wupperverband.de/sos2/api/v1/timeseries/461?timespan=PT12H/2013-08-06Z",
				firstInput.getValue());

		/*
		 * second input
		 */
		ExecuteInput secondInput = inputs.get(1);
		Assert.assertEquals("3", secondInput.getId());
		Assert.assertEquals("http://fluggs.wupperverband.de/sos2/api/v1/timeseries/526?timespan=PT12H/2013-08-06Z",
				secondInput.getValue());

		/*
		 * third input
		 */
		ExecuteInput thirdInput = inputs.get(2);
		Assert.assertEquals("1", thirdInput.getId());
		Assert.assertEquals(
				"[0.03550405161598, -0.01860639146241, -0.01860639146241, -0.03550405161598, 385795.23669382796000, 5667086.67852447180000]",
				thirdInput.getValue());

		/*
		 * outputs
		 */
		List<ExecuteOutput> outputs = executeBody.getOutputs();

		/*
		 * first output
		 */
		ExecuteOutput firstOutput = outputs.get(0);
		Assert.assertEquals("1", firstOutput.getId());
		Assert.assertEquals("RasterImage Geotiff", firstOutput.getType());
	}

}
