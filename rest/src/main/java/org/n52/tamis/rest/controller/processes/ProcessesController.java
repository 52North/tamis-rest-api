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

import org.n52.tamis.core.javarepresentations.processes.Processes_Tamis;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.forward.processes.ProcessesRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = URL_Constants_TAMIS.PROCESSES, produces = { "application/json" })
public class ProcessesController extends AbstractRestController {

	private static final Logger logger = LoggerFactory.getLogger(ProcessesController.class);

	@Autowired
	ProcessesRequestForwarder processesRequestForwarder;

	@RequestMapping("")
	@ResponseBody
	public Processes_Tamis getProcessesOverview(
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId, HttpServletRequest request) {

		logger.info("Received processes request (overview of available processes) for service id \"{}\"!", serviceId);

		Processes_Tamis processesOverview = processesRequestForwarder.forwardRequestToWpsProxy(request, serviceId);

		return processesOverview;
	}

}
