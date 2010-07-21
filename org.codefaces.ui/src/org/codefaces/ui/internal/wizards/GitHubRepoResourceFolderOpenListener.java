package org.codefaces.ui.internal.wizards;

import org.codefaces.core.models.RepoResource;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class GitHubRepoResourceFolderOpenListener implements IOpenListener {
	
	@Override
	public void open(OpenEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (selection.isEmpty()) {
			return;
		}

		RepoResource clickedRepoResource = (RepoResource) selection
				.getFirstElement();
		Viewer viewer = event.getViewer();
		//if the selected resource is a branch or tag, we stop
		if (!GitHubUtil.isBranchOrTag(clickedRepoResource) 
				&& viewer instanceof TreeViewer) {
			
			TreeViewer treeViewer = (TreeViewer) viewer;
			if (treeViewer.isExpandable(clickedRepoResource)) {
				treeViewer.setExpandedState(clickedRepoResource,
						!treeViewer.getExpandedState(clickedRepoResource));
			}
		}
	}
}