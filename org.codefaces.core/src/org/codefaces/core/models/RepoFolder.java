package org.codefaces.core.models;

public class RepoFolder extends RepoContainer {
	public RepoFolder(String id, String name, RepoContainer parent) {
		super(id, name, RepoResourceType.FOLDER, parent);
	}
}
