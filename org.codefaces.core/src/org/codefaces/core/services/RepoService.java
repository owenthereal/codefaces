package org.codefaces.core.services;

import java.util.Set;

import org.codefaces.core.models.Repo;
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

	public Set<RepoResource> listChildren(RepoResource repoResource) {
		// TODO
		return null;
	}
}
