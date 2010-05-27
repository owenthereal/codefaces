package org.codefaces.ui.commands;

import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.Workspace;
import org.codefaces.ui.dialogs.RepoUrlInputDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class ImportRepositoryCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);

		RepoUrlInputDialog dlg = new RepoUrlInputDialog(shell);

		if (dlg.open() == Window.OK) {
			RepoBranch branch = dlg.getSelectedBranch();
			if (branch != null) {
				Workspace.getCurrent().update(branch);
			}
		}

		return null;
	}

	// private void executeOpenRepositoryCommand(String repoUrl, String
	// branchId) {
	// IHandlerService handlerService = (IHandlerService) PlatformUI
	// .getWorkbench().getService(IHandlerService.class);
	// ICommandService cmdService = (ICommandService) PlatformUI
	// .getWorkbench().getService(ICommandService.class);
	//
	// Command openRepositoryCmd = cmdService
	// .getCommand(OpenRepositoryCommandHandler.ID);
	// try {
	// IParameter repoUrlParam = openRepositoryCmd
	// .getParameter(OpenRepositoryCommandHandler.PARAM_REPO_URL_ID);
	// Parameterization paramRepoUrl = new Parameterization(repoUrlParam,
	// repoUrl);
	// ParameterizedCommand parmCommand = new ParameterizedCommand(
	// openRepositoryCmd, new Parameterization[] { paramRepoUrl });
	// handlerService.executeCommand(parmCommand, null);
	//
	// } catch (NotDefinedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (ExecutionException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (NotEnabledException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (NotHandledException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
