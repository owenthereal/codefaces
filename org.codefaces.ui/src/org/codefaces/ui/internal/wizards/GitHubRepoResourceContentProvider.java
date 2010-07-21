package org.codefaces.ui.internal.wizards;

import org.codefaces.core.models.RepoResource;
import org.codefaces.ui.viewers.DefaultRepoResourceContentProvider;
import org.codefaces.ui.viewers.DefaultRepoResourceTreeViewManager;

public class GitHubRepoResourceContentProvider extends DefaultRepoResourceContentProvider{
	
	public GitHubRepoResourceContentProvider(
			DefaultRepoResourceTreeViewManager manager) {
		super(manager);
	}
	
	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof RepoResource) {
			RepoResource resource = (RepoResource)parent;
			//if the selected resource is a branch or tag, we stop
			return (!GitHubUtil.isBranchOrTag(resource) && resource.hasChildren());
		}

		return false;
	}
}
