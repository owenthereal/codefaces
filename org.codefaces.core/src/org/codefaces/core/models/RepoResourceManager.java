package org.codefaces.core.models;

public class RepoResourceManager {
	private static final RepoResourceManager INSTANCE = new RepoResourceManager();

	public static RepoResourceManager getInstance() {
		return INSTANCE;
	}

	public RepoResourceInfo getInfo(RepoResource resource) {
		switch (resource.getType()) {
		case FOLDER:
		case FOLDER_ROOT:
			return new RepoFolderInfo(resource);
		case FILE:
			return new RepoFileInfo((RepoFile) resource);
		case REPO:
			return new RepoInfo((Repo) resource);
		}

		return new RepoResourceInfo(resource);
	}
}
