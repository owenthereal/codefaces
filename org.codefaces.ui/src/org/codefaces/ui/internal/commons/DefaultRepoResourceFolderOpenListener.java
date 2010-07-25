package org.codefaces.ui.internal.commons;

import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class DefaultRepoResourceFolderOpenListener implements IOpenListener {
	@Override
	public void open(OpenEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (selection.isEmpty()
				|| !(selection.getFirstElement() instanceof RepoResource)) {
			return;
		}

		RepoResource clickedRepoResource = (RepoResource) selection
				.getFirstElement();
		Viewer viewer = event.getViewer();
		if (clickedRepoResource instanceof RepoFolder
				&& viewer instanceof TreeViewer) {
			TreeViewer treeViewer = (TreeViewer) viewer;
			if (treeViewer.isExpandable(clickedRepoResource)) {
				treeViewer.setExpandedState(clickedRepoResource,
						!treeViewer.getExpandedState(clickedRepoResource));
			}
		}
	}
}
