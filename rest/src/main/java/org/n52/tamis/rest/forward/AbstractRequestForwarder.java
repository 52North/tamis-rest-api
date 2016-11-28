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
package org.n52.tamis.rest.forward;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class implementing {@link RequestForwarder} interface.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public abstract class AbstractRequestForwarder implements RequestForwarder {

	private static final String DEFAULT_CONFIG_FILE_WPS_PROXY = "/wpsProxy.properties";
	private static final String WPS_PROXY_CONFIG_PARAMETER_NAME_BASE_URL = "wpsProxy_baseUrl";
	private static final String WPS_PROXY_CONFIG_PARAMETER_NAME_FILTER_TIMESERIES_REQUESTS = "filterTimeSeriesRequests";

	protected static final String DEFAULT_SERVICE_ID = "1";

	protected static final String PROCESS_ID_PARAMETER_NAME = "processId";

	private String serviceId = DEFAULT_SERVICE_ID;
	private String processId;
	private String jobId;
	private String outputId;
	protected boolean filterTimeSeriesRequests = false;

	private static final Logger logger = LoggerFactory.getLogger(AbstractRequestForwarder.class);

	private String baseURL_WpsProxy = "";

	public AbstractRequestForwarder() {
		// load WPS proy config file to know the BASE URL

		this.baseURL_WpsProxy = parseBaseUrlFromStandardConfigFile();
	}

	private String parseBaseUrlFromStandardConfigFile() {

		logger.info("Try to extract parameter \"{}\" from configuration file named \"{}\".",
				WPS_PROXY_CONFIG_PARAMETER_NAME_BASE_URL, DEFAULT_CONFIG_FILE_WPS_PROXY);

		Properties props = new Properties();
		InputStream wpsProxyConfigInput = null;
		String extractedBaseUrl_WpsProxy = "";
		try {

			wpsProxyConfigInput = getClass().getResourceAsStream(DEFAULT_CONFIG_FILE_WPS_PROXY);

			props.load(wpsProxyConfigInput);

			extractedBaseUrl_WpsProxy = props.getProperty(WPS_PROXY_CONFIG_PARAMETER_NAME_BASE_URL);
			
			filterTimeSeriesRequests = Boolean.parseBoolean(props.getProperty(WPS_PROXY_CONFIG_PARAMETER_NAME_FILTER_TIMESERIES_REQUESTS));

			logger.info("Successfully extracted WPS proxy base URL from the configuration file. The value is \"{}\"",
					this.baseURL_WpsProxy);

		} catch (IOException e) {
			e.printStackTrace();
			logger.warn(
					"The base URL of the WPS proxy (property name \"{}\") could not be retrieved from the configuration file \"{}\".",
					WPS_PROXY_CONFIG_PARAMETER_NAME_BASE_URL, DEFAULT_CONFIG_FILE_WPS_PROXY);
		} finally {
			try {
				wpsProxyConfigInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return extractedBaseUrl_WpsProxy;
	}

	/**
	 * Creates the target URL of the appropriate request against the WPS proxy.
	 * 
	 * @param originalRequest
	 *            the original request. NOT the one that will be generated
	 *            through this method
	 * @return the URL that triggers the adequate operation of the WPS proxy.
	 */
	protected final String createTargetURL_WpsProxy(HttpServletRequest originalRequest) {

		String targetURL = "";

		if (baseURL_WpsProxy != null) {
			if (!baseURL_WpsProxy.equalsIgnoreCase("")) {
				logger.debug("WPS proxy base URL differs from this instance!");

				targetURL = setUpTargetUrl_WithDifferentBaseUrl(baseURL_WpsProxy);
			}
		} else {
			logger.warn(
					"Since no proper WPS proxy base URL could be retrieved from the configuration file, the base URL of this instance will be used as WPS proxy base URL!");

			String contextURL = originalRequest.getRequestURL().toString();

			targetURL = setUpTargetUrl_WithSameBaseUrl(contextURL);
		}

		logger.info("Following url was constructed to request extended WPS document: {}", targetURL);

		return targetURL;
	};

	/**
	 * Uses the base URL of Tamis REST interface to create the targetUrl against
	 * the WPS proxy
	 * 
	 * @param contextURL
	 *            the complete Tamis specific URL
	 * @return
	 */
	protected abstract String setUpTargetUrl_WithSameBaseUrl(String contextURL);

	/**
	 * Uses a different base URL (extracted from a configuration file) to create
	 * the targetUrl against the WPS proxy
	 * 
	 * @param baseURL_WpsProxy
	 *            base URL of WPS proxy, ectracted from a configuration file
	 * @return
	 */
	protected abstract String setUpTargetUrl_WithDifferentBaseUrl(String baseURL_WpsProxy);

	/**
	 * Extracts request specific parameters from the arguments-array for
	 * class-attribute instantiation
	 * 
	 * @param parameterValueStore
	 *            contains request specific URL variables/parameters in a map
	 */
	protected void initializeRequestSpecificParameters(ParameterValueStore parameterValueStore) {

		Map<String, String> parameterValuePairs = parameterValueStore.getParameterValuePairs();

		logger.info("Parse request specific parameters/variables! The number of parameters/variables is {}",
				parameterValuePairs.size());

		/*
		 * service id
		 */
		if (parameterValuePairs.containsKey(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME))
			this.setServiceId(parameterValuePairs.get(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME));

		/*
		 * service id
		 */
		if (parameterValuePairs.containsKey(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME))
			this.setServiceId(parameterValuePairs.get(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME));

		if (this.getServiceId().equalsIgnoreCase("")) {
			logger.error(
					"No URL variable named \"service_id\" was found in argument-array {}! Will use default service_id \"1\"",
					parameterValueStore);
			this.setServiceId("1");
		}

		/*
		 * process id
		 */
		if (parameterValuePairs.containsKey(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME))
			this.setProcessId(parameterValuePairs.get(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME));

		/*
		 * job id
		 */
		if (parameterValuePairs.containsKey(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME))
			this.setJobId(parameterValuePairs.get(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME));

		/*
		 * output id
		 */
		if (parameterValuePairs.containsKey(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME))
			this.setOutputId(parameterValuePairs.get(URL_Constants_TAMIS.OUTPUT_ID_VARIABLE_NAME));

		logger.info(
				"Following request specific parameters/variables have been initialized: \"service_id\" = {}, \"process_id\" = {}, \"job_id\" = {}, \"output_id\" = {}!",
				this.serviceId, this.processId, this.jobId, this.outputId);

	};

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getOutputId() {
		return outputId;
	}

	public void setOutputId(String outputId) {
		this.outputId = outputId;
	}

	public String getBaseURL_WpsProxy() {
		return baseURL_WpsProxy;
	}

}
