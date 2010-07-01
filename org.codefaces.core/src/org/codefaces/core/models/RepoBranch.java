package org.codefaces.core.models;

import java.util.ArrayList;
import java.util.Collection;

public class RepoBranch extends RepoResource {
	private static final String AT = "@";

	private final RepoFolderRoot root;

	private final Repo repo;

	private final boolean isMaster;

	public RepoBranch(Repo repo, String id, String name, boolean isMaster) {
		super(null, repo, id, name, RepoResourceType.BRANCH);
		this.repo = repo;
		this.isMaster = isMaster;

		root = new RepoFolderRoot(this, id, repo.getName() + AT + repo.getUrl());
	}

	public boolean isMaster() {
		return isMaster;
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
