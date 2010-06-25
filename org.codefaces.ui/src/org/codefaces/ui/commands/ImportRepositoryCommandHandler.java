package org.codefaces.ui.commands;

import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.dialogs.RepoUrlInputDialog;
import org.codefaces.ui.views.ProjectExplorerViewPart;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class ImportRepositoryCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);

		RepoUrlInputDialog dlg = new RepoUrlInputDialog(shell);

		if (dlg.open() == Window.OK) {
			RepoBranch branch = dlg.getSelectedBranch();
			if (branch != null) {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().showView(
									ProjectExplorerViewPart.ID);
					Workspace.getCurrent().update(branch);
				} catch (PartInitException e) {
					IStatus status = new Status(Status.ERROR,
							CodeFacesUIActivator.PLUGIN_ID,
							"Errors occurs when openning view "
									+ ProjectExplorerViewPart.ID, e);
					CodeFacesUIActivator.getDefault().getLog().log(status);
				}
			}
		}

		return null;
	}
}
