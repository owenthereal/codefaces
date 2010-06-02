package org.codefaces.ui.widgets.internal.ajaxclient.ajaxclientkit;

import java.io.IOException;

import org.codefaces.ui.widgets.ajaxclient.AjaxClient;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Widget;

public class AjaxClientLCA extends AbstractWidgetLCA {
	private static final String WIDGET_ID = "org.codefaces.ui.widgets.ajaxclient.ajaxclient";

	@Override
	public void preserveValues(Widget widget) {
		//Do Nothing
	}

	@Override
	public void renderChanges(Widget widget) throws IOException {
		//Do Nothing
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
		// TODO Auto-generated method stub

	}

}
