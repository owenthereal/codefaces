package org.codefaces.core.services.github;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryDescriber;

public class GitHubQueryDescriber implements SCMQueryDescriber {
	private static final GitHubFetchFileInfoQuery FETCH_FILE_INFO_QUERY = new GitHubFetchFileInfoQuery();

	private static final GitHubFetchChildrenQuery FETCH_CHILDREN_QUERY = new GitHubFetchChildrenQuery();

	private static final GitHubFetchBranchesQuery FETCH_BRANCHES_QUERY = new GitHubFetchBranchesQuery();
	
	private static final GitHubRepoQuery FETCH_REPO_QUERY = new GitHubRepoQuery();

	@Override
	public SCMQuery<Repo> getFetchRepoQuery() {
		return FETCH_REPO_QUERY;
	}

	@Override
	public SCMQuery<Collection<RepoBranch>> getFetchBranchesQuery() {
		return FETCH_BRANCHES_QUERY;
	}

	@Override
	public SCMQuery<Collection<RepoResource>> getFetchChildrenQuery() {
		return FETCH_CHILDREN_QUERY;
	}

	@Override
	public SCMQuery<RepoFileInfo> getFetchFileInfoQuery() {
		return FETCH_FILE_INFO_QUERY;
	}
}
