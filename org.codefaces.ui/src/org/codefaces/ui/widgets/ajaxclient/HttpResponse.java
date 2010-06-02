package org.codefaces.ui.widgets.ajaxclient;

/**
 * This class represent a single Qooxdoo HTTP response received
 * @author kklo
 */
public class HttpResponse {
	private final int statusCode;
	private final String content;
	
	public HttpResponse(int statusCode, String content){
		this.statusCode = statusCode;
		this.content = content;
	}
	
	public int getStatusCode(){
		return statusCode;
	}
	
	public String getContent(){
		return content;
	}
}
