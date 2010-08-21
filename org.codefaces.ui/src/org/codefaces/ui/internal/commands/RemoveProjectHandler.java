package org.codefaces.ui.internal.commands;

import java.util.Iterator;

import org.codefaces.core.models.RepoProject;
import org.codefaces.core.models.RepoWorkspace;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class RemoveProjectHandler extends AbstractHandler {
	public static final String ID = "org.codefaces.ui.commands.removeProjectCommand";

	private static final String PARAM_VIEW_ID = "org.codefaces.ui.commands.removeProjectCommand.viewId";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		RepoProject project = getSelectedProject(event);
		if (project == null) {
			return null;
		}

		Shell shell = HandlerUtil.getActiveShell(event);
		boolean confirm = MessageDialog.openConfirm(shell, "Remove Project",
				"Are you sure you want to remove project '" + project.getName()
						+ "' from the workspace?");
		if (confirm) {
			RepoWorkspace.getCurrent().removeProject(project);
		}

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
