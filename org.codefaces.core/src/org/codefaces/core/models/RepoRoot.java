package org.codefaces.core.models;

import java.util.List;

public class RepoRoot extends RepoItem {

	public RepoRoot(RepoItem parent, List<RepoItem> mockRepoItems, RepoType repoType,
			RepoMetaData metaData) {
		super(parent, mockRepoItems, repoType, metaData);
	}

}
