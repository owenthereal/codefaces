package org.codefaces.core.services;

import java.util.Collections;
import java.net.MalformedURLException;
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

	public Repo getRepo(String url) throws RepoConnectionException,
			RepoResponseException, MalformedURLException {
		return githubService.createGithubRepo(url);
	}

	public Set<RepoResource> listChildren(RepoContainer repoContainer)
			throws RepoResponseException, RepoConnectionException {
		Repo repo = (Repo) repoContainer.getAdapter(Repo.class);
		if (repo == null) {
			Collections.emptySet();
		}

		return githubService.listGitHubChildren(repo, repoContainer);
	}

	public RepoFile getRepoFile(RepoFileLite adaptableObject)
			throws RepoResponseException, RepoConnectionException {
		Repo repo = (Repo) adaptableObject.getAdapter(Repo.class);
		if (repo == null) {
			return null;
		}

		return githubService.getGitHubFile(repo, adaptableObject);
	}
}
