package org.codefaces.core.models;

public class RepoFolderRoot extends RepoFolder {
	public RepoFolderRoot(Repo repo, String id, String name) {
		super(null, repo, id, name, RepoResourceType.FOLDER_ROOT);
	}
	
	//This method is intended to use only in testing
	protected RepoFolderRoot(Repo repo, String id, String name, RepoFolderInfo info) {
		super(null, repo, id, name, RepoResourceType.FOLDER_ROOT, info);
	}

	@Override
	public RepoFolderRoot getRoot() {
		return this;
	}

	public Repo getRepo() {
		return (Repo) getParent();
	}
}
