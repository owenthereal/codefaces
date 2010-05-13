package org.codefaces.core.models;

public class RepoFileLite extends RepoResource {

	public RepoFileLite(String id, String name, RepoContainer parent) {
		super(id, name, RepoResourceType.FILE, parent);
	}

}
