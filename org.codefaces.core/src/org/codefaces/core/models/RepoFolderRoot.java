package org.codefaces.core.models;

public class RepoFolderRoot extends RepoFolder {
	public RepoFolderRoot(Repo repo, String id, String name) {
		super(null, repo, id, name, RepoResourceType.FOLDER_ROOT);
	}

	@Override
	public RepoFolderRoot getRoot() {
		return this;
	}

	public Repo getRepo() {
		return (Repo) getParent();
	}
}
