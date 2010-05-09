package org.codefaces.core.models;

public class Repo {
	private String userName;
	private String repoName;

	public Repo(String userName, String repoName) {
		this.userName = userName;
		this.repoName = repoName;
	}

	public String getUserName() {
		return userName;
	}

	public String getRepoName() {
		return repoName;
	}
}
