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
package org.n52.tamis.rest.forward;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.n52.tamis.rest.controller.ParameterValueStore;

/**
 * Generic interface to forward an incoming request to a WPS proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public interface RequestForwarder {

	/**
	 * Forwards the request to the WPS proxy, retrieves an extended JSON-encoded
	 * document and transforms the content to the appropriate "shortened"
	 * document.
	 * @param <T>
	 * 
	 * @param request
	 *            the original request
	 * @param requestBody
	 *            an optional Java representation of the request body (sent via
	 *            POST and parsed as Java representation of the JSON code). May
	 *            be null if not needed.
	 * @param parameterValueStore
	 *            stores request specific parameter-value-pairs in a map.
	 * @return an instance of the appropriate Java representation of return
	 *         document.
	 * @throws IOException 
	 */
	public <T> Object forwardRequestToWpsProxy(HttpServletRequest request, T requestBody,
			ParameterValueStore parameterValueStore) throws IOException;

}
