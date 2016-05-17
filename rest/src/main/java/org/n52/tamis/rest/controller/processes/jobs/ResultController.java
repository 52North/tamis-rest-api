package org.n52.tamis.rest.controller.processes.jobs;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.processes.jobs.ResultRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST Controller for result requests. (Only handles GET requests to that URL).
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@RequestMapping(value = URL_Constants_TAMIS.OUTPUT, method = RequestMethod.GET, produces = { "application/json" })
public class ResultController extends AbstractRestController {

	private static final Logger logger = LoggerFactory.getLogger(ResultController.class);
	private static final String OUTPUT_FORMAT_PARAMETER_NAME = "outputformat";
	private static final String OUTPUT_FORMAT_PARAMETER_VALUE = "json";

	@Autowired
	ResultRequestForwarder resultRequestForwarder;

	@Autowired
	ParameterValueStore parameterValueStore;

	/**
	 * Returns the result.
	 * 
	 * @param serviceId
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} specifies
	 *            the id of the service.
	 * @param processId
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} specifies
	 *            the id of the process.
	 * @param jobId
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#JOB_ID_VARIABLE_NAME} specifies the
	 *            id of the job.
	 * @param outputId
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#OUTPUT_ID_VARIABLE_NAME} specifies
	 *            the id of the output.
	 * @param request
	 * @return the result
	 */
	@RequestMapping(value = ".json")
	@ResponseBody
	public Object getResult_DotJsonUrlExtension(
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId,
			@PathVariable(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME) String jobId,
			@PathVariable(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME) String outputId, HttpServletRequest request) {

		logger.info(
				"Received get result request with \".json\" URL extension for service id \"{}\", process id \"{}\", job id \"{}\" and output id \"{}\"!",
				serviceId, processId, jobId, outputId);

		initializeParameterValueStore(serviceId, processId, jobId, outputId);

		return resultRequestForwarder.forwardRequestToWpsProxy(request, null, parameterValueStore);
	}

	private void initializeParameterValueStore(String serviceId, String processId, String jobId, String outputId) {
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME, serviceId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME, processId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME, jobId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME, outputId);
	}

	@RequestMapping("")
	public Object getResult_acceptHeaderOrRequestParam(@RequestParam(OUTPUT_FORMAT_PARAMETER_NAME) String outputFormat,
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId,
			@PathVariable(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME) String jobId,
			@PathVariable(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME) String outputId, HttpServletRequest request) {

		String acceptHeader = request.getHeader("Accept");

		if (acceptHeader != null) {
			logger.info(
					"Received get result request with expected \"application/json\" as accept-header for service id \"{}\", process id \"{}\", job id \"{}\" and output id \"{}\"!",
					serviceId, processId, jobId, outputId);

			if (!acceptHeader.equals(MediaType.APPLICATION_JSON)) {
				logger.error("No \"{}\" accept Header present!", MediaType.APPLICATION_JSON);
			}
		}

		if (outputFormat != null) {
			logger.info(
					"Received get result request with output format as request parameter for service id \"{}\", process id \"{}\", job id \"{}\" and output id \"{}\"!",
					serviceId, processId, jobId, outputId);

			if (!outputFormat.equalsIgnoreCase(OUTPUT_FORMAT_PARAMETER_VALUE)) {
				logger.error("Value of request parameter \"{}\" must be " + OUTPUT_FORMAT_PARAMETER_VALUE
						+ "! It was \"{}\"", OUTPUT_FORMAT_PARAMETER_NAME, outputFormat);
			}
		}

		initializeParameterValueStore(serviceId, processId, jobId, outputId);

		return resultRequestForwarder.forwardRequestToWpsProxy(request, null, parameterValueStore);
	}

}
