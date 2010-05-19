package org.codefaces.core.models;

public class RepoFolderRoot extends RepoResource {
	private final RepoBranch branch;

	public RepoFolderRoot(RepoBranch branch, RepoResource parent, String id,
			String name) {
		super(null, parent, id, name, RepoResourceType.FOLDER_ROOT);
		this.branch = branch;
	}

	@Override
	public RepoFolderRoot getRoot() {
		return this;
	}

	public RepoBranch getBranch() {
		return branch;
	}
}
