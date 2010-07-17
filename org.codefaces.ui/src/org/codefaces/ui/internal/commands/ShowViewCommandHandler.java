package org.codefaces.ui.internal.commands;

import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ShowViewCommandHandler extends AbstractHandler {
	public static final String ID = "org.codefaces.ui.commands.showViewCommand";
	public static final String PARAM_VIEW_ID = "org.codefaces.ui.commands.parameters.showViewCommand.viewId";

	/**
	 * To show a view by providing a view id in the parameter
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String viewId = event.getParameter(PARAM_VIEW_ID);
		// The cmd is executed by providing a view ID
		if (viewId != null) {
			IWorkbenchPage activePage = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				activePage.showView(viewId);
			} catch (PartInitException e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when showing view " + viewId, e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}
		}
		return null;
	}

}
