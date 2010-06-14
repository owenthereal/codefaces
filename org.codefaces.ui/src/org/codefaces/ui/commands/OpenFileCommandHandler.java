package org.codefaces.ui.commands;

import java.util.Iterator;

import org.codefaces.core.models.RepoFile;
import org.codefaces.ui.views.CodeExplorerViewPart;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class OpenFileCommandHandler extends AbstractHandler {
	public static final String PARAM_VIEW_ID = "org.codefaces.ui.commands.parameters.openFileCommand.viewId";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String viewId = event.getParameter(PARAM_VIEW_ID);
		//The cmd is executed by providing a view ID
		if(viewId != null){
			ISelectionService selectionService = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getSelectionService();
			ISelection selection = selectionService.getSelection(viewId);
			if(selection != null && selection instanceof IStructuredSelection){
				Iterator elements = ((IStructuredSelection) selection).iterator();
				while(elements.hasNext()){
					Object element = elements.next();
					if(element instanceof RepoFile){
						openFile((RepoFile)element);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Open the given file in the code explorer
	 * @param repoFile - the file 
	 */
	private void openFile(RepoFile repoFile) {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			IViewPart viewPart = activePage.showView(CodeExplorerViewPart.ID,
					repoFile.getId(), IWorkbenchPage.VIEW_ACTIVATE);
			((CodeExplorerViewPart) viewPart).setInput(repoFile);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

}
