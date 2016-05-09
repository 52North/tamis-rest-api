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
package org.n52.tamis.core.json.deserialize.capabilities;

import java.io.IOException;

import org.n52.tamis.core.javarepresentations.capabilities.Capabilities_Tamis;
import org.n52.tamis.core.urlconstants.URL_Constants_TAMIS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class to deserialize the extended capabilities document returned by the WPS
 * proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class CapabilitiesDeserializer extends JsonDeserializer<Object> {

	Logger logger = LoggerFactory.getLogger(CapabilitiesDeserializer.class);

	/*
	 * The service id constant is (temporarily) used, if there is only one
	 * single WPS instance. TODO how to address multiple WPS instances properly?
	 */
	private static final String SERVICE_ID_CONSTANT = "1";

	/**
	 * Parses the extended capabilities document and creates the shortened
	 * document as instance of {@link Capabilities_Tamis}.
	 */
	@Override
	public Capabilities_Tamis deserialize(JsonParser jsonParser, DeserializationContext desContext)
			throws IOException, JsonProcessingException {
		logger.info("Start deserialization of extended WPS capabilities document.");

		// initialization
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode node = codec.readTree(jsonParser);

		// create empty shortened CapabilitiesDocument.
		Capabilities_Tamis capabilities_short = new Capabilities_Tamis();

		/*
		 * id
		 * 
		 * TODO FIXME how to determine the id?
		 */
		JsonNode capabilitiesNode = node.get("Capabilities");
		JsonNode serviceIdentificationNode = capabilitiesNode.get("ServiceIdentification");

		capabilities_short.setId(SERVICE_ID_CONSTANT);

		/*
		 * label
		 */
		String title = serviceIdentificationNode.get("Title").asText();

		capabilities_short.setLabel(title);

		/*
		 * type
		 */
		String serviceType = serviceIdentificationNode.get("ServiceType").asText();

		capabilities_short.setType(serviceType);

		/*
		 * url
		 * 
		 * url is static (with exception of the serviceId; however, for now the
		 * serviceId is set to a static value of 1)! Thus just set the static
		 * value TODO if serviceId becomes variable, change the code!
		 */
		capabilities_short.setUrl(URL_Constants_TAMIS.getCapabilitiesURL(SERVICE_ID_CONSTANT));

		/*
		 * e-mail contact
		 */
		String mailAdress = capabilitiesNode.get("ServiceProvider").get("ServiceContact").get("ContactInfo")
				.get("Address").get("ElectronicMailAddress").asText();

		capabilities_short.setContact(mailAdress);

		logger.info("Deserialization ended! The following capabilities instance was created: {}", capabilities_short);

		return capabilities_short;
	}

}
