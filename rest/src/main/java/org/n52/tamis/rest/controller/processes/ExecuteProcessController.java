package org.n52.tamis.rest.controller.processes;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.tamis.core.javarepresentations.processes.execute.Execute_HttpPostBody;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.processes.execute.ExecuteRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST Controller for process execute requests. (Only handles POST requests to
 * that URL).
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@RequestMapping(value = URL_Constants_TAMIS.EXECUTE, method = RequestMethod.POST, produces = { "application/json" })
public class ExecuteProcessController extends AbstractRestController {

	private static final Logger logger = LoggerFactory.getLogger(SingleProcessDescriptionController.class);

	public static final String SYNC_EXECUTE_PARAMETER_NAME = "sync-execute";
	// private static final String SYNC_EXECUTE_PARAMETER_TRUE = "true";
	// private static final String SYNC_EXECUTE_PARAMETER_FALSE = "false";

	/*
	 * default value is false!
	 */
	private boolean syncExecute = false;

	@Autowired
	ExecuteRequestForwarder executeRequestForwarder;

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
	public ResponseEntity executeProcess(@RequestBody Execute_HttpPostBody requestBody,
			@RequestParam(value = SYNC_EXECUTE_PARAMETER_NAME, required = false, defaultValue = "false") boolean sync_execute,
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		logger.info(
				"Received execute process request for service id \"{}\" and process id \"{}\"! URL request parameter \"{}\" is of value \"{}\".",
				serviceId, processId, SYNC_EXECUTE_PARAMETER_NAME, sync_execute);

		/*
		 * in the following we add an attribute to the request which is used
		 * later by ExecuteReqeustForwarder.java to create proper URL of WPS
		 * proxy.
		 * 
		 * This is necessary, since the URL parameter "sync-execute" is
		 * optional, and thus, might not exist. Hence, we add the attribute
		 * manually, which is afterwards guaranteed to be present.
		 */
		if (sync_execute) {
			this.syncExecute = sync_execute;
			request.setAttribute(SYNC_EXECUTE_PARAMETER_NAME, true);
		} else {
			request.setAttribute(SYNC_EXECUTE_PARAMETER_NAME, false);
		}
		/*
		 * else: syncExecute is set to "false" per default.
		 */

		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME, serviceId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME, processId);

		String jobUrl = executeRequestForwarder.forwardRequestToWpsProxy(request, requestBody, parameterValueStore);

		response.setHeader("Location", jobUrl);

		return new ResponseEntity(HttpStatus.CREATED);

	}

}
