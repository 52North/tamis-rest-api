package org.n52.tamis.rest.controller.processes.jobs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.n52.tamis.rest.controller.AbstractRestController;
import org.n52.tamis.rest.controller.ParameterValueStore;
import org.n52.tamis.rest.forward.processes.jobs.DeleteRequestForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * REST Controller for delete requests. (Only handles GET requests to that URL).
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@RequestMapping(value = URL_Constants_TAMIS.DELETE, method = RequestMethod.DELETE)
public class DeleteRequestController extends AbstractRestController {
	private static final Logger logger = LoggerFactory.getLogger(DeleteRequestController.class);

	@Autowired
	DeleteRequestForwarder deleteRequestForwarder;

	@Autowired
	ParameterValueStore parameterValueStore;

	@RequestMapping("")
	public ResponseEntity delete(@PathVariable(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME) String serviceId,
			@PathVariable(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME) String processId,
			@PathVariable(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME) String jobId, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("Received delete request for service id \"{}\", process id \"{}\" and job id \"{}\" !", serviceId,
				processId, jobId);

		initializeParameterValueStore(serviceId, processId, jobId);

		// prepare response entity with HTTP status 404, not found
		ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);

		try {
			logger.info("Trying to delete resource at \"{}\"", request.getRequestURL());
			responseEntity = deleteRequestForwarder.forwardRequestToWpsProxy(request, null, parameterValueStore);

		} catch (Exception e) {
			logger.info("DELETE request for resouce at \"{}\" failed.", request.getRequestURL());
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return responseEntity;

	}

	private void initializeParameterValueStore(String serviceId, String processId, String jobId) {
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.SERVICE_ID_VARIABLE_NAME, serviceId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.PROCESS_ID_VARIABLE_NAME, processId);
		parameterValueStore.addParameterValuePair(URL_Constants_TAMIS.JOB_ID_VARIABLE_NAME, jobId);
	}
}
