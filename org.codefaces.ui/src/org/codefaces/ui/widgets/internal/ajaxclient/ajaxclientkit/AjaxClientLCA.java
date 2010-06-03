package org.codefaces.ui.widgets.internal.ajaxclient.ajaxclientkit;

import java.io.IOException;

import org.codefaces.ui.widgets.ajaxclient.AjaxClient;
import org.codefaces.ui.widgets.ajaxclient.HttpRequest;
import org.codefaces.ui.widgets.ajaxclient.HttpResponse;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Widget;

public class AjaxClientLCA extends AbstractWidgetLCA {
	private static final String WIDGET_ID = 
		"org.codefaces.ui.widgets.ajaxclient.AjaxClient";
	private static final String JS_SEND_HTTP_REQUEST = "sendHttpRequest";
	private static final String JS_PARAM_HTTP_STATUS_CODE = "statusCode";
	private static final String JS_PARAM_HTTP_CONTENT = "content";

	@Override
	public void preserveValues(Widget widget) {
		//Do Nothing. Nothing need to preserved in the javascript 
	}

	@Override
	public void renderChanges(Widget widget) throws IOException {
		sendHttpRequest((AjaxClient)widget);
	}

	@Override
	public void renderDispose(Widget widget) throws IOException {
		JSWriter writer = JSWriter.getWriterFor(widget);
		writer.dispose();  
	}

	@Override
	public void renderInitialization(Widget widget) throws IOException {
		JSWriter writer = JSWriter.getWriterFor(widget);
		String id = WidgetUtil.getId(widget);
		writer.newWidget(WIDGET_ID, new Object[] { id });
		ControlLCAUtil.writeStyleFlags((AjaxClient) widget);
	}

	@Override
	public void readData(Widget widget) {
		AjaxClient ajaxClient = (AjaxClient)widget;
		String statusCode = WidgetLCAUtil.readPropertyValue(ajaxClient,
				JS_PARAM_HTTP_STATUS_CODE);
		String content = WidgetLCAUtil.readPropertyValue(ajaxClient,
				JS_PARAM_HTTP_CONTENT);
		if(statusCode != null){
			HttpResponse response = new HttpResponse(
					Integer.parseInt(statusCode), content);
			ajaxClient.setHttpResponse(response);
		}
	}

	/**
	 * Tell the javascript to send a http request
	 * @param ajaxClient an AjaxClient
	 * @throws IOException if IO error occurs
	 */
	private void sendHttpRequest(final AjaxClient ajaxClient) throws IOException{
		HttpRequest request = ajaxClient.getHttpRequest();
		if(request != null){
			String url = request.getUrl();
			String method = request.getHttpMethod().toString();
			boolean async = request.isAsynchronous();
			Integer timeout = request.getTimeout();
			JSWriter writer = JSWriter.getWriterFor(ajaxClient);
			writer.call(JS_SEND_HTTP_REQUEST, new Object[] { url, method,
					async, timeout });
		}
	}
}
