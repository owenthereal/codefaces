package org.codefaces.ui.viewers;

import org.codefaces.core.models.RepoResource;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DefaultRepoResourceContentProvider implements ITreeContentProvider {

	private DefaultRepoResourceTreeViewManager manager;

	public DefaultRepoResourceContentProvider(DefaultRepoResourceTreeViewManager manager){
		this.manager = manager;
	}
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
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