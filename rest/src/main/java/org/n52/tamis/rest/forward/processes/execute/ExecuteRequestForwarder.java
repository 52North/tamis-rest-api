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
package org.n52.tamis.rest.forward.processes.execute;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.javarepresentations.processes.execute.Execute_HttpPostBody;
import org.n52.tamis.core.javarepresentations.processes.jobs.result.ResultDocument;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.core.urlconstants.URL_Constants_WpsProxy;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.controller.processes.ExecuteProcessController;
import org.n52.tamis.rest.forward.AbstractRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * Class to delegate an execute request to the WPS proxy instance.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ExecuteRequestForwarder extends AbstractRequestForwarder {

	private static final Logger logger = LoggerFactory.getLogger(ExecuteRequestForwarder.class);

	/**
	 * {@inheritDoc} <br/>
	 * <br/>
	 * Delegates an incoming execute request to the WPS proxy.
	 * 
	 * Has two possible return values depending on the type of execution
	 * (synchronous or asynchronous)! See return description.
	 * 
	 * @param parameterValueStore
	 *            must contain the URL variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} to
	 *            identify the WPS instance and
	 *            {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} to
	 *            identify the process
	 * 
	 * @param requestBody
	 *            must be an instance of {@link Execute_HttpPostBody}
	 * @return either a String value representing the location header to the
	 *         created job instance (in case of asynchronous execution)
	 *         <b>OR</b> an instance of {@link ResultDocument} (in case of
	 *         synchronous execution).
	 * @throws IOException
	 */
	@Override
	public Object forwardRequestToWpsProxy(HttpServletRequest request, Object requestBody,
			ParameterValueStore parameterValueStore) throws IOException {
		initializeRequestSpecificParameters(parameterValueStore);

		Execute_HttpPostBody executeBody = null;

		/*
		 * requestBody must be an instance of Execute_HttpPostBody
		 */
		if (requestBody instanceof Execute_HttpPostBody)
			executeBody = (Execute_HttpPostBody) requestBody;
		else
			logger.error(
					"Request body was expected to be an instance of \"{}\", but was \"{}\". NullPointerException might occur.",
					Execute_HttpPostBody.class, requestBody.getClass());

		// add process Id, since it is not included in the received execute
		// body, but is needed
		executeBody.setProcessId(this.getProcessId());

		String execute_url_wpsProxy = createTargetURL_WpsProxy(request);

		/*
		 * To guarantee the existence of the parameter "sync-execute" in the
		 * request-object, the parameter has been added as an attribute to the
		 * request.
		 */
		boolean syncExecute_parameter = (boolean) request
				.getAttribute(ExecuteProcessController.SYNC_EXECUTE_PARAMETER_NAME);
		execute_url_wpsProxy = append_syncExecute_parameter(syncExecute_parameter, execute_url_wpsProxy);

		/*
		 * forward execute request to WPS proxy.
		 * 
		 * depending on the request parameter "sync-execute" the call should be
		 * realized asynchronously or synchronously.
		 */
		URI createdJobUri_wpsProxy = null;
		
		/**
		 * content headers!
		 */
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		if (syncExecute_parameter) {
			// synchronous
			RestTemplate synchronousExecuteTemplate = new RestTemplate();

			/*
			 * execute the POST request synchronously
			 * 
			 * the return value will be a result document of the newly created
			 * resource. Thus, we have to extract the jobId from it to manually
			 * build the location header)
			 * 
			 */		
			
	        HttpEntity<Execute_HttpPostBody> requestEntity = new HttpEntity<Execute_HttpPostBody>(executeBody, headers);
	        
			ResultDocument resultDocument = synchronousExecuteTemplate.postForObject(execute_url_wpsProxy, requestEntity, ResultDocument.class);

			/*
			 * the WPS is not conceptualized to offer a StatusRequest against a
			 * job that has been executed synchronously. Hence, any jobID-URL
			 * pointing to a synchronous job will fail (and result in a Bad
			 * Request error or syntax error)
			 * 
			 * Hence, we will simply return the ResultDocument!
			 */
			
			return resultDocument;
		} else {

			/*
			 * Proceed similar to synchronous, since I just call the WPS proxy
			 * with different sync-execute parameter;
			 * 
			 * In opposite to synchronous call, will receive and return the
			 * location header of the newly created job instance
			 */

			RestTemplate asynchronousExecuteTemplate = new RestTemplate();

			createdJobUri_wpsProxy = asynchronousExecuteTemplate.postForLocation(execute_url_wpsProxy, executeBody);

			/*
			 * from the result of the execution request against the WPS proxy,
			 * extract the location header and return it.
			 * 
			 * the location header points to an URL specific for the WPS proxy!
			 * 
			 * What we need is the URL pointing to THIS applications resource.
			 * Hence, we must adapt the URL! --> Basically we have to extract the
			 * job ID and append it to the standard URL path of THIS application.
			 * 
			 * createdJobUrl_wpsProxy looks like "<baseUrl-wpsProxy>/processes/{processId}/jobs/{jobId}"
			 */
			String jobId = createdJobUri_wpsProxy.getPath().split(URL_Constants_WpsProxy.SLASH_JOBS + "/")[1];
			
			/*
			 * target job URL should look like: "<base-url-tamis>/services/{serviceId}/processes/{processId}/jobs/{jobId}"
			 */
			
			String createdJobUrl = request.getRequestURL().toString();
			createdJobUrl = createdJobUrl + URL_Constants_TAMIS.SLASH_JOBS + "/" + jobId;

			return createdJobUrl;
		}
	}

	private String append_syncExecute_parameter(boolean sync_execute, String execute_url_wpsProxy) {
		/*
		 * manually add request parameter "sync-execute" to the execute_url
		 * against the wps proxy
		 * 
		 */
		execute_url_wpsProxy = execute_url_wpsProxy + "?" + ExecuteProcessController.SYNC_EXECUTE_PARAMETER_NAME + "="
				+ sync_execute;
		return execute_url_wpsProxy;
	}

	@Override
	protected String setUpTargetUrl_WithSameBaseUrl(String contextURL) {
		/*
		 * The difference between contextURL of TAMIS interface and the
		 * targetURL of the WPS proxy is a prefix + service number:
		 * 
		 * contexURL:
		 * "<baseURL>/constantServicePrefix/{service_id}/processes/{process_id}"
		 * 
		 * targetURL: "<baseURL>/processes/{process_id}"
		 */

		String targetURL = contextURL.split(URL_Constants_TAMIS.TAMIS_PREFIX)[0];
		targetURL = targetURL + URL_Constants_WpsProxy.SLASH_PROCESSES + "/" + this.getProcessId();

		return targetURL;
	}

	@Override
	protected String setUpTargetUrl_WithDifferentBaseUrl(String baseURL_WpsProxy) {
		// targetURL: "<baseURL>/processes/{process_id}"
		return baseURL_WpsProxy + URL_Constants_WpsProxy.PROCESSES + "/" + this.getProcessId();
	}

}
