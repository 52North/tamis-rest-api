package org.n52.tamis.core.javarepresentations.processes.jobs.result;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java representation of a result data output.
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
public class ResultData {

	@JsonProperty("_mimeType")
	private String mimeType;

	@JsonProperty("_encoding")
	private String encoding;

	@JsonProperty("_text")
	private String text;

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "ResultData [mimeType=" + mimeType + ", encoding=" + encoding + ", href=" + text + "]";
	}

}
