package org.codefaces.ui.widgets.ajaxclient;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class AjaxClient extends Control{

	private JsonpRequest jsonpRequest = null;
	private JsonpResponse jsonpResponse = null;


	public AjaxClient(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
	 * Send the given JSONP request with a given url and default timeout.
	 * It construct a JsonpRequest object and pass it into the 
	 * sendJsonpRequest(JsonpRequest) method.
	 * 
	 * @param url the url
	 * @return the response of the JSONP request
	 */
	public JsonpResponse sendJsonpRequest(String url){
		return sendJsonpRequest(new JsonpRequest(url));
	}

	/**
	 * Send the given JSONP request
	 * 
	 * @param jsonpRequest
	 *            the given JSONP request
	 * @return the response of the JSONP request
	 */
	public JsonpResponse sendJsonpRequest(JsonpRequest jsonpRequest){
		checkWidget();
		if (jsonpRequest == null) return null;
		
		this.jsonpRequest = jsonpRequest;
		this.jsonpResponse = null;
		while(this.jsonpResponse == null){
			Display display = getDisplay();
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		this.jsonpRequest = null;
		return this.jsonpResponse; 
	}
	
	public void setJsonpRequest(JsonpRequest request){
		jsonpRequest = request;
	}
	
	public void setJsonpResponse(JsonpResponse response){
		jsonpResponse = response;
	}
	
	public JsonpRequest getJsonpRequest() {
		return jsonpRequest;
	}

	public JsonpResponse getJsonpResponse() {
		return jsonpResponse;
	}
}
