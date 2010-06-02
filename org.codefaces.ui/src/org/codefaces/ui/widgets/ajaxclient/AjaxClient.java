package org.codefaces.ui.widgets.ajaxclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class AjaxClient extends Control{

	private HttpRequest httpRequest = null;
	private HttpResponse httpResponse = null;


	public AjaxClient(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Send the given HTTP request
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the httpRequest is null</li>
	 *                </ul>
	 * 
	 * @param httpRequest
	 *            the given HTTP request
	 * @return the response of the HTTP request
	 */
	public HttpResponse sendHttpRequest(HttpRequest httpRequest){
		checkWidget();
		if (httpRequest == null) { SWT.error(SWT.ERROR_NULL_ARGUMENT); }
		
		this.httpRequest = httpRequest;
		this.httpResponse = null;
		while(this.httpResponse == null){
			Display display = getDisplay();
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		this.httpRequest = null;
		return this.httpResponse; 
	}
	
	public void setHttpRequest(HttpRequest request){
		httpRequest = request;
	}
	
	public void setHttpResponse(HttpResponse response){
		httpResponse = response;
	}
	
	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public HttpResponse getHttpResponse() {
		return httpResponse;
	}
}
