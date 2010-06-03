package org.codefaces.ui.widgets.ajaxclient;

/**
 * This class represent a single Qooxdoo HTTP request called
 * @author kklo
 */
public class HttpRequest {
	private final String url;
	private final HttpMethod httpMethod;
	private final boolean asynchronous;
	private final Integer timeout;

	/**
	 * Creates a new instance of an asynchronous HttpRequest with default
	 * timeout value.
	 * 
	 * @param url
	 *            URL to load
	 * @param httpMethod
	 *            the HTTP request method type
	 */
	public HttpRequest(String url, HttpMethod httpMethod){
		this(url, httpMethod, true);
	}
	
	/**
	 * Creates a new instance of HttpRequest with default timeout value.
	 * @param url
	 *            URL to load
	 * @param httpMethod
	 *            the HTTP request method type
	 * @param asynchronous
	 *            set the request to asynchronous
	 */
	public HttpRequest(String url, HttpMethod httpMethod, boolean asynchronous){
		this(url, httpMethod, asynchronous, null);
	}
	
	/**
	 * Creates a new instance of HttpRequest.
	 * 
	 * @param url
	 *            URL to load
	 * @param httpMethod
	 *            the HTTP request method type
	 * @param asynchronous
	 *            set the request to asynchronous
	 * @param timeout
	 *            number of milliseconds before the request is being timed out.
	 *            To use the default timeout value, set it null
	 */
	public HttpRequest(String url, HttpMethod httpMethod, boolean asynchronous, Integer timeout){
		this.url = url;
		this.httpMethod = httpMethod;
		this.asynchronous = asynchronous;
		this.timeout = timeout;
	}

	public String getUrl() {
		return url;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public boolean isAsynchronous() {
		return asynchronous;
	}

	public int getTimeout() {
		return timeout;
	}
}
