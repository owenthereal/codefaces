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
}
