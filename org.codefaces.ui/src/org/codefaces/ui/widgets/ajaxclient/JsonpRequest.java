package org.codefaces.ui.widgets.ajaxclient;

/**
 * This class represent a single JSONP request called
 * @author kklo
 */
public class JsonpRequest {
	private final static int DEFAULT_TIMEOUT = 0;
	
	private final String url;
	private final int timeout;

	/**
	 * Creates a new instance of an asynchronous JsonpRequest with default
	 * timeout value.
	 * 
	 * @param url
	 *            URL to load
	 */
	public JsonpRequest(String url){
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
	public JsonpRequest(String url, int timeout){
		this.url = url;
		this.timeout = timeout;
	}

	public String getUrl() {
		return url;
	}

	public int getTimeout() {
		return timeout;
	}
}
