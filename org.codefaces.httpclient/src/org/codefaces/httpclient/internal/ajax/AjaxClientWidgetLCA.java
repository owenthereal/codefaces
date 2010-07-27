package org.codefaces.httpclient.internal.ajax;

import java.io.IOException;

import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Widget;

public class AjaxClientWidgetLCA extends AbstractWidgetLCA {
	private static final String JS_PARAM_STATUS = "status";
	private static final String JS_PARAM_CONTENT = "content";
	private static final String JS_PARAM_REQUEST_ID = "requestId";
	private static final String JS_SEND_JSONP_REQUEST = "sendJsonpRequest";

	@Override
	public void preserveValues(Widget widget) {
		// do nothing
	}

	@Override
	public void renderChanges(Widget widget) throws IOException {
		AjaxClientWidget ajaxClientWidget = (AjaxClientWidget) widget;
		JsonGet jsonGet = ajaxClientWidget.getClient().nextRequest();
		if (jsonGet == null) {
			return;
		}

		String requestId = jsonGet.getRequestId();
		String url = jsonGet.getUrl();
		int timeout = jsonGet.getTimeout();
		JSWriter writer = JSWriter.getWriterFor(widget);
		writer.call(JS_SEND_JSONP_REQUEST, new Object[] { requestId, url,
				timeout });
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
		writer.newWidget(AjaxClientWidget.class.getName(), new Object[] { id });
	}

	@Override
	public void readData(final Widget widget) {
		String status = WidgetLCAUtil
				.readPropertyValue(widget, JS_PARAM_STATUS);
		if (status != null) {
			JsonResponse.Status responseStatus;
			if (status.equals("success")) {
				responseStatus = JsonResponse.Status.SUCCESS;
			} else if (status.equals("timeout")) {
				responseStatus = JsonResponse.Status.TIMEOUT;
			} else {
				responseStatus = JsonResponse.Status.ERROR;
			}

			String requestId = WidgetLCAUtil.readPropertyValue(widget,
					JS_PARAM_REQUEST_ID);
			String content = WidgetLCAUtil.readPropertyValue(widget,
					JS_PARAM_CONTENT);
			final JsonResponse response = new JsonResponse(requestId,
					responseStatus, content);

			((AjaxClientWidget) widget).getClient().setJsonResponse(response);
		}
	}
}
