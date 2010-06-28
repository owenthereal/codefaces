package org.codefaces.httpclient.ajax;

import org.codefaces.httpclient.ajax.internal.AjaxClientWidgetLCA;
import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.rwt.lifecycle.ILifeCycleAdapter;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class AjaxClientWidget extends Control  {
	private AjaxClient client;

	private AjaxClientWidgetLCA widgetLCA;

	public AjaxClientWidget() {
		this(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
	}

	public AjaxClientWidget(Shell shell) {
		super(shell, SWT.NONE);
		client = new AjaxClient(shell.getDisplay(), JSWriter.getWriterFor(this));
	}

	public AjaxClient getClient() {
		return client;
	}
	
	public static AjaxClientWidget getCurrent() {
		return (AjaxClientWidget) SessionSingletonBase
				.getInstance(AjaxClientWidget.class);
	}

	@SuppressWarnings("rawtypes")
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
