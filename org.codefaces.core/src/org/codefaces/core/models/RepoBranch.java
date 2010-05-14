package org.codefaces.core.models;

public class RepoBranch extends RepoContainer {
	public RepoBranch(Repo repo, String id, String name) {
		super(repo, null, id, name, RepoResourceType.BRANCH);
	}
}
