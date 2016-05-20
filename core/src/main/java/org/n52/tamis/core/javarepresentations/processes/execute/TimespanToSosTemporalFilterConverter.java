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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

/**
 * Class to convert an ISO 8601 time interval into a SOS GetObservation
 * temporalFilter parameter String like "{startTime}/{endtime}".
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class TimespanToSosTemporalFilterConverter {

	/**
	 * Converts a timespan String that describes a time interval in IS= 8601
	 * format to a SOS temporalFilter String.
	 * 
	 * E.g.: the timespan "PT12H/2013-08-06" will be converted to the String
	 * "2013-08-05T12:00:00.000Z/2013-08-06T00:00:00.000Z", which may serve as
	 * temporalFIlter parameter in a SOS GetObservation request.
	 * 
	 * @param timespan
	 *            an ISO 8601 encoded timespan.
	 * @return a String, consisting of "{startTime}/{endTime}",which may serve
	 *         as temporalFIlter parameter in a SOS GetObservation request
	 * @throws IOException
	 */
	public static String convertIso8601TimespanToSosTemporalFilter(String timespan) throws IOException {
		// /*
		// * if timespan String contains a "Z" then the parsed time shall be
		// interpreted as UTC.
		// * A timespan String like "PT12H/2013-08-06Z" cannot be parsed due to
		// the final Z; parser throws malformed format exception.
		// *
		// * Hence, we set UTC and standard interpretation and remove the Z from
		// the String
		// */
		// if (encodedTimespan.contains("Z")){
		// DateTimeZone.setDefault(DateTimeZone.UTC);
		//
		// encodedTimespan.
		// }

		try {

			// set UTC as standard time
			DateTimeZone.setDefault(DateTimeZone.UTC);

			Interval timeInterval = Interval.parse(timespan);

			DateTime startTime = timeInterval.getStart();
			DateTime endTime = timeInterval.getEnd();

			String sosTemproalFilter = startTime.toString() + "/" + endTime.toString();

			return sosTemproalFilter;

		} catch (IllegalArgumentException e) {
			String message = "Could not parse timespan parameter." + timespan;
			throw new IOException(message, e);
		}
	}

}
