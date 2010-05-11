package org.codefaces.core.models;

import java.util.Collection;
import java.util.Collections;

public class Repo extends RepoResource {
	private String url;
	
	private Collection<RepoBranch> branches;
	
	private RepoCredential credential; 

	public Repo(String url, String name, Collection<RepoBranch> branches, RepoCredential credential) {
		super(url, name, RepoResourceType.REPO, null);
		this.url = url;
		this.branches = branches;
		this.credential =  credential;
	}

	public Collection<RepoBranch> getBranches() {
		return Collections.unmodifiableCollection(this.branches);
	}
	
	public String getUrl() {
		return url;
	}
	
	public RepoCredential getCredential(){
		return credential;
	}
}
