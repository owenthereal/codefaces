package org.codefaces.ui.viewers;

import org.codefaces.core.models.RepoResource;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class DefaultRepoResourceTreeViewContentProvider implements ITreeContentProvider {

	private QueuedAndCachedRepoResourceTreeViewManager manager;

	public void dispose() {
		if(manager != null){
			manager.dispose();
		}
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		if(manager != null){
			manager.dispose();
		}
		manager = new QueuedAndCachedRepoResourceTreeViewManager((TreeViewer) viewer);
	}

	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof RepoResource) {
			return ((RepoResource) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		return manager.getElement(parent);
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof RepoResource) {
			return ((RepoResource) parent).hasChildren();
		}

		return false;
	}
}