package org.codefaces.core.models;

public class RepoInfo extends RepoResourceInfo {
	protected RepoInfo(Repo context) {
		super(context);
	}

	@Override
	public Repo getContext() {
		return (Repo) super.getContext();
	}

	@Override
	public boolean hasChildren() {
		return true;
	}
}
