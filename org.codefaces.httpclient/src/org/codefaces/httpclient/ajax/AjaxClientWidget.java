package org.codefaces.httpclient.ajax;

import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AjaxClientWidget extends Control {
	private AjaxClient client;

	public AjaxClientWidget() {
		this(Display.getCurrent().getActiveShell());
	}

	public AjaxClientWidget(Shell shell) {
		super(shell, SWT.NONE);
		client = new AjaxClient(getDisplay(), JSWriter.getWriterFor(this));
	}

	public AjaxClient getClient() {
		return client;
	}

	public static AjaxClientWidget getCurrent() {
		return (AjaxClientWidget) SessionSingletonBase
				.getInstance(AjaxClientWidget.class);
	}
}
