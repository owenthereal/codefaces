package org.codefaces.ui.internal.wizards;

import org.codefaces.core.models.RepoFolder;
import org.codefaces.ui.viewers.DefaultRepoResourceTreeViewContentProvider;

public class FolderOnlyRepoResourceContentProvider extends DefaultRepoResourceTreeViewContentProvider{

	
	@Override
	public boolean hasChildren(Object parent) {
		return (parent instanceof RepoFolder);
	}
}
