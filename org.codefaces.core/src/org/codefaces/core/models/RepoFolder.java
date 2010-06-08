package org.codefaces.core.models;

public class RepoFolder extends RepoResource {
	public RepoFolder(RepoFolderRoot root, RepoResource parent, String id,
			String name) {
		super(root, parent, id, name, RepoResourceType.FOLDER);
	}

	@Override
	protected RepoFolderInfo getInfo() {
		return (RepoFolderInfo) super.getInfo();
	}

}
