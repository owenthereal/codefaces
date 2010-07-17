package org.codefaces.httpclient.internal.ajax;

/**
 * This class represent a single JSONP response received
 */
public class JsonResponse {
	public enum Status {SUCCESS, ERROR, TIMEOUT};
	
	private final Status status;
	private final String content;
	private final String requestId;
	
	public JsonResponse(String requestId, Status status, String content){
		this.requestId = requestId;
		this.status = status;
		this.content = content;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public Status getStatus(){
		return status;
	}
	
	/**
	 * @return a JSON string if SUCCESS, empty string for otherwise.
	 */
	public String getContent(){
		return content;
	}
}
