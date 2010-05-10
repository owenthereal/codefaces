package org.codefaces.core.models;

public class RepoCredential {
	private String owner;
	private String user;
	private String password;
	
	public RepoCredential() {
	}
	
	public RepoCredential(String owner, String user, String password){
		this.owner = owner;
		this.user = user;
		this.password = password;
	}
	
	public String getOwner() {
		return owner;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}

	
	
}
