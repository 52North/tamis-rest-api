package org.n52.tamis.rest.controller;

import java.util.HashMap;
import java.util.Map;

public class ParameterValueStore {

	private Map<String, String> parameterValuePairs;

	public ParameterValueStore() {
		this.parameterValuePairs = new HashMap<String, String>();
	};
	
	public void addParameterValuePair(String key, String value){
		this.parameterValuePairs.put(key, value);
	}

	public Map<String, String> getParameterValuePairs() {
		return parameterValuePairs;
	}

}
