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
package org.n52.tamis.rest.forward.processes.jobs;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.core.urlconstants.URL_Constants_WpsProxy;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.AbstractRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * class to delegate a WPS delete request to the WPS proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class DeleteRequestForwarder extends AbstractRequestForwarder {

	private static Logger logger = LoggerFactory.getLogger(DeleteRequestForwarder.class);

	/**
	 * {@inheritDoc} <br/>
	 * <br/>
	 * Delegates an incoming delete request to the WPS proxy.
	 * 
	 * @param parameterValueStore
	 *            must contain the URL variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} to
	 *            identify the WPS instance and
	 *            {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} to
	 *            identify the process and
	 *            {@link URL_Constants_TAMIS#JOB_ID_VARIABLE_NAME} to identify
	 *            the job and
	 * @return
	 */
	@Override
	public <T> ResponseEntity<?> forwardRequestToWpsProxy(HttpServletRequest request, T requestBody,
			ParameterValueStore parameterValueStore) {
		
		logger.info("Forward get delete job request to WPS REST proxy.");		
		
		initializeRequestSpecificParameters(parameterValueStore);

		String delete_url_wpsProxy = createTargetURL_WpsProxy(request);

		RestTemplate deleteTemplate = new RestTemplate();

		// TODO does that really work this way?
		// resultTemplate.delete(delete_url_wpsProxy);
		deleteTemplate.delete(delete_url_wpsProxy);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Override
	protected String setUpTargetUrl_WithSameBaseUrl(String contextURL) {
		/*
		 * The difference between contextURL of TAMIS interface and the
		 * targetURL of the WPS proxy is a prefix + service number:
		 * 
		 * contexURL:
		 * "<baseURL>/constantServicePrefix/{service_id}/processes/{process_id}/jobs/{jobId}"
		 * 
		 * targetURL: "<baseURL>/processes/{process_id}/jobs/{jobId}"
		 */

		String targetURL = contextURL.split(URL_Constants_TAMIS.TAMIS_PREFIX)[0];
		targetURL = targetURL + URL_Constants_WpsProxy.SLASH_PROCESSES + "/" + this.getProcessId()
				+ URL_Constants_WpsProxy.SLASH_JOBS + "/" + this.getJobId();

		return targetURL;
	}

	@Override
	protected String setUpTargetUrl_WithDifferentBaseUrl(String baseURL_WpsProxy) {
		// targetURL: "<baseURL>/processes/{process_id}/jobs/{jobId}"
		return baseURL_WpsProxy + URL_Constants_WpsProxy.SLASH_PROCESSES + "/" + this.getProcessId()
		+ URL_Constants_WpsProxy.SLASH_JOBS + "/" + this.getJobId();
	}

}
