package org.n52.tamis.rest.controller.jobs;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.javarepresentations.jobs.StatusDescription;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.controller.processes.SingleProcessDescriptionController;
import org.n52.tamis.rest.forward.jobs.StatusRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST Controller for status requests. (Only handles GET requests to that URL).
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@RequestMapping(value = URL_Constants_TAMIS.STATUS, method = RequestMethod.GET, produces = { "application/json" })
public class StatusRequestController extends AbstractRestController {

	private static final Logger logger = LoggerFactory.getLogger(SingleProcessDescriptionController.class);

	@Autowired
	StatusRequestForwarder statusRequestForwarder;

	@Autowired
	ParameterValueStore parameterValueStore;

	/**
	 * Returns the status description.
	 * 
	 * @param serviceId
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#SERVICE_ID_VARIABLE_NAME} specifies
	 *            the id of the service.
	 * @param processId
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#PROCESS_ID_VARIABLE_NAME} specifies
	 *            the id of the process.
	 * @param jobId
	 *            inside the URL the variable
	 *            {@link URL_Constants_TAMIS#JOB_ID_VARIABLE_NAME} specifies the
	 *            id of the job.
	 * @param request
	 * @return the status description
	 */
	@RequestMapping("")
	@ResponseBody
	public StatusDescription getStatusDescription(
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId,
			@PathVariable(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME) String jobId, HttpServletRequest request) {

		logger.info("Received status description request for service id \"{}\", process id \"{}\" and job id \"{}\"!",
				serviceId, processId, jobId);

		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME, serviceId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME, processId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME, jobId);

		StatusDescription singleProcessDescription = statusRequestForwarder.forwardRequestToWpsProxy(request,
				null, parameterValueStore);

		return singleProcessDescription;
	}

}
