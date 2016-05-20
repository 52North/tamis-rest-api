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
package org.n52.tamis.rest.controller.processes;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.tamis.core.javarepresentations.processes.execute.Execute_HttpPostBody;
import org.n52.tamis.core.javarepresentations.processes.jobs.result.ResultDocument;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.processes.execute.ExecuteRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST Controller for process execute requests. (Only handles POST requests to
 * that URL).
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@RequestMapping(value = URL_Constants_TAMIS.EXECUTE, method = RequestMethod.POST, produces = { "application/json" })
public class ExecuteProcessController extends AbstractRestController {

	private static final Logger logger = LoggerFactory.getLogger(SingleProcessDescriptionController.class);

	public static final String SYNC_EXECUTE_PARAMETER_NAME = "sync-execute";
	// private static final String SYNC_EXECUTE_PARAMETER_TRUE = "true";
	// private static final String SYNC_EXECUTE_PARAMETER_FALSE = "false";

	@Autowired
	ExecuteRequestForwarder executeRequestForwarder;

	@Autowired
	ParameterValueStore parameterValueStore;


	/**
	 * Returns the shortened single process description.
	 * 
	 * @param serviceID
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} specifies
	 *            the id of the service. * @param request
	 * @param processId
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} specifies
	 *            the id of the process.
	 * @param request
	 * @return the shortened single process description
	 */
	@RequestMapping("")
	public ResponseEntity executeProcess(@RequestBody Execute_HttpPostBody requestBody,
			@RequestParam(value = SYNC_EXECUTE_PARAMETER_NAME, required = false, defaultValue = "false") boolean sync_execute,
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		logger.info(
				"Received execute process request for service id \"{}\" and process id \"{}\"! URL request parameter \"{}\" is of value \"{}\".",
				serviceId, processId, SYNC_EXECUTE_PARAMETER_NAME, sync_execute);

		/*
		 * in the following we add an attribute to the request which is used
		 * later by ExecuteReqeustForwarder.java to create proper URL of WPS
		 * proxy.
		 * 
		 * This is necessary, since the URL parameter "sync-execute" is
		 * optional, and thus, might not exist. Hence, we add the attribute
		 * manually, which is afterwards guaranteed to be present.
		 */
		if (sync_execute) {
			request.setAttribute(SYNC_EXECUTE_PARAMETER_NAME, true);
		} else {
			request.setAttribute(SYNC_EXECUTE_PARAMETER_NAME, false);
		}
		/*
		 * else: syncExecute is set to "false" per default.
		 */

		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME, serviceId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME, processId);

		/*
		 * execute response may either be a String (= the location header of an
		 * asynchronously executed job) or an instance of ResultDocument (in
		 * case of synchronous job execution)
		 * 
		 * Hence, we have to consider both cases!
		 */
		Object executeResponse = executeRequestForwarder.forwardRequestToWpsProxy(request, requestBody, parameterValueStore);

		if(executeResponse instanceof String){
			/* 
			 * response of asynchronous job execution. 
			 * 
			 * Thus set location header
			 */
			String locationHeaer = (String) executeResponse;
			response.setHeader("Location", locationHeaer);

			return new ResponseEntity(HttpStatus.CREATED);
		}
		else if (executeResponse instanceof ResultDocument){
			/*
			 * response of synchronous job execution.
			 * 
			 * simply return the resultDocument.
			 */
			ResultDocument resultDoc = (ResultDocument) executeResponse;
			
			return ResponseEntity.ok(resultDoc);
		}
		else{
			logger.error("The response of the execute request is of unexpected type. Either String (as location header from synchonous job execution) or ResultDocument (for asynchronous job execution) were expected. Response is of type {}!", executeResponse.getClass());
			
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		

	}

}
