package org.codefaces.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.codefaces.core.CodeFacesCoreActivator;
import org.codefaces.httpclient.http.RepoResponseException;

public class RepoInfo extends RepoResourceInfo {
	private Collection<RepoBranch> branches;

	protected RepoInfo(Repo context) {
		super(context);
	}

	@Override
	public Repo getContext() {
		return (Repo) super.getContext();
	}

	public Collection<RepoBranch> getBranches() {
		if (branches == null) {
			branches = fetchBranches();
		}

		return branches;
	}

	private Collection<RepoBranch> fetchBranches() {
		try {
			return CodeFacesCoreActivator.getDefault().getRepoService()
					.fetchBranches(getContext());
		} catch (RepoResponseException e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	@Override
	public Collection<RepoResource> getChildren() {
		List<RepoResource> children = new ArrayList<RepoResource>();
		for (RepoBranch branch : getBranches()) {
			children.add(branch);
		}

		return children;
	}
}
