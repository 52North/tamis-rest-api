package org.n52.tamis.core.json.serialize.processes.execute;

import java.io.IOException;
import java.util.List;

import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteInput;
import org.n52.tamis.core.javarepresentations.processes.execute.ExecuteOutput;
import org.n52.tamis.core.javarepresentations.processes.execute.Execute_HttpPostBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializes an instance of {@link Execute_HttpPostBody} into an extended
 * version that can be used as HTTP POST request body for an execute request
 * against the WPS proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ExtendedExecuteHttpPostBodySerializer extends JsonSerializer<Execute_HttpPostBody> {

	private static final Logger logger = LoggerFactory.getLogger(ExtendedExecuteHttpPostBodySerializer.class);

	@Override
	public void serialize(Execute_HttpPostBody executeBody_short, JsonGenerator jsonGenerator,
			SerializerProvider serProvider) throws IOException, JsonProcessingException {

		logger.info("Start serialization of execute HTTP POST input body \"{}\"", executeBody_short);
		
		/*
		 * the structure of the target JSON body looks like:
		 * 
		 * {
    "Execute": {
        "Identifier": "org.n52.tamis.algorithm.interpolation",
        "Input": [
            {
                "Input": [
                    {
                        "Reference": {
                            "_href": "http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Grundwasserstand&responseFormat=http%3A//www.opengis.net/om/2.0",
                            "_mimeType": "application/om+xml; version=2.0",
                            "_schema": "http://schemas.opengis.net/om/2.0/observation.xsd"
                        },
                        "_id": "gw1"
                    },
                    {
                        "Reference": {
                            "_href": "http://fluggs.wupperverband.de/sos2/service?service=SOS&request=GetObservation&version=2.0.0&offering=Zeitreihen_2m_Tiefe&observedProperty=Sohlenwasserdruck&responseFormat=http%3A//www.opengis.net/om/2.0",
                            "_mimeType": "application/om+xml; version=2.0",
                            "_schema": "http://schemas.opengis.net/om/2.0/observation.xsd"
                        },
                        "_id": "swd1"
                    }
                ],
                "_id": "input-variables"
            },
            {
                "Data": {
                    "_mimeType": "text/xml",
                    "_schema": "http://schemas.opengis.net/gml/3.2.1/base/feature.xsd",
                    "__text": "<wfs:FeatureCollection xmlns:testbed11="http://opengeospatial.org"
				xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml"
				xmlns:ogc="http://www.opengis.net/ogc" xmlns:ows="http://www.opengis.net/ows"
				xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xmlns:wml2="http://www.opengis.net/waterml/2.0" xmlns:sf="http://www.opengis.net/sampling/2.0"
				xmlns:sams="http://www.opengis.net/samplingSpatial/2.0"
				numberOfFeatures="1" timeStamp="2016-02-15T16:24:55.640Z"
				xsi:schemaLocation="http://www.opengis.net/wfs http://geoprocessing.demo.52north.org:8080/geoserver/schemas/wfs/1.1.0/wfs.xsd">
				<gml:featureMembers>
					<wml2:MonitoringPoint gml:id="xyz.1">
						<gml:identifier codeSpace="http://www.opengis.net/def/nil/OGC/0/unknown">xyz</gml:identifier>
						<gml:name codeSpace="http://www.opengis.net/def/nil/OGC/0/unknown">xyz</gml:name>
						<sf:sampledFeature xlink:href="urn:ogc:def:nil:OGC:unknown" />
						<sams:shape>
							<ns:Point xmlns:ns="http://www.opengis.net/gml/3.2"
								ns:id="point_xyz">
								<ns:pos srsName="http://www.opengis.net/def/crs/EPSG/0/31466">5668202.8356 2595842.8958</ns:pos>
							</ns:Point>
						</sams:shape>
					</wml2:MonitoringPoint>
				</gml:featureMembers>
			</wfs:FeatureCollection>"
                },
                "_id": "target-variable-point"
            }
        ],
        "Output": {
            "_mimeType": "application/om+xml; version=2.0",
            "_schema": "http://schemas.opengis.net/om/2.0/observation.xsd",
            "_id": "estimated-values",
            "_transmission": "value"
        }, 
        "_service": "WPS",
        "_version": "2.0.0"}
}

		 */

//		ObjectMapper mapper = new ObjectMapper();
		
		jsonGenerator.writeStartObject();

		jsonGenerator.writeObjectFieldStart("Execute");

		jsonGenerator.writeStringField("Identifier", executeBody_short.getProcessId());

		jsonGenerator.writeArrayFieldStart("Input");

		List<ExecuteInput> inputs = executeBody_short.getInputs();

		for (ExecuteInput executeInput : inputs) {
			jsonGenerator.writeObject(executeInput);
		}

		jsonGenerator.writeEndArray();

		jsonGenerator.writeArrayFieldStart("Output");

		List<ExecuteOutput> outputs = executeBody_short.getOutputs();

		for (ExecuteOutput executeOutput : outputs) {
			jsonGenerator.writeObject(executeOutput);
		}

		jsonGenerator.writeEndArray();

		jsonGenerator.writeStringField("_service", Execute_HttpPostBody.service);

		jsonGenerator.writeStringField("_version", executeBody_short.getVersion());

		jsonGenerator.writeEndObject();

		jsonGenerator.writeEndObject();
	}

}
