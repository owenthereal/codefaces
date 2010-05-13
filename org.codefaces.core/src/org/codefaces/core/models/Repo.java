package org.codefaces.core.models;


public class Repo extends RepoContainer {
	private String url;

	private RepoCredential credential;

	public Repo(String url, String name, RepoCredential credential) {
		super(url, name, RepoResourceType.REPO, null);
		this.url = url;
		this.credential = credential;
	}

	public String getUrl() {
		return url;
	}

	public RepoCredential getCredential() {
		return credential;
	}
}
