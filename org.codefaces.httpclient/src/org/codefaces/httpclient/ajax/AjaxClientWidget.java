package org.codefaces.httpclient.ajax;

import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

public class AjaxClientWidget extends Widget {
	private AjaxClient client;

	public AjaxClientWidget() {
		super(PlatformUI.createDisplay().getActiveShell(), SWT.NONE);
		client = new AjaxClient(getDisplay(), JSWriter.getWriterFor(this));
	}
	
	public AjaxClient getClient() {
		return client;
	}
}
