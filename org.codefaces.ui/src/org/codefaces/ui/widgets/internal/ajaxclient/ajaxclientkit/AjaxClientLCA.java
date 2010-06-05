package org.codefaces.ui.widgets.internal.ajaxclient.ajaxclientkit;

import java.io.IOException;

import org.codefaces.ui.widgets.ajaxclient.AjaxClient;
import org.codefaces.ui.widgets.ajaxclient.JsonpRequest;
import org.codefaces.ui.widgets.ajaxclient.JsonpResponse;
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
	private static final String JS_SEND_JSONP_REQUEST = "sendJsonpRequest";
	private static final String JS_PARAM_STATUS = "status";
	private static final String JS_PARAM_CONTENT = "content";

	@Override
	public void preserveValues(Widget widget) {
		//Do Nothing. Nothing need to preserved in the javascript 
	}

	@Override
	public void renderChanges(Widget widget) throws IOException {
		sendJsonpRequest((AjaxClient)widget);
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
		String status = WidgetLCAUtil.readPropertyValue(ajaxClient,
				JS_PARAM_STATUS);
		String content = WidgetLCAUtil.readPropertyValue(ajaxClient,
				JS_PARAM_CONTENT);
		
		if(status != null){
			JsonpResponse.STATUS responseStatus;
			// I think this is better than using Enum.valueOf.
			// Any unknown status String is considered as error.
			if(status.equals("success")){
				responseStatus = JsonpResponse.STATUS.SUCCESS;
			}
			else if(status.equals("timeout")){
				responseStatus = JsonpResponse.STATUS.TIMEOUT;
			}else{
				responseStatus = JsonpResponse.STATUS.ERROR;
			}
			JsonpResponse response = new JsonpResponse(
					responseStatus, content);
			ajaxClient.setJsonpResponse(response);
		}
	}

	/**
	 * Tell the javascript to send a JSONP request
	 * @param ajaxClient an AjaxClient
	 * @throws IOException if IO error occurs
	 */
	private void sendJsonpRequest(final AjaxClient ajaxClient) throws IOException{
		JsonpRequest request = ajaxClient.getJsonpRequest();
		if(request != null){
			String url = request.getUrl();
			int timeout = request.getTimeout();
			JSWriter writer = JSWriter.getWriterFor(ajaxClient);
			writer.call(JS_SEND_JSONP_REQUEST, new Object[] { url, timeout });
		}
	}
}
