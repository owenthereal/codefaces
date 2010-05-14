package org.codefaces.core.models;

public class RepoBranch extends RepoContainer {
	private final Repo repo;

	public RepoBranch(String id, String name, Repo repo) {
		super(id, name, RepoResourceType.BRANCH, null);
		this.repo = repo;
	}

	public Repo getRepo() {
		return repo;
	}
}
