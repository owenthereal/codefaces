package org.codefaces.core.services;

import java.net.MalformedURLException;
import java.util.Set;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
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

	public Set<RepoBranch> listBranches(Repo repo)
			throws RepoConnectionException, RepoResponseException {
		return githubService.listGitHubBranches(repo);
	}

	public Set<RepoResource> listChildren(RepoContainer repoContainer)
			throws RepoResponseException, RepoConnectionException {
		return githubService.listGitHubChildren(repoContainer);
	}

	public RepoFile getRepoFile(RepoFileLite adaptableObject)
			throws RepoResponseException, RepoConnectionException {
		return githubService.getGitHubFile(adaptableObject);
	}
}
