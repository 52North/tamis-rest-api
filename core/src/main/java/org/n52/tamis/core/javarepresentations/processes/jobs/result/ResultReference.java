package org.n52.tamis.core.javarepresentations.processes.jobs.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java representation of a result reference output
 * 
 * @author Christian Danowski (contact: c.danowski@52north.org)
 *
 */
@JsonInclude(Include.NON_NULL)
public class ResultReference {

	@JsonProperty("_mimeType")
	private String mimeType;

	@JsonProperty("_encoding")
	private String encoding;

	@JsonProperty("_href")
	private String href;

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

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		return "ResultReference [mimeType=" + mimeType + ", encoding=" + encoding + ", href=" + href + "]";
	}

}
