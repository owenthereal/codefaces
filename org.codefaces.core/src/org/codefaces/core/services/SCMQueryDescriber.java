package org.codefaces.core.services;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;

public interface SCMQueryDescriber {
	SCMQuery<Repo> getFetchRepoQuery();

	SCMQuery<Collection<RepoBranch>> getFetchBranchesQuery();

	SCMQuery<Collection<RepoResource>> getFetchChildrenQuery();

	SCMQuery<RepoFileInfo> getFetchFileInfoQuery();
}
