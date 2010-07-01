package org.codefaces.core.models;

public class RepoFolderRoot extends RepoFolder {
	private final RepoBranch branch;

	public RepoFolderRoot(RepoBranch branch, String id, String name) {
		super(null, branch, id, name, RepoResourceType.FOLDER_ROOT);
		this.branch = branch;
	}

	@Override
	public RepoFolderRoot getRoot() {
		return this;
	}

	public RepoBranch getBranch() {
		return branch;
	}

	public Repo getRepo() {
		return getBranch().getRepo();
	}
}
