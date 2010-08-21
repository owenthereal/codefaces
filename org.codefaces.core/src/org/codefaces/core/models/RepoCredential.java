package org.codefaces.core.models;

public class RepoCredential {
	private String user;

	private String password;

	public RepoCredential(String user, String password) {
		this.user = user;
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}
