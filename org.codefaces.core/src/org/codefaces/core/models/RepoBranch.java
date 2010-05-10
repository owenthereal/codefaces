package org.codefaces.core.models;

public class RepoBranch extends RepoContainer {
	public RepoBranch(String id, String name, Repo repo) {
		super(id, name, RepoResourceType.BRANCH, repo);
	}
}
