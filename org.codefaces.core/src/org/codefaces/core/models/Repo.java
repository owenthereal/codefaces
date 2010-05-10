package org.codefaces.core.models;

import java.util.Collection;
import java.util.Collections;

public class Repo extends RepoResource {
	private String url;
	
	private Collection<RepoBranch> branches;

	public Repo(String url, Collection<RepoBranch> branches) {
		super(url, url, RepoResourceType.REPO, null);
		this.url = url;
		this.branches = branches;
	}

	public Collection<RepoBranch> getBranches() {
		return Collections.unmodifiableCollection(this.branches);
	}
	
	public String getUrl() {
		return url;
	}
}
