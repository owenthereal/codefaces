package org.codefaces.core.models;

import java.util.ArrayList;
import java.util.Collection;

public class RepoBranch extends RepoResource {
	private final RepoFolderRoot root;
	
	private final Repo repo;

	public RepoBranch(Repo repo, String id, String name) {
		super(null, repo, id, name, RepoResourceType.BRANCH);
		this.repo = repo;
		root = new RepoFolderRoot(this, this, id, name);
	}

	@Override
	public RepoFolderRoot getRoot() {
		return root;
	}

	public Repo getRepo() {
		return repo;
	}

	@Override
	public Collection<RepoResource> getChildren() {
		Collection<RepoResource> children = new ArrayList<RepoResource>();
		children.add(getRoot());

		return children;
	}
}
