package org.codefaces.httpclient.ajax;

import org.codefaces.httpclient.ajax.internal.AjaxClientWidgetLCA;
import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.rwt.lifecycle.ILifeCycleAdapter;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AjaxClientWidget extends Control  {
	private AjaxClient client;

	private AjaxClientWidgetLCA widgetLCA;

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

	@Override
	public Object getAdapter(Class adapter) {
		Object result;
		if (adapter == ILifeCycleAdapter.class) {
			if (widgetLCA == null) {
				widgetLCA = new AjaxClientWidgetLCA();
			}

			result = widgetLCA;
		} else {
			result = super.getAdapter(adapter);
		}
		return result;
	}

}
