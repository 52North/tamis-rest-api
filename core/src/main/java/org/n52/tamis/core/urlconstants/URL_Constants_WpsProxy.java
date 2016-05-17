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
package org.n52.tamis.core.urlconstants;

public class URL_Constants_WpsProxy {

	public static final String SLASH_OUTPUTS = "/outputs";

	public static final String SLASH_JOBS = "/jobs";

	public static final String SLASH_PROCESSES = "/processes";

	public static final String API_PREFIX = "api";

	public static final String V1_PREFIX = "/v1";

	public static final String API_V1_BASE_PREFIX = API_PREFIX + V1_PREFIX;

	/*
	 * Variable decleration: reusable names for variable id-parameter like
	 * service-id, process-id, job-id
	 */

	public static final String SERVICE_ID_VARIABLE_NAME = "service_id";
	public static final String SERVICE_ID_VARIABLE_NAME_WITH_BRACES = "{" + SERVICE_ID_VARIABLE_NAME + "}";

	public static final String PROCESS_ID_VARIABLE_NAME = "process_id";
	/*
	 * NOTE ":.+" at the end of the process id --> this is necessary in case
	 * that the process id contains "."(dots) in the identifier. Normally,
	 * Spring will truncate it and consider the part of the URL after the last
	 * dot as file ending, which will be separated automatically (e.g identifier
	 * "org.n52.process.testprocess" "testprocess" would be considered as file
	 * ending).
	 * 
	 * In order to allow any dots inside the URL identifier, the regex
	 * expression ":.+" must be added!
	 * 
	 */
	public static final String PROCESS_ID_VARIABLE_NAME_WITH_BRACES = "{" + PROCESS_ID_VARIABLE_NAME + ":.+}";

	public static final String JOB_ID_VARIABLE_NAME = "job_id";
	public static final String JOB_ID_VARIABLE_NAME_WITH_BRACES = "{" + JOB_ID_VARIABLE_NAME + "}";

	public static final String OUTPUT_ID_VARIABLE_NAME = "output_id";
	public static final String OUTPUT_ID_VARIABLE_NAME_WITH_BRACES = "{" + OUTPUT_ID_VARIABLE_NAME + "}";

	public static final String SYNC_EXECUTE_TRUE = "?sync-execute=true";

	public static final String SYNC_EXECUTE_FALSE = "?sync-execute=false";

	/*
	 * constant URLs for each request of the WPS
	 */

	// public static final String CAPABILITIES = API_V1_BASE_PREFIX;
	public static final String CAPABILITIES = "";

	public static final String PROCESSES = CAPABILITIES + SLASH_PROCESSES;

	public static final String PROCESS_DESCRIPTION_SINGLE = PROCESSES + "/" + PROCESS_ID_VARIABLE_NAME_WITH_BRACES;

	public static final String EXECUTE = PROCESS_DESCRIPTION_SINGLE;
	
	public static final String EXECUTE_ASYNCHRONOUS = PROCESS_DESCRIPTION_SINGLE + SYNC_EXECUTE_FALSE;

	public static final String EXECUTE_SYNCHRONOUS = PROCESS_DESCRIPTION_SINGLE + SYNC_EXECUTE_TRUE;

	public static final String STATUS = PROCESS_DESCRIPTION_SINGLE + SLASH_JOBS + "/"
			+ JOB_ID_VARIABLE_NAME_WITH_BRACES;

	public static final String OUTPUTS = STATUS + SLASH_OUTPUTS;

	public static final String OUTPUT = OUTPUTS + "/" + OUTPUT_ID_VARIABLE_NAME_WITH_BRACES;

	public static final String DELETE = STATUS;

}
