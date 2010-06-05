package org.codefaces.ui.widgets.ajaxclient;

/**
 * This class represent a single JSONP response received
 * @author kklo
 */
public class JsonpResponse {
	public enum STATUS {SUCCESS, ERROR, TIMEOUT};
	
	private final STATUS status;
	private final String content;
	
	public JsonpResponse(STATUS status, String content){
		this.status = status;
		this.content = content;
	}
	
	public STATUS getStatus(){
		return status;
	}
	
	/**
	 * @return a JSON string if SUCCESS, empty string for otherwise.
	 */
	public String getContent(){
		return content;
	}
}
