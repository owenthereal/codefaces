package org.codefaces.httpclient.internal.ajax;

import java.util.UUID;

/**
 * This class represent a single JSONP request called
 */
public class JsonGet {
	private final static int DEFAULT_TIMEOUT = 0;

	private String requestId;

	private final int timeout;

	private final String url;

	/**
	 * Creates a new instance of an asynchronous JsonpRequest with default
	 * timeout value.
	 * 
	 * @param url
	 *            URL to load
	 */
	public JsonGet(String url) {
		this(url, DEFAULT_TIMEOUT);
	}

	/**
	 * Creates a new instance of an asynchronous JsonpRequest
	 * 
	 * @param url
	 *            URL to load
	 * @param timeout
	 *            number of milliseconds before the request is being timed out.
	 *            To use the default timeout value, set it to 0
	 */
	public JsonGet(String url, int timeout) {
		this.requestId = UUID.randomUUID().toString();
		this.url = url;
		this.timeout = timeout;
	}

	public String getRequestId() {
		return requestId;
	}

	public int getTimeout() {
		return timeout;
	}

	public String getUrl() {
		return url;
	}
}
