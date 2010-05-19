package org.codefaces.core.models;

import java.util.Collection;

public class Repo extends RepoResource {
	private RepoCredential credential;

	public Repo(String url, String name, RepoCredential credential) {
		super(null, null, url, name, RepoResourceType.REPO);
		this.credential = credential;
	}

	public String getUrl() {
		return getId();
	}

	public RepoCredential getCredential() {
		return credential;
	}

	public Collection<RepoBranch> getBranches() {
		return getInfo().getBranches();
	}

	@Override
	protected RepoInfo getInfo() {
		return (RepoInfo) super.getInfo();
	}
}
