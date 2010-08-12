package org.codefaces.ui.internal.commands;

import java.util.Iterator;

import org.codefaces.core.models.RepoProject;
import org.codefaces.ui.internal.dialogs.RepoPropertiesDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowRepoInfoDialogCommandHandler extends AbstractHandler {
	public static final String ID = "org.codefaces.ui.internal.commands.showRepoInfoDialogCommand";

	private static final String PARAM_VIEW_ID = "org.codefaces.ui.internal.commands.parameters.showRepoInfoCommand.viewId";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);
		RepoProject project = getSelectedProject(event);
		
		RepoPropertiesDialog dialog = new RepoPropertiesDialog(shell, project);
		dialog.open();
		
		return null;
	}

	private RepoProject getSelectedProject(ExecutionEvent event) {
		String viewId = event.getParameter(PARAM_VIEW_ID);
		if (viewId != null) {
			ISelectionService selectionService = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getSelectionService();
			ISelection selection = selectionService.getSelection(viewId);
			if (selection instanceof IStructuredSelection) {
				Iterator<?> elements = ((IStructuredSelection) selection)
						.iterator();
				while (elements.hasNext()) {
					Object element = elements.next();
					if (element instanceof RepoProject) {
						return (RepoProject) element;
					}
				}
			}
		}

		return null;
	}

}
