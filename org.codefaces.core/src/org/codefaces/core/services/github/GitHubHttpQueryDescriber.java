package org.codefaces.core.services.github;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryDescriber;

public class GitHubHttpQueryDescriber implements SCMQueryDescriber {

	private static final GitHubFetchFileInfoQuery FILE_INFO_QUERY = new GitHubFetchFileInfoQuery();

	private static final GitHubFetchChildrenQuery CHILDREN_QUERY = new GitHubFetchChildrenQuery();

	private static final GitHubFetchBranchesQuery BRANCHES_QUERY = new GitHubFetchBranchesQuery();

	private static final GitHubRepoQuery REPO_QUERY = new GitHubRepoQuery();

	@Override
	public SCMQuery<Repo> getFetchRepoQuery() {
		return REPO_QUERY;
	}

	@Override
	public SCMQuery<Collection<RepoBranch>> getFetchBranchesQuery() {
		return BRANCHES_QUERY;
	}

	@Override
	public SCMQuery<Collection<RepoResource>> getFetchChildrenQuery() {
		return CHILDREN_QUERY;
	}

	@Override
	public SCMQuery<RepoFileInfo> getFetchFileInfoQuery() {
		return FILE_INFO_QUERY;
	}
}
