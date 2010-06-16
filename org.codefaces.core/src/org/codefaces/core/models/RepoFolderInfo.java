package org.codefaces.core.models;

import java.util.Collection;

import org.codefaces.core.CodeFacesCoreActivator;
import org.codefaces.core.services.RepoService;

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
	public boolean hasChildren() {
		return true;
	}

	private Collection<RepoResource> fetchChildren() {
		RepoService repoService = CodeFacesCoreActivator.getDefault()
				.getRepoService();
		return repoService.fetchChildren(getContext());
	}
}
