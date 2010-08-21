package org.codefaces.ui.internal.commons;

import org.codefaces.core.models.RepoFolder;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class RepoFolderViewFilter extends ViewerFilter {
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return element instanceof RepoFolder || element instanceof LoadingItem;
	}
}
