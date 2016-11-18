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
package org.n52.tamis.rest.controller.processes.jobs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.javarepresentations.processes.jobs.result.ResultDocument;
import org.n52.tamis.core.javarepresentations.processes.jobs.result.ResultOutput;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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

	private static final String JSON_EXTENSION_VALUE = ".json";

	@Autowired
	ParameterValueStore parameterValueStore;

	// /**
	// * Returns the result.
	// *
	// * @param serviceId
	// * inside the URL the variable
	// * {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} specifies
	// * the id of the service.
	// * @param processId
	// * inside the URL the variable
	// * {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} specifies
	// * the id of the process.
	// * @param jobId
	// * inside the URL the variable
	// * {@link URL_Constants_TAMIS#JOB_ID_VARIABLE_NAME} specifies the
	// * id of the job.
	// * @param outputId
	// * inside the URL the variable
	// * {@link URL_Constants_TAMIS#OUTPUT_ID_VARIABLE_NAME} specifies
	// * the id of the output.
	// * @param request
	// * @return the result
	// */
	// @RequestMapping(value = URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME +
	// ":.+.json")
	// @ResponseBody
	// public Object getResult_DotJsonUrlExtension(
	// @PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String
	// serviceId,
	// @PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String
	// processId,
	// @PathVariable(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME) String jobId,
	// @PathVariable(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME) String
	// outputId, HttpServletRequest request) {
	//
	// logger.info(
	// "Received get result request with \".json\" URL extension for service id
	// \"{}\", process id \"{}\", job id \"{}\" and output id \"{}\"!",
	// serviceId, processId, jobId, outputId);
	//
	// initializeParameterValueStore(serviceId, processId, jobId, outputId);
	//
	// ResultDocument resultDocument = fetchResultDocumentForJob(jobId,
	// request);
	//
	// return getOutput(outputId, resultDocument);
	// }

	private void initializeParameterValueStore(String serviceId, String processId, String jobId, String outputId) {
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME, serviceId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME, processId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME, jobId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME, outputId);
	}

	/**
	 * Returns a single output. Hereby it will inspect three methods to set
	 * responseFormat to JSON<br/>
	 * <br/>
	 * <br/>
	 * 1. ".json" extension at URL <br/>
	 * 2. request parameter "outputFormat" is set to "json" <br/>
	 * 3. Accept header is set to "application/json"
	 * 
	 * @param outputFormat
	 *            may be omitted or set to value "json".
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
	 * @return
	 */
	@RequestMapping("")
	public ResponseEntity<ResultOutput> getResult(@RequestParam(value = OUTPUT_FORMAT_PARAMETER_NAME, required = false) String outputFormat,
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId,
			@PathVariable(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME) String jobId,
			@PathVariable(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME) String outputId, HttpServletRequest request) {

		String acceptHeader = request.getHeader("Accept");

		/*
		 * Since the outputId may have dots within it (e.g. "my.output"), the
		 * ".json" extension might be interpreted as part of the outputId
		 * 
		 * inspect outputID for .json extension
		 * 
		 */
		if (outputId.contains(JSON_EXTENSION_VALUE)) {
			/*
			 * remove .json extnesion from outputId
			 */

			outputId = outputId.split(JSON_EXTENSION_VALUE)[0];

			logger.info(
					"Received get result request with \".json\" URL extension for service id\"{}\", process id \"{}\", job id \"{}\" and output id \"{}\"!",
					serviceId, processId, jobId, outputId);
		}

		/*
		 * inspect accept header
		 */

		else if (acceptHeader.equalsIgnoreCase(MediaType.APPLICATION_JSON.toString())) {
			logger.info(
					"Received get result request with expected \"application/json\" as accept-header for service id \"{}\", process id \"{}\", job id \"{}\" and output id \"{}\"!",
					serviceId, processId, jobId, outputId);
		}

		/*
		 * inspect outputFormst query parameter
		 */

		else if (outputFormat != null) {
			logger.info(
					"Received get result request with output format as request parameter for service id \"{}\", process id \"{}\", job id \"{}\" and output id \"{}\"!",
					serviceId, processId, jobId, outputId);

			if (!outputFormat.equalsIgnoreCase(OUTPUT_FORMAT_PARAMETER_VALUE)) {
				logger.error("Value of request parameter \"{}\" must be " + OUTPUT_FORMAT_PARAMETER_VALUE
						+ "! It was \"{}\"", OUTPUT_FORMAT_PARAMETER_NAME, outputFormat);
			}
		}

		else {
			/*
			 * JSON output format was not properly specified by client
			 */
			logger.error(
					"Client request did not specify output format, neither by \".json\" file extension, nor through \"Accept\" header, not via query parameter \"outputFormat=json\"!");
		}

		initializeParameterValueStore(serviceId, processId, jobId, outputId);

		ResultDocument resultDocument = fetchResultDocumentForJob(jobId, request);

		return getOutput(outputId, resultDocument);
	}

	private ResponseEntity<ResultOutput> getOutput(String outputId, ResultDocument resultDocument) {
		/*
		 * now extract the requested output from the complete document
		 */

		List<ResultOutput> outputs = resultDocument.getResult().getOutputs();

		for (ResultOutput resultOutput : outputs) {
			if (resultOutput.getId().equalsIgnoreCase(outputId))
				return ResponseEntity.ok(resultOutput);
		}

		/**
		 * should this code be reached then the requested output was not found.
		 */

		logger.error("Could not find output with id \"{}\" in result document \"{}\"", outputId, resultDocument);

		return new ResponseEntity<ResultOutput>(HttpStatus.NOT_FOUND);
	}

	private ResultDocument fetchResultDocumentForJob(String jobId, HttpServletRequest request) {
		/*
		 * Fetches the complete resultDocument (by sending a GET outputs request
		 * against the specified jobId) and extracts the requested output with
		 * the specified id.
		 */
		/*
		 * request URL looks like =
		 * <tamis-rest-baseURL>/prefixes/services/{serviceID}/processes/{
		 * processID}/jobs/{jobId}/outputs/{outputId}
		 * 
		 * outputs request URL looks like (trailing outputId must be cut off):
		 * request URL looks like =
		 * <tamis-rest-baseURL>/prefixes/services/{serviceID}/processes/{
		 * processID}/jobs/{jobId}/outputs
		 */

		logger.info("Trying to fetch the complete result document from job with jobId=\"{}\"", jobId);

		String slashOutputId = "/"
				+ parameterValueStore.getParameterValuePairs().get(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME);
		String getOutputsUrl = request.getRequestURL().toString().split(slashOutputId)[0];

		RestTemplate getOutputs = new RestTemplate();
		ResultDocument resultDocument = getOutputs.getForObject(getOutputsUrl, ResultDocument.class);

		return resultDocument;
	}

}
