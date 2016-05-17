package org.n52.tamis.rest.forward.processes.jobs;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.core.urlconstants.URL_Constants_WpsProxy;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.AbstractRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Class to delegate a result request to the WPS proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ResultRequestForwarder extends AbstractRequestForwarder {

	private static Logger logger = LoggerFactory.getLogger(ResultRequestForwarder.class);

	/**
	 * {@inheritDoc} <br/>
	 * <br/>
	 * Delegates an incoming result request to the WPS proxy.
	 * 
	 * @param parameterValueStore
	 *            must contain the URL variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} to
	 *            identify the WPS instance and
	 *            {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} to
	 *            identify the process and
	 *            {@link URL_Constants_TAMIS#JOB_ID_VARIABLE_NAME} to identify
	 *            the job and
	 *            {@link URL_Constants_TAMIS#OUTPUT_ID_VARIABLE_NAME} to
	 *            identify the ouput
	 * @return
	 */
	@Override
	public Object forwardRequestToWpsProxy(HttpServletRequest request, Object requestBody,
			ParameterValueStore parameterValueStore) {
		initializeRequestSpecificParameters(parameterValueStore);

		String result_url_wpsProxy = createTargetURL_WpsProxy(request);

		RestTemplate resultTemplate = new RestTemplate();

		// TODO does that really work this way?

		return resultTemplate.getForEntity(result_url_wpsProxy, Object.class);
	}

	@Override
	protected String setUpTargetUrl_WithSameBaseUrl(String contextURL) {
		/*
		 * The difference between contextURL of TAMIS interface and the
		 * targetURL of the WPS proxy is a prefix + service number:
		 * 
		 * contexURL:
		 * "<baseURL>/constantServicePrefix/{service_id}/processes/{process_id}/jobs/{jobId}/outputs/{outputId}"
		 * 
		 * targetURL:
		 * "<baseURL>/processes/{process_id}/jobs/{jobId}/outputs/{outputId}"
		 */

		String targetURL = contextURL.split(URL_Constants_TAMIS.TAMIS_PREFIX)[0];
		targetURL = targetURL + URL_Constants_WpsProxy.SLASH_PROCESSES + "/" + this.getProcessId()
				+ URL_Constants_WpsProxy.SLASH_JOBS + "/" + this.getJobId() + URL_Constants_WpsProxy.SLASH_OUTPUTS + "/"
				+ this.getOutputId();

		return targetURL;
	}

	@Override
	protected String setUpTargetUrl_WithDifferentBaseUrl(String baseURL_WpsProxy) {
		 /* 
		  * target url: "<baseURL>/processes/{process_id}/jobs/{jobId}/outputs/{outputId}"
		 */

		return baseURL_WpsProxy + URL_Constants_WpsProxy.SLASH_PROCESSES + "/" + this.getProcessId()
				+ URL_Constants_WpsProxy.SLASH_JOBS + "/" + this.getJobId() + URL_Constants_WpsProxy.SLASH_OUTPUTS + "/"
				+ this.getOutputId();
	}

}
