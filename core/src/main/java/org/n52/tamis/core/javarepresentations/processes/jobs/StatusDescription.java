package org.n52.tamis.core.javarepresentations.processes.jobs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusDescription {
	
	@JsonProperty("StatusInfo")
	private StatusInfo statusInfo;

	public StatusInfo getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(StatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}

	@Override
	public String toString() {
		return "StatusDescription [statusInfo=" + statusInfo + "]";
	}

}
