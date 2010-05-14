package org.codefaces.core.models;

public class RepoFolder extends RepoContainer {
	public RepoFolder(Repo repo, RepoContainer parent, String id, String name) {
		super(repo, parent, id, name, RepoResourceType.FOLDER);
	}
}
