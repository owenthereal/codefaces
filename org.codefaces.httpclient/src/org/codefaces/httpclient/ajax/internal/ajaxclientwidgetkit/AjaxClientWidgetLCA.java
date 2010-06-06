package org.codefaces.httpclient.ajax.internal.ajaxclientwidgetkit;

import java.io.IOException;

import org.codefaces.httpclient.ajax.AjaxClient;
import org.codefaces.httpclient.ajax.AjaxClientWidget;
import org.codefaces.httpclient.ajax.JsonResponse;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Widget;

public class AjaxClientWidgetLCA extends AbstractWidgetLCA {
	private static final String JS_PARAM_STATUS = "status";
	private static final String JS_PARAM_CONTENT = "content";

	@Override
	public void preserveValues(Widget widget) {
		// Do Nothing. Nothing need to preserved in the javascript
	}

	@Override
	public void renderChanges(Widget widget) throws IOException {
		AjaxClientWidget ajaxClient = (AjaxClientWidget) widget;
		ajaxClient.getClient().flush();
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

		// TODO: check whether id is necessary
		writer.newWidget(AjaxClient.class.getName(), new Object[] { id });
	}

	@Override
	public void readData(Widget widget) {
		AjaxClientWidget ajaxClient = (AjaxClientWidget) widget;
		String status = WidgetLCAUtil.readPropertyValue(ajaxClient,
				JS_PARAM_STATUS);
		String content = WidgetLCAUtil.readPropertyValue(ajaxClient,
				JS_PARAM_CONTENT);
		if (status != null) {
			JsonResponse.STATUS responseStatus;
			// I think this is better than using Enum.valueOf.
			// Any unknown status String is considered as error.
			if (status.equals("success")) {
				responseStatus = JsonResponse.STATUS.SUCCESS;
			} else if (status.equals("timeout")) {
				responseStatus = JsonResponse.STATUS.TIMEOUT;
			} else {
				responseStatus = JsonResponse.STATUS.ERROR;
			}
			JsonResponse response = new JsonResponse(responseStatus, content);
			ajaxClient.getClient().setJsonResponse(response);
		}
	}
}
