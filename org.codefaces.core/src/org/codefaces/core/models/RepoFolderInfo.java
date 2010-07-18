package org.codefaces.core.models;

import java.util.Collection;

import org.codefaces.core.services.SCMService;

public class RepoFolderInfo extends RepoResourceInfo {
	private Collection<RepoResource> children;

	public RepoFolderInfo(RepoResource context) {
		super(context);
	}

	@Override
	public Collection<RepoResource> getChildren() {
		if (children == null) {
			children = fetchChildren();
		}

		return children;
	}

	@Override
	public RepoFolder getContext() {
		return (RepoFolder) super.getContext();
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	private Collection<RepoResource> fetchChildren() {
		SCMService repoService = SCMService.getCurrent();
		return repoService.fetchChildren(getContext());
	}
}
