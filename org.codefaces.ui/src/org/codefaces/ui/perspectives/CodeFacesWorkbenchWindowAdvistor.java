package org.codefaces.ui.perspectives;

import java.util.HashMap;
import java.util.Map;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.CodeFacesUIActivator;
import org.codefaces.ui.commands.CommandExecutor;
import org.codefaces.ui.commands.OpenEditorHandler;
import org.codefaces.ui.editors.WelcomeEditor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class CodeFacesWorkbenchWindowAdvistor extends WorkbenchWindowAdvisor {

	public CodeFacesWorkbenchWindowAdvistor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowMenuBar(true);
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(true);
		getWindowConfigurer().setShellStyle(SWT.NO_TRIM | SWT.TITLE);
	}

	@Override
	public void postWindowCreate() {
		Shell shell = getWindowConfigurer().getWindow().getShell();
		shell.setMaximized(true);
	}

	@Override
	public void postWindowOpen() {
		showWelcomeEditor();
		openRepository();
	}

	private void openRepository() {
		String repoUrl = (String) RWT.getRequest().getParameter("repo");
		String branchName = (String) RWT.getRequest().getParameter("branch");

		if (repoUrl != null) {
			try {
				Repo repo = Repo.create(repoUrl);
				RepoBranch repoBranch = repo.getBranchByName(branchName);

				if (repoBranch == null) {
					repoBranch = repo.getMaster();
				}

				if (repoBranch != null) {
					Workspace.getCurrent().update(repoBranch);
				}

			} catch (Exception e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs when connecting to repository "
								+ repoUrl + " with branch " + branchName, e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}
		}
	}

	private void showWelcomeEditor() {
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put(OpenEditorHandler.PARAMETER_EDITOR_ID, WelcomeEditor.ID);
		CommandExecutor.execute(OpenEditorHandler.ID, paraMap, null);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new CodeFacesActionBarAdvisor(configurer);
	}
}
