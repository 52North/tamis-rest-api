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
package org.n52.tamis.core.json.deserialize.processes;

import java.io.IOException;

import org.n52.tamis.core.javarepresentations.processes.ProcessDescriptionInput;
import org.n52.tamis.core.javarepresentations.processes.ProcessDescriptionOutput;
import org.n52.tamis.core.javarepresentations.processes.ProcessDescription_singleProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class to deserialize the extended single process description document
 * returned by the WPS proxy.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class SingleProcessDescriptionDeserializer extends JsonDeserializer<ProcessDescription_singleProcess> {

	private static final String FORMAT_IDENTIFIER = "Format";
	private static final Logger logger = LoggerFactory.getLogger(ProcessesDeserializer.class);

	@Override
	public ProcessDescription_singleProcess deserialize(JsonParser jsonParser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		logger.info("Start deserialization of extended WPS singleProcessDescription document.");

		// initialization
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode node = codec.readTree(jsonParser);

		// create empty shortened CapabilitiesDocument.
		ProcessDescription_singleProcess singleProcessDescription_short = new ProcessDescription_singleProcess();

		/*
		 * JSPON element "ProcessSummaries" is an array of process descriptions
		 */
		JsonNode process = node.get("ProcessOffering").get("Process");

		/*
		 * label
		 */
		singleProcessDescription_short.setLabel(process.get("Title").asText());

		/*
		 * ID
		 */
		singleProcessDescription_short.setId(process.get("Identifier").asText());

		// array of Input elements
		JsonNode inputs = process.get("Input");

		transformAndAddInputs(singleProcessDescription_short, inputs);

		/*
		 * TODO are multiple outputs possible?
		 */
		JsonNode outputs = process.get("Output");

		transformAndAddOutputs(singleProcessDescription_short, outputs);

		logger.info("Deserialization ended! The following processes description instance was created: {}",
				singleProcessDescription_short);

		return singleProcessDescription_short;
	}

	private void transformAndAddOutputs(ProcessDescription_singleProcess singleProcessDescription_short,
			JsonNode outputNode) {
		if (outputNode.isArray()) {
			for (JsonNode output : outputNode) {
				transformAndAddOutput(singleProcessDescription_short, output);
			}
		} else
			transformAndAddOutput(singleProcessDescription_short, outputNode);

	}

	private void transformAndAddOutput(ProcessDescription_singleProcess singleProcessDescription_short,
			JsonNode output_extended) {
		ProcessDescriptionOutput output_short = new ProcessDescriptionOutput();

		/**
		 * id
		 */
		output_short.setId(output_extended.get("Identifier").asText());

		/**
		 * label
		 */
		output_short.setLabel(output_extended.get("Title").asText());

		/**
		 * type
		 */
		output_short.setType(output_extended.get("Identifier").asText());

		/*
		 * different types of output are possible ("ComplexData" or
		 * "LiteralData"), which are child Nodes of "Output". Either one might
		 * occur.
		 */
		JsonNode complexData = output_extended.get("ComplexData");
		JsonNode literalData = output_extended.get("LiteralData");

		if (complexData != null) {
			JsonNode format_complexData = complexData.get(FORMAT_IDENTIFIER);
			extractAndAddOutputFormat(output_short, format_complexData);
		}

		else if (literalData != null) {
			JsonNode format_literalData = literalData.get(FORMAT_IDENTIFIER);
			extractAndAddOutputFormat(output_short, format_literalData);
		} else
			logger.error(
					"Subtype of \"Output\" element is unknown. \"ComplexData\" or \"LiteralData\" was expected. JSON node looks like: {}",
					output_extended);

		singleProcessDescription_short.addInput(output_short);

	}

	private void transformAndAddInputs(ProcessDescription_singleProcess singleProcessDescription_short,
			JsonNode inputs) {
		for (JsonNode input_extended : inputs) {
			ProcessDescriptionInput input_short = new ProcessDescriptionInput();

			/*
			 * id
			 */

			input_short.setId(input_extended.get("Identifier").asText());

			/*
			 * label
			 */
			input_short.setLabel(input_extended.get("Title").asText());

			/*
			 * required
			 * 
			 * TAMIS WPS inputs always have the cardinality 1! So the are always
			 * required. In future, an optional parameter "maxOccurs" might be
			 * used to indicate multiple inputs.
			 */
			input_short.setRequired("true");

			/*
			 * either "Format" or "type" is present per input! TODO how to
			 * decide when to use "Format" or "type"? For now, everything is
			 * parsed as "type".
			 * 
			 * different types of input are possible ("ComplexData" or
			 * "LiteralData"), which are child Nodes of "Input". Either one
			 * might occur.
			 */
			JsonNode complexData = input_extended.get("ComplexData");
			JsonNode literalData = input_extended.get("LiteralData");

			if (complexData != null) {
				/*
				 * TODO format might be an array! Thus, we have to distinguish.
				 * In case of NO array, simply one "_mimeType" attribute is
				 * present. In case of AN ARRAY, we iterate over each
				 * format-entry and concat each mimeType sperated by " | "
				 */
				JsonNode format_complexData = complexData.get(FORMAT_IDENTIFIER);
				extractAndAddInputFormat(input_short, format_complexData);
			} else if (literalData != null) {
				JsonNode format_literalData = literalData.get(FORMAT_IDENTIFIER);
				extractAndAddInputFormat(input_short, format_literalData);
			} else
				logger.error(
						"Subtype of \"Input\" element is unknown. \"ComplexData\" or \"LiteralData\" was expected. JSON node looks like: {}",
						input_extended);

			singleProcessDescription_short.addInput(input_short);

		}

	}

	private void extractAndAddInputFormat(ProcessDescriptionInput input_short, JsonNode format) {
		String formatAstring = "";

		if (format.isArray()) {
			// format IS AN ARRAY
			int numberOfFormatEntries = format.size();

			for (int i = 0; i < numberOfFormatEntries; i++) {
				JsonNode formatEntry = format.get(i);

				if (i == 0)
					formatAstring = formatAstring + formatEntry.get("_mimeType").asText();

				else
					formatAstring = formatAstring + " | " + formatEntry.get("_mimeType").asText();
			}
		}

		else {
			// format is NOT AN ARRAY
			formatAstring = format.get("_mimeType").asText();
		}

		input_short.setType(formatAstring);
	}

	private void extractAndAddOutputFormat(ProcessDescriptionOutput output_short, JsonNode format) {
		String formatAstring = "";

		if (format.isArray()) {
			// format IS AN ARRAY
			int numberOfFormatEntries = format.size();

			for (int i = 0; i < numberOfFormatEntries; i++) {
				JsonNode formatEntry = format.get(i);

				if (i == 0)
					formatAstring = formatAstring + formatEntry.get("_mimeType").asText();

				else
					formatAstring = formatAstring + " | " + formatEntry.get("_mimeType").asText();
			}
		}

		else {
			// format is NOT AN ARRAY
			formatAstring = format.get("_mimeType").asText();
		}

		output_short.setType(formatAstring);
	}

}
