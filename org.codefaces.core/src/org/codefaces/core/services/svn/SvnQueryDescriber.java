package org.codefaces.core.services.svn;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryDescriber;

public class SvnQueryDescriber implements SCMQueryDescriber {
	private static final SvnRepoQuery FETCH_REPO_QUERY = new SvnRepoQuery();
	private static final SvnFetchBranchesQuery FETCH_BRANCHES_QUERY = new SvnFetchBranchesQuery();
	private static final SvnFetchChildrenQuery FETCH_CHILDREN_QUERY = new SvnFetchChildrenQuery();
	private static final SvnFetchFileInfoQuery FETCH_FILEINFO_QUERY = new SvnFetchFileInfoQuery();
	

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
		return FETCH_FILEINFO_QUERY;
	}

}
