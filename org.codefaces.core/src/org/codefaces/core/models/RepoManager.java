package org.codefaces.core.models;

import org.codefaces.core.services.RepoService;

public class RepoManager {
	private RepoService repoService;

	private static RepoManager INSTANCE = new RepoManager();

	private RepoManager() {
		repoService = new RepoService();
	}

	public static RepoManager getInstance() {
		return INSTANCE;
	}

	public RepoService getRepoService() {
		return repoService;
	}
}
