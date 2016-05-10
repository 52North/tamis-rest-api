package org.n52.tamis.rest.forward.jobs;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.javarepresentations.jobs.StatusDescription;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.core.urlconstants.URL_Constants_WpsProxy;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.AbstractRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Class to delegate a status description request to the WPS proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class StatusRequestForwarder extends AbstractRequestForwarder {

	private static Logger logger = LoggerFactory.getLogger(StatusRequestForwarder.class);

	/**
	 * {@inheritDoc} <br/>
	 * <br/>
	 * Delegates an incoming status description request to the WPS proxyand
	 * creates an instance of {@link StatusDescription}
	 * 
	 * @param parameterValueStore
	 *            must contain the URL variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} to
	 *            identify the WPS instance and
	 *            {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} to
	 *            identify the process and
	 *            {@link URL_Constants_TAMIS#JOB_ID_VARIABLE_NAME} to identify
	 *            the job
	 * @return an instance of {@link StatusDescription}
	 */
	@Override
	public StatusDescription forwardRequestToWpsProxy(HttpServletRequest request,
			ParameterValueStore parameterValueStore) {
		initializeRequestSpecificParameters(parameterValueStore);

		String statusDescription_url_wpsProxy = createTargetURL_WpsProxy(request);

		RestTemplate statusDescriptionTemplate = new RestTemplate();

		// fetch the status description from WPS proxy and
		// deserialize it into TAMIS status decsription
		// TODO does that really work this way?
		StatusDescription statusDescriptionDoc = statusDescriptionTemplate.getForObject(statusDescription_url_wpsProxy,
				StatusDescription.class);

		String outputs = statusDescriptionDoc.getOutputs();

		if (outputs != null) {
			String outputURL_wpsProxy = outputs;
			/*
			 * we want to insert tamis prefix and service id into the URL!
			 * 
			 * outputURL_wpsProxy =
			 * <baseURL>/processes/<process_id>/jobs/<job_id> outputURL_tamis =
			 * <baseURL>tamis_prefix/<service_id>/processes/<process_id>/jobs/<
			 * job_id>
			 * 
			 * Hereby, it is supposed that both instances of the WPS (this and
			 * the proxy) have the same <baseUrl>
			 */
			String baseURL = outputURL_wpsProxy.split(URL_Constants_WpsProxy.SLASH_PROCESSES)[0];
			String outputURL_tamis = baseURL + URL_Constants_TAMIS.API_V1_BASE_PREFIX + "/"
					+ URL_Constants_TAMIS.TAMIS_PREFIX + "/" + this.getServiceId() + URL_Constants_TAMIS.SLASH_PROCESSES
					+ "/" + this.getProcessId() + URL_Constants_TAMIS.SLASH_JOBS + "/" + this.getJobId();
			statusDescriptionDoc.setOutputs(outputURL_tamis);
		}

		return statusDescriptionDoc;
	}

	@Override
	protected String setUpTargetUrl_WithSameBaseUrl(String contextURL) {
		/*
		 * The difference between contextURL of TAMIS interface and the
		 * targetURL of the WPS proxy is a prefix + service number:
		 * 
		 * contexURL:
		 * "<baseURL>/constantServicePrefix/{service_id}/processes/{process_id}/jobs/{job_id}"
		 * 
		 * targetURL: "<baseURL>/processes/{process_id}/jobs/{job_id}"
		 */

		String targetURL = contextURL.split(URL_Constants_TAMIS.TAMIS_PREFIX)[0];
		targetURL = targetURL + URL_Constants_WpsProxy.SLASH_PROCESSES + "/" + this.getProcessId()
				+ URL_Constants_WpsProxy.SLASH_JOBS + "/" + this.getJobId();

		return targetURL;
	}

	@Override
	protected String setUpTargetUrl_WithDifferentBaseUrl(String baseURL_WpsProxy) {
		// targetURL: "<baseURL>/processes/{process_id}/jobs/{job_id}"
		return baseURL_WpsProxy + URL_Constants_WpsProxy.PROCESSES + "/" + this.getProcessId()
				+ URL_Constants_WpsProxy.SLASH_JOBS + "/" + this.getJobId();
	}

	@Override
	protected void initializeRequestSpecificParameters(ParameterValueStore parameterValueStore) {
		super.initializeRequestSpecificParameters(parameterValueStore);
	}

}
