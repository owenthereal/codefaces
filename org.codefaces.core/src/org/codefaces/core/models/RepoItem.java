package org.codefaces.core.models;

import java.util.Collections;
import java.util.List;

public class RepoItem {
	private RepoItem parent;

	private List<RepoItem> children;

	private RepoType repoType;

	private RepoMetaData metaData;

	public RepoItem(RepoItem parent, List<RepoItem> mockRepoItems,
			RepoType repoType, RepoMetaData metaData) {
		this.parent = parent;
		this.children = mockRepoItems;
		this.repoType = repoType;
		this.metaData = metaData;
	}

	public RepoItem getParent() {
		return parent;
	}

	public List<RepoItem> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public RepoType getRepoType() {
		return repoType;
	}

	public RepoMetaData getMetaData() {
		return metaData;
	}
}
