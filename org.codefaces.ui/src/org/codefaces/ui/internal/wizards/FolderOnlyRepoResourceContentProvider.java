package org.codefaces.ui.internal.wizards;

import org.codefaces.core.models.RepoFolder;
import org.codefaces.ui.internal.commons.RepoResourceTreeContentProvider;

public class FolderOnlyRepoResourceContentProvider extends
		RepoResourceTreeContentProvider {

	@Override
	public boolean hasChildren(Object parent) {
		return (parent instanceof RepoFolder);
	}
}
