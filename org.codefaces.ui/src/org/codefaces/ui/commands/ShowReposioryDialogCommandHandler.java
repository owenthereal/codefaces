package org.codefaces.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

public class ShowReposioryDialogCommandHandler extends AbstractHandler
		implements IHandler {

	private static final String SAMPLE_URL = "http://github.com/defunkt/facebox";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);
		String title = "Open Repository";
		String description = "Enter the GitHub Repository URL:\n" + "e.g. "
				+ SAMPLE_URL;

		InputDialog dlg = new InputDialog(shell, title, description, null, null);

		if (dlg.open() == Window.OK) {
			executeOpenRepositoryCommand(dlg.getValue());
		}
		return null;
	}

	private void executeOpenRepositoryCommand(String repoUrl) {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		ICommandService cmdService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);

		Command openRepositoryCmd = cmdService
				.getCommand(OpenRepositoryCommandHandler.ID);
		try {
			IParameter repoUrlParam = openRepositoryCmd
					.getParameter(OpenRepositoryCommandHandler.PARAM_REPO_URL_ID);
			Parameterization paramRepoUrl = new Parameterization(repoUrlParam,
					repoUrl);
			ParameterizedCommand parmCommand = new ParameterizedCommand(
					openRepositoryCmd, new Parameterization[] { paramRepoUrl });
			handlerService.executeCommand(parmCommand, null);

		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnabledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotHandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
