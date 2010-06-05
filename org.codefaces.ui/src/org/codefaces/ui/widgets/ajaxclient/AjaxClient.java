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
