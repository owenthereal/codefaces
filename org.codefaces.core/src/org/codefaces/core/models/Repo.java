package org.codefaces.core.models;

import java.net.MalformedURLException;

import org.codefaces.core.services.SCMService;

public class Repo extends RepoResource {
	private RepoCredential credential;

	private String kind;

	private RepoFolderRoot root;

	public Repo(String kind, String url, String name, RepoCredential credential) {
		super(null, null, url, name, RepoResourceType.REPO);
		this.kind = kind;
		this.credential = credential;
		this.root = new RepoFolderRoot(this, "/", "/");
	}

	public RepoFolderRoot getRoot() {
		return root;
	}

	public String getUrl() {
		return getId();
	}

	public RepoCredential getCredential() {
		return credential;
	}

	@Override
	protected RepoInfo getInfo() {
		return (RepoInfo) super.getInfo();
	}

	public static Repo create(String kind, String url)
			throws MalformedURLException {
		return SCMService.getCurrent().connect(kind, url);
	}

	public String getKind() {
		return kind;
	}
}
