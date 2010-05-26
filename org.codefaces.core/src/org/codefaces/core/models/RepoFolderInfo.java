package org.codefaces.core.models;

import java.util.Collection;

import org.codefaces.core.CodeFacesCoreActivator;
import org.codefaces.core.services.RepoResponseException;
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

	private Collection<RepoResource> fetchChildren() {
		RepoService repoService = CodeFacesCoreActivator.getDefault()
				.getRepoService();
		try {
			return repoService.fetchChildren(getContext());
		} catch (RepoResponseException e) {
			e.printStackTrace();
		} 

		return EMPTY_CHILDREN;
	}
}
