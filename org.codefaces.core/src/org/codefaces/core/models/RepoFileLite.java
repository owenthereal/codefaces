package org.codefaces.core.models;

public class RepoFileLite extends RepoResource {
	public RepoFileLite(Repo repo, RepoContainer parent, String id, String name) {
		super(repo, parent, id, name, RepoResourceType.FILE);
	}

}
