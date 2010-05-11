package org.codefaces.core.models;

import org.codefaces.core.services.RepoService;

public class RepoManager {
	private RepoService repoService;

	private static RepoManager INSTANCE;

	private RepoManager() {
		repoService = new RepoService();
	}

	public static RepoManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RepoManager();
		}

		return INSTANCE;
	}
	
	public RepoService getRepoService() {
		return repoService;
	}
}
