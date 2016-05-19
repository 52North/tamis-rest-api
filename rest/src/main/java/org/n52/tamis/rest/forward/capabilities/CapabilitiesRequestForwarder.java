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
package org.n52.tamis.rest.forward.capabilities;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.javarepresentations.capabilities.Capabilities_Tamis;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.core.urlconstants.URL_Constants_WpsProxy;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.AbstractRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Delegates a capabilities request to the WPS proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class CapabilitiesRequestForwarder extends AbstractRequestForwarder {

	private static final Logger logger = LoggerFactory.getLogger(CapabilitiesRequestForwarder.class);

	/**
	 * {@inheritDoc} <br/>
	 * <br/>
	 * Delegates an incoming getCapabilities request to the WPS proxy, receives
	 * the extended capabilities document and creates an instance of
	 * {@link Capabilities_Tamis}
	 * 
	 * @param parameterValueStore
	 *            must contain the URL variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} to
	 *            identify the WPS instance
	 * @return an instance of {@link Capabilities_Tamis}
	 */
	public final Capabilities_Tamis forwardRequestToWpsProxy(HttpServletRequest request, Object requestBody,
			ParameterValueStore parameterValueStore) {
		// assure that the URL variable "serviceId" is existent
		initializeRequestSpecificParameters(parameterValueStore);

		String capabilities_url_wpsProxy = createTargetURL_WpsProxy(request);

		RestTemplate capabilitiesTemplate = new RestTemplate();

		// fetch extended capabilitiesDoc from WPS proxy and deserialize it into
		// shortened capabilitiesDoc
		Capabilities_Tamis capabilitiesDoc = capabilitiesTemplate.getForObject(capabilities_url_wpsProxy,
				Capabilities_Tamis.class);
		
		/*
		 * Override the URL of  capabilitiesDoc with the base URL of the tamis WPS proxy.
		 * 
		 * Retrieve the base URL from the HtteServletRequest object 
		 */
		String requestURL = request.getRequestURL().toString();
		String wpsBaseUrl_tamisProxy = requestURL.split(URL_Constants_TAMIS.TAMIS_PREFIX)[0];
		capabilitiesDoc.setUrl(wpsBaseUrl_tamisProxy);

		return capabilitiesDoc;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param parameterValueStore
	 *            must consist of exactly one parameter named "serviceId"
	 */
	@Override
	protected void initializeRequestSpecificParameters(ParameterValueStore parameterValueStore) {
		super.initializeRequestSpecificParameters(parameterValueStore);
	}

	@Override
	protected String setUpTargetUrl_WithSameBaseUrl(String contextURL) {
		/*
		 * The difference between contextURL of TAMIS interface and the
		 * targetURL of the WPS proxy is a prefix + service number:
		 * 
		 * contexURL: "<baseURL>/constantServicePrefix/{service_id}"
		 * 
		 * targetURL: "<baseURL>"
		 */

		return contextURL.split(URL_Constants_TAMIS.TAMIS_PREFIX)[0];
	}

	@Override
	protected String setUpTargetUrl_WithDifferentBaseUrl(String baseURL_WpsProxy) {
		// simply extend base URL with capabilities

		String targetUrl = baseURL_WpsProxy + URL_Constants_WpsProxy.CAPABILITIES;

		return targetUrl;
	}

}
