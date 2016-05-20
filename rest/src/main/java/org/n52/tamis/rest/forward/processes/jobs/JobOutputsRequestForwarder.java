package org.n52.tamis.rest.forward.processes.jobs;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.javarepresentations.processes.jobs.result.ResultDocument;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.core.urlconstants.URL_Constants_WpsProxy;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.AbstractRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class JobOutputsRequestForwarder extends AbstractRequestForwarder {

	private static Logger logger = LoggerFactory.getLogger(JobOutputsRequestForwarder.class);

	/**
	 * {@inheritDoc} <br/>
	 * <br/>
	 * Delegates an incoming outputs (result document) request to the WPS proxy
	 * and creates an instance of {@link ResultDocument}
	 * 
	 * @param parameterValueStore
	 *            must contain the URL variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} to
	 *            identify the WPS instance and
	 *            {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} to
	 *            identify the process and
	 *            {@link URL_Constants_TAMIS#JOB_ID_VARIABLE_NAME} to identify
	 *            the job
	 * @return an instance of {@link ResultDocument}
	 */
	@Override
	public <T> ResultDocument forwardRequestToWpsProxy(HttpServletRequest request, T requestBody,
			ParameterValueStore parameterValueStore) {
		initializeRequestSpecificParameters(parameterValueStore);

		String outputs_url_wpsProxy = createTargetURL_WpsProxy(request);

		RestTemplate outputsTemplate = new RestTemplate();

		// fetch the result document from WPS proxy and
		// deserialize it into TAMIS ResultDocument
		ResultDocument resultDocument = outputsTemplate.getForObject(outputs_url_wpsProxy, ResultDocument.class);

		return resultDocument;
	}

	@Override
	protected String setUpTargetUrl_WithSameBaseUrl(String contextURL) {
		/*
		 * The difference between contextURL of TAMIS interface and the
		 * targetURL of the WPS proxy is a prefix + service number:
		 * 
		 * contexURL:
		 * "<baseURL>/constantServicePrefix/{service_id}/processes/{process_id}/jobs/{job_id}/outputs"
		 * 
		 * targetURL: "<baseURL>/processes/{process_id}/jobs/{job_id}/outputs"
		 */

		String targetURL = contextURL.split(URL_Constants_TAMIS.TAMIS_PREFIX)[0];
		targetURL = targetURL + URL_Constants_WpsProxy.SLASH_PROCESSES + "/" + this.getProcessId()
				+ URL_Constants_WpsProxy.SLASH_JOBS + "/" + this.getJobId() + URL_Constants_WpsProxy.SLASH_OUTPUTS;

		return targetURL;
	}

	@Override
	protected String setUpTargetUrl_WithDifferentBaseUrl(String baseURL_WpsProxy) {
		// targetURL: "<baseURL>/processes/{process_id}/jobs/{job_id}/outputs"
		return baseURL_WpsProxy + URL_Constants_WpsProxy.PROCESSES + "/" + this.getProcessId()
				+ URL_Constants_WpsProxy.SLASH_JOBS + "/" + this.getJobId() + URL_Constants_WpsProxy.SLASH_OUTPUTS;
	}

}
