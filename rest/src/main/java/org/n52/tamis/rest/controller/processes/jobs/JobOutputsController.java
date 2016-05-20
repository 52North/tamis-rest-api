package org.n52.tamis.rest.controller.processes.jobs;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.core.javarepresentations.processes.jobs.result.ResultDocument;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.processes.jobs.JobOutputsRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST controller to handle "/outputs" request.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@RequestMapping(value = URL_Constants_TAMIS.OUTPUTS, method = RequestMethod.GET, produces = { "application/json" })
public class JobOutputsController extends AbstractRestController {

	private static final Logger logger = LoggerFactory.getLogger(JobOutputsController.class);
	
	@Autowired
	JobOutputsRequestForwarder jobOutputsRequestForwarder;

	@Autowired
	ParameterValueStore parameterValueStore;

	/**
	 * Returns the result document containing outputs of a certain job.
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
	public ResponseEntity<ResultDocument> getResultDocument(
			@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId,
			@PathVariable(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME) String jobId, HttpServletRequest request) {

		logger.info("Received get outputs (result document) request for service id \"{}\", process id \"{}\" and job id \"{}\"!",
				serviceId, processId, jobId);

		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME, serviceId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME, processId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME, jobId);

		ResultDocument resultDocument = jobOutputsRequestForwarder.forwardRequestToWpsProxy(request,
				null, parameterValueStore);

		return ResponseEntity.ok(resultDocument);
	}
	
}
