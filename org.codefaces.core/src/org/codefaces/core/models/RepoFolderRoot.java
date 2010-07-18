package org.codefaces.core.models;

public class RepoFolderRoot extends RepoFolder {
	private RepoBranch branch;

	@Deprecated
	public RepoFolderRoot(RepoBranch branch, String id, String name) {
		super(null, branch, id, name, RepoResourceType.FOLDER_ROOT);
		this.branch = branch;
	}

	public RepoFolderRoot(Repo repo, String id, String name) {
		super(null, repo, id, name, RepoResourceType.FOLDER_ROOT);
	}

	@Override
	public RepoFolderRoot getRoot() {
		return this;
	}

	@Deprecated
	public RepoBranch getBranch() {
		return branch;
	}

	public Repo getRepo() {
		return (Repo) getParent();
	}
}
