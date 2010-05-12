package org.codefaces.core.services;

import java.util.Collections;
import java.util.Set;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoContainer;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileLite;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.github.GitHubService;

public class RepoService {
	private GitHubService githubService;

	public RepoService() {
		githubService = new GitHubService();
	}

	public Repo getRepo(String url) {
		return githubService.createGithubRepo(url);
	}

	public Set<RepoResource> listChildren(RepoContainer repoContainer) {
		Repo repo = (Repo) repoContainer.getAdapter(Repo.class);
		if (repo == null) {
			Collections.emptySet();
		}

		return githubService.listGitHubChildren(repo, repoContainer);
	}

	public RepoFile getRepoFile(RepoFileLite adaptableObject) {
		// TODO
		return null;
	}
}
