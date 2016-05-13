package org.n52.tamis.core.test.timeInterval;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.n52.tamis.core.javarepresentations.processes.execute.TimespanToSosTemporalFilterConverter;

public class TimeSpanToSosTemporalFilter_Test {

	@Test
	public void transformTimespanToSosTemporalFilter() {

		String timespan = "PT12H/2013-08-06";

		String sosTemproalFilter = "";
		try {
			sosTemproalFilter = TimespanToSosTemporalFilterConverter
					.convertIso8601TimespanToSosTemporalFilter(timespan);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("2013-08-05T12:00:00.000Z/2013-08-06T00:00:00.000Z", sosTemproalFilter);

	}

}
