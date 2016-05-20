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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteInput;
import org.n52.tamis.core.javarepresentations.processes.execute.Execute_HttpPostBody;
import org.n52.tamis.core.javarepresentations.processes.execute.SosTimeseriesInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Helper class that inspects the inputs of instances of
 * {@link Execute_HttpPostBody}. If the value of an input contains "http", then
 * it will be treated as OGC WPS "Reference" input with a REST URL. In this
 * case, the REST URL must be converted into a SOS KVP Get Request.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class SosRequestConstructor {

	private static Logger logger = LoggerFactory.getLogger(SosRequestConstructor.class);

	/**
	 * Inspects each input of the instance of {@link Execute_HttpPostBody}. If
	 * the value of an input contains "http", then it will be treated as OGC WPS
	 * "Reference" input resembling a REST URL. In this case, the REST URL must
	 * be converted into a SOS KVP Get Request.
	 * 
	 * Hence, the method calls the timeseries-api to acquire the necessary
	 * information to construct the complete SOS request.
	 * 
	 * @param executeBody
	 * @throws IOException
	 */
	public void constructSosGetObservationRequestsForInputs(Execute_HttpPostBody executeBody) throws IOException {

		logger.info(
				"Inspecting the instance of Execute_HttpPostBody for any inputs whose value is a REST-URL that must be changed into a SOS KVP Get-Request.");

		List<ExecuteInput> inputs = executeBody.getInputs();

		for (ExecuteInput executeInput : inputs) {
			String inputValue = executeInput.getValue();

			if (inputValue.contains("http")) {
				logger.info("Found following input that is expected to be a SOS-request or other Web resource: \"{}\"",
						inputValue);

				/*
				 * if the link points to a web resource, then it is not changed.
				 * 
				 * Only SOS requests must be considered as follows: in case of a
				 * SOS request, two possibilities must be considered:
				 * 
				 * 1. a typical SOAP-SOS request of form
				 * "<SOS-baseURL>?<KVP-binding>" where <KVP-binding> contains
				 * all necessary URL parameters like {service, observedProperty,
				 * offering, etc.}. In this case this must not be changed at all
				 * and can be used as valid input!
				 * 
				 * 2. a RESTful URL that points to a timeseries and contains a
				 * temporalFilter. This one needs to be converted into a
				 * SOAP-SOS-Request as defined by 1.
				 * 
				 * 
				 */

				if (inputValue.contains("timeseries")) {
					/*
					 * this is case 2., a RESTful SOS URL that has to be
					 * converted
					 */

					replaceRestUrlWithSoapGetObservationRequest(executeInput, inputValue);
				}

				else {
					/*
					 * case 1: input is already a well-defined SOAP-SOS request
					 * or any other web resource. Thus do nothing
					 */
					logger.info(
							"Following URL to a web resource (which might be a SOS SOAP GetObservation request) has been detected: \"{}\"",
							inputValue);
				}

			}
		}

	}

	private void replaceRestUrlWithSoapGetObservationRequest(ExecuteInput executeInput, String inputValue)
			throws IOException {
		logger.info("Following input SOS-request as RESTful URL has been detected: \"{}\"", inputValue);
		logger.debug(
				"Trying to convert RESTful SOS URL \"{}\" to a valid SOS-SOAP-URL using typical Key-Value-Parameters!",
				inputValue);

		/*
		 * Now we have to extract SOS-baseURL and the timeSpan parameter value.
		 * 
		 * An example URL might look like:
		 * http://fluggs.wupperverband.de/sos2/api/v1/timeseries/ 461?
		 * timespan=PT12H/2013-08-06Z
		 * 
		 * with SOS base URL = http://fluggs.wupperverband.de/sos2
		 * 
		 * and timeSpan parameter = PT12H/2013-08-06Z
		 */
		String timespanParameterValue = extractTimespanParameter(inputValue);

		String targetUrl_timeseriesApi = constructTimeSeriesTargetUrl(inputValue);

		SosTimeseriesInformation sosInformation = fetchSosTimeseriesInformation(timespanParameterValue,
				targetUrl_timeseriesApi);

		/*
		 * build SOS request
		 */

		Map<String, String> kvpForSosRequest = createUrlParametersMap(sosInformation);
		String sosRequest = sosInformation.getSosUrl() + "?";

		sosRequest = appendKvpParameters(sosRequest, kvpForSosRequest);

		logger.debug("Constructed the SOS GetObservationRequest \"{}\" for the input \"{}\"", sosRequest, inputValue);

		/*
		 * now replace the REST-ful SOS URL with the KVP URL
		 */
		executeInput.setValue(sosRequest);
	}

	private String appendKvpParameters(String sosRequest, Map<String, String> kvpForSosRequest) {
		Set<Entry<String, String>> entrySet = kvpForSosRequest.entrySet();

		Iterator<Entry<String, String>> iterator = entrySet.iterator();

		while (iterator.hasNext()) {
			Entry<String, String> nextEntry = iterator.next();

			sosRequest = sosRequest + nextEntry.getKey() + "=" + nextEntry.getValue();

			/*
			 * add "&" if there are further KVP parameters.
			 */
			if (iterator.hasNext())
				sosRequest = sosRequest + "&";
		}

		return sosRequest;

	}

	private Map<String, String> createUrlParametersMap(SosTimeseriesInformation sosInformation) {
		Map<String, String> kvpForSosRequest = new HashMap<String, String>();

		kvpForSosRequest.put(SosTimeseriesInformation.SERVICE_PARAMATER_NAME,
				SosTimeseriesInformation.SERVICE_PARAMATER_VALUE);
		kvpForSosRequest.put(SosTimeseriesInformation.VERSION_PARAMATER_NAME,
				SosTimeseriesInformation.VERSION_PARAMATER_VALUE);
		kvpForSosRequest.put(SosTimeseriesInformation.REQUEST_PARAMATER_NAME,
				SosTimeseriesInformation.REQUEST_PARAMATER_VALUE);
		kvpForSosRequest.put(SosTimeseriesInformation.RESPONSE_FORMAT_PARAMATER_NAME,
				SosTimeseriesInformation.RESPONSE_FORMAT_PARAMATER_VALUE);

		kvpForSosRequest.put(SosTimeseriesInformation.OFFERING_ID_PARAMATER_NAME, sosInformation.getOfferingId());
		kvpForSosRequest.put(SosTimeseriesInformation.OBSERVED_PROPERTY_PARAMATER_NAME,
				sosInformation.getObservedProperty());
		kvpForSosRequest.put(SosTimeseriesInformation.FEATURE_OF_INTEREST_PARAMATER_NAME,
				sosInformation.getFeatureOfInterest());
		kvpForSosRequest.put(SosTimeseriesInformation.PROCEDURE_PARAMATER_NAME, sosInformation.getProcedure());
		kvpForSosRequest.put(SosTimeseriesInformation.TEMPORAL_FILTER_PARAMATER_NAME,
				sosInformation.getTemporalFilter());

		return kvpForSosRequest;
	}

	private SosTimeseriesInformation fetchSosTimeseriesInformation(String timespanParameterValue,
			String targetUrl_timeseriesApi) throws IOException {
		RestTemplate timeseriesApiTemplate = new RestTemplate();
		SosTimeseriesInformation timeseriesInformation = timeseriesApiTemplate.getForObject(targetUrl_timeseriesApi,
				SosTimeseriesInformation.class);

		// set the temporal filter from timespan parameter
		timeseriesInformation.setTemporalFilterFromEncodedTimespan(timespanParameterValue);

		return timeseriesInformation;
	}

	private String constructTimeSeriesTargetUrl(String inputValue) {
		/*
		 * An example URL might look like:
		 * http://fluggs.wupperverband.de/sos2/api/v1/timeseries/461?
		 * timespan=PT12H/2013-08-06Z
		 * 
		 * and target URL against timeseries-api to acquire necessary
		 * information to build SOS KVP request looks like
		 * http://fluggs.wupperverband.de/sos2/api/v1/timeseries/461?expanded=
		 * true
		 */
		String targetUrl_timeseriesApi = inputValue.split("?")[0];
		// now add expanded=true to URL
		targetUrl_timeseriesApi = targetUrl_timeseriesApi + "?expanded=true";
		return targetUrl_timeseriesApi;
	}

	private String extractTimespanParameter(String inputValue) {
		/*
		 * An example URL might look like:
		 * http://fluggs.wupperverband.de/sos2/api/v1/timeseries/461?
		 * timespan=PT12H/2013-08-06Z
		 * 
		 * with timeSpan parameter = PT12H/2013-08-06Z
		 */
		return inputValue.split("timespan=")[1];
	}

}
