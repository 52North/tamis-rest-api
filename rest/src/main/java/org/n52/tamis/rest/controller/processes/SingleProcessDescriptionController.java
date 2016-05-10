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

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.javarepresentations.processes.ProcessDescription_singleProcess;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.processes.SingleProcessDescriptionRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST COntroller for single processes descriptions. (Only handles GET requests
 * to that URL).
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@RequestMapping(value = URL_Constants_TAMIS.PROCESS_DESCRIPTION_SINGLE, method = RequestMethod.GET, produces = {
		"application/json" })
public class SingleProcessDescriptionController extends AbstractRestController {

	private static final Logger logger = LoggerFactory.getLogger(SingleProcessDescriptionController.class);

	@Autowired
	SingleProcessDescriptionRequestForwarder sProcessDescrRequestForwarder;

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
	@ResponseBody
	public ProcessDescription_singleProcess getSingleProcessDescription(
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId, HttpServletRequest request) {

		logger.info("Received single process description request for service id \"{}\" and process id \"{}\"!",
				serviceId, processId);

		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME, serviceId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME, processId);

		ProcessDescription_singleProcess singleProcessDescription = sProcessDescrRequestForwarder
				.forwardRequestToWpsProxy(request, null, parameterValueStore);

		return singleProcessDescription;
	}

}
