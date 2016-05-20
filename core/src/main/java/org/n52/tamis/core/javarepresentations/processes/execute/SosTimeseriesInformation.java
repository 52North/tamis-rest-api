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
package org.n52.tamis.core.javarepresentations.processes.execute;

import java.io.IOException;

import org.n52.tamis.core.json.deserialize.processes.execute.SosTimeseriesInformationDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * THis class holds necessary information in order to contruct a SOS KVP Get
 * Request
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@JsonDeserialize(using = SosTimeseriesInformationDeserializer.class)
public class SosTimeseriesInformation {

	public static final String SERVICE_PARAMATER_NAME = "service";
	public static final String SERVICE_PARAMATER_VALUE = "SOS";
	
	public static final String VERSION_PARAMATER_NAME = "version";
	public static final String VERSION_PARAMATER_VALUE = "2.0.0";
	
	public static final String REQUEST_PARAMATER_NAME = "request";
	public static final String REQUEST_PARAMATER_VALUE = "GetObservation";
	
	public static final String RESPONSE_FORMAT_PARAMATER_NAME = "responseFormat";
	public static final String RESPONSE_FORMAT_PARAMATER_VALUE = "http://www.opengis.net/om/2.0";
	
	public static final String FEATURE_OF_INTEREST_PARAMATER_NAME = "featureOfInterest";

	public static final String OFFERING_ID_PARAMATER_NAME = "offering";

	public static final String PROCEDURE_PARAMATER_NAME = "procedure";

	public static final String TEMPORAL_FILTER_PARAMATER_NAME = "temporalFilter";

	public static final String OBSERVED_PROPERTY_PARAMATER_NAME = "observedProperty";

	private String featureOfInterest;

	private String SosUrl;

	private String offeringId;

	private String procedure;

	private String temporalFilter;

	private String observedProperty;

	public String getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setFeatureOfInterest(String featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}

	public String getSosUrl() {
		return SosUrl;
	}

	public void setSosUrl(String sosUrl) {
		SosUrl = sosUrl;
	}

	public String getOfferingId() {
		return offeringId;
	}

	public void setOfferingId(String offeringId) {
		this.offeringId = offeringId;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getTemporalFilter() {
		return temporalFilter;
	}

	public void setTemporalFilter(String temporalFilter) {
		this.temporalFilter = temporalFilter;
	}

	/**
	 * Takes a timeSpan encoded in ISO 8601 format and converts it into a SOS
	 * temporalFilter value encoded like "{startTime}/{endTime}". Refer to
	 * https://en.wikipedia.org/wiki/ISO_8601#Time_intervals
	 * 
	 * 
	 * @param encodedTimespan
	 * @throws IOException
	 */
	public void setTemporalFilterFromEncodedTimespan(String encodedTimespan) throws IOException {

		String sosTemporalFilter = TimespanToSosTemporalFilterConverter
				.convertIso8601TimespanToSosTemporalFilter(encodedTimespan);

		this.temporalFilter = sosTemporalFilter;
	}

	public String getObservedProperty() {
		return observedProperty;
	}

	public void setObservedProperty(String observedProperty) {
		this.observedProperty = observedProperty;
	}

}
