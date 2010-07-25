package org.codefaces.ui.internal.commands;

import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.codefaces.ui.internal.views.ProjectExplorerViewPart;
import org.codefaces.ui.internal.wizards.RepoSettings;
import org.codefaces.ui.internal.wizards.ImportRepoWizard;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class ImportRepoCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);

		ImportRepoWizard wizard = new ImportRepoWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);
		
		if (dialog.open() == Window.OK) {
			RepoSettings settings = wizard.getRepoSettings();
			RepoFolder baseDirectory = (RepoFolder) settings
					.get(RepoSettings.REPO_RESOURCE_INPUT);
			
			if (baseDirectory != null) {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().showView(
									ProjectExplorerViewPart.ID);
					Workspace.getCurrent().update(baseDirectory);
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
