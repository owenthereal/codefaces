package org.codefaces.core.services;

import java.net.MalformedURLException;
import java.util.Set;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileLite;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.github.GitHubService;

public class RepoService {
	private GitHubService githubService;

	public RepoService() {
		githubService = new GitHubService();
	}

	public Repo getRepo(String url) throws RepoConnectionException,
			RepoResponseException, MalformedURLException {
		return githubService.createGithubRepo(url);
	}

	public Set<RepoResource> listChildren(RepoResource repoResource) {
		// TODO
		return null;
	}

	public RepoFile getRepoFile(RepoFileLite adaptableObject) {
		// TODO
		return null;
	}
}
