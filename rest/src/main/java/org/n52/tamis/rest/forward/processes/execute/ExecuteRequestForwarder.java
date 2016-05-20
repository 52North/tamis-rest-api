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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to delegate an execute request to the WPS proxy instance.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ExecuteRequestForwarder extends AbstractRequestForwarder {

	private static final Logger logger = LoggerFactory.getLogger(ExecuteRequestForwarder.class);

	@Autowired
	SosRequestConstructor sosRequestConstructor;

	/**
	 * {@inheritDoc} <br/>
	 * <br/>
	 * Delegates an incoming execute request to the WPS proxy.
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
	 * @return a String value representing the URL ob the created job instance.
	 * @throws IOException
	 */
	@Override
	public String forwardRequestToWpsProxy(HttpServletRequest request, Object requestBody,
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

		sosRequestConstructor.constructSosGetObservationRequestsForInputs(executeBody);

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
        
        String bodyString = new ObjectMapper().writeValueAsString(executeBody);
		System.out.println(bodyString);

		if (syncExecute_parameter) {
			// synchronous
			RestTemplate synchronousExecuteTemplate = new RestTemplate();

			/*
			 * execute the POST request synchronously
			 * 
			 * the return value will be the location of the newly created
			 * resource (= the location header)
			 * 
			 */
//			createdJobUri_wpsProxy = synchronousExecuteTemplate.postForLocation(execute_url_wpsProxy, executeBody);			
			
	        HttpEntity requestEntity = new HttpEntity(executeBody, headers);
	        
			ResultDocument resultDocument = synchronousExecuteTemplate.postForObject(execute_url_wpsProxy, requestEntity, ResultDocument.class);
			String jobId = resultDocument.getResult().getJobId();
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
			builder.path(URL_Constants_TAMIS.SLASH_JOBS + "/" + jobId);
			
			String locationHeader = builder.build().encode().toUriString();
			
//			String locationHeader = request.getRequestURL().toString() + URL_Constants_TAMIS.SLASH_JOBS + "/" + jobId;
			return locationHeader;
		} else {

			/*
			 * TODO FIXME how to deal with asynchronous request?
			 * 
			 * Proceed similar to synchronous, since I just call the WPS proxy
			 * with different sync-execute parameter; or do I have to
			 * distinguish it in my own implementation?
			 */

			// AsyncRestTemplate asyncExecuteTemplate = new AsyncRestTemplate();
			//
			// /*
			// * execute the POST request asynchronously
			// *
			// * the return value will be the location of the newly created
			// * resource (= the location header)
			// *
			// */
			// HttpHeaders headers = new HttpHeaders();
			// HttpEntity<Execute_HttpPostBody> executeBodyEntity = new
			// HttpEntity<Execute_HttpPostBody>(executeBody);
			// ListenableFuture<URI> test =
			// asyncExecuteTemplate.postForLocation(execute_url_wpsProxy,
			// executeBodyEntity);

			/*
			 * TODO check if that implementation is correct or if
			 * AsyncRestTemplate must be used?
			 */
			RestTemplate asynchronousExecuteTemplate = new RestTemplate();
//
//			HttpEntity requestEntity = new HttpEntity(executeBody, headers);
//			
//			String bodyAsString = new ObjectMapper().writeValueAsString(executeBody);
//	        System.out.println(bodyAsString);
//			
////			ResponseEntity<URI> response = asynchronousExecuteTemplate.postForObject(execute_url_wpsProxy, requestEntity, ResponseEntity.class);
//			
//			ResponseEntity response = asynchronousExecuteTemplate.exchange(execute_url_wpsProxy, HttpMethod.POST, requestEntity, ResponseEntity.class);
//			
//			createdJobUri_wpsProxy = (URI) response.getHeaders().getLocation();
//			
//			System.out.println();
			createdJobUri_wpsProxy = asynchronousExecuteTemplate.postForLocation(execute_url_wpsProxy, executeBody);

		}

		// executeTemplate.exchange(execute_url_wpsProxy, HttpMethod.POST,
		// executeBody, null, null);
		// executeTemplate.execute(execute_url_wpsProxy, HttpMethod.POST, null,
		// null);

		/*
		 * from the result of the execution request against the WPS proxy,
		 * extract the location header and return it.
		 * 
		 * TODO the location header points to an URL specific for the WPS proxy!
		 * 
		 * What we need is the URL pointing to THIS applications resource.
		 * Hence, we must adapt the URL! --> Basically we have to extract the
		 * job ID and append it to the standard URL path of THIS application.
		 * 
		 * createdJobUrl_wpsProxy looks like "<baseUrl-wpsProxy>/processes/{processId}/jobs/{jobId}"
		 */
		String jobId = createdJobUri_wpsProxy.getPath().split(URL_Constants_WpsProxy.SLASH_JOBS + "/")[1];
		
		/*
		 * executeRequestUri should look like: "<base-url-tamis>/services/{serviceId}/processes/{processId}/jobs/{jobId}"
		 */
		
		String createdJobUrl = request.getRequestURL().toString();
		createdJobUrl = createdJobUrl + URL_Constants_TAMIS.SLASH_JOBS + "/" + jobId;

		return createdJobUrl;
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
