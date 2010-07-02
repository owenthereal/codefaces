package org.codefaces.core.services;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;

public interface SCMQueryCreator {
	SCMQuery<Repo> createFetchRepoQuery();

	SCMQuery<Collection<RepoBranch>> createFetchBranchesQuery();

	SCMQuery<Collection<RepoResource>> CreateFetchChildrenQuery();

	SCMQuery<RepoFileInfo> createFetchFileInfoQuery();
}
