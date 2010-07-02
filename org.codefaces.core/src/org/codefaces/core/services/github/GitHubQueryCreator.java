package org.codefaces.core.services.github;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryCreator;

public class GitHubQueryCreator implements SCMQueryCreator {
	@Override
	public SCMQuery<Repo> createFetchRepoQuery() {
		return new GitHubRepoQuery();
	}

	@Override
	public SCMQuery<Collection<RepoBranch>> createFetchBranchesQuery() {
		return new GitHubFetchBranchesQuery();
	}

	@Override
	public SCMQuery<Collection<RepoResource>> CreateFetchChildrenQuery() {
		return new GitHubFetchChildrenQuery();
	}

	@Override
	public SCMQuery<RepoFileInfo> createFetchFileInfoQuery() {
		return new GitHubFetchFileInfoQuery();
	}
}
