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
package org.n52.tamis.core.json.deserialize.processes.jobs.result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.n52.tamis.core.javarepresentations.processes.jobs.result.ResultDocument;
import org.n52.tamis.core.javarepresentations.processes.jobs.result.ResultOutput;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ResultsDeserializer extends JsonDeserializer<ResultDocument> {

	@Override
	public ResultDocument deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		ObjectCodec codec = jsonParser.getCodec();
		
		JsonNode node = codec.readTree(jsonParser);
		
		List<ResultOutput> outputs = new ArrayList<>();
		
		try {
			
			JsonNode outputNode = node.get("Result").get("Output");
			
			if(outputNode instanceof ArrayNode){
				Iterator<JsonNode> outputIterator = ((ArrayNode)outputNode).iterator();
				
				while (outputIterator.hasNext()) {
					JsonNode jsonNode = (JsonNode) outputIterator.next();
					outputs.add(createResultOutput(jsonNode));
				}
			}else{
				outputs.add(createResultOutput(outputNode));
			}
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Result document JSON not valid." + node);
		}
		
		ResultDocument resultDocument = new ResultDocument();
		
		resultDocument.setResult(outputs);
				
		return resultDocument;
	}

	private ResultOutput createResultOutput(JsonNode outputNode) {
		
		ResultOutput resultOutput = new ResultOutput();
		
		String id;
		
		try {
			id = outputNode.get("ID").asText();
		} catch (Exception e) {
			throw new IllegalArgumentException("Output has no id.");
		}
		
		resultOutput.setId(id);
		
		String value = "";
		
		JsonNode complexDataNode = outputNode.path("ComplexData");
		
		if(!complexDataNode.isMissingNode()){
			try {
				value = complexDataNode.get("_text").asText();
			} catch (Exception e) {
				throw new IllegalArgumentException("ComplexOutput has no _text node.");
			}
		}
		
		JsonNode literalDataNode = outputNode.path("LiteralData");
		
		if(!literalDataNode.isMissingNode()){
			try {
				value = literalDataNode.get("_text").asText();
			} catch (Exception e) {
				throw new IllegalArgumentException("LiteralOutput has no _text node.");
			}
		}
		
		JsonNode referenceDataNode = outputNode.path("Reference");
		
		if(!referenceDataNode.isMissingNode()){
			try {
				value = referenceDataNode.get("_href").asText();
			} catch (Exception e) {
				throw new IllegalArgumentException("ReferenceOutput has no _href node.");
			}
		}
		
		resultOutput.setValue(value);
		
		return resultOutput;
	}

}
