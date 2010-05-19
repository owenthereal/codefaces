package org.codefaces.core.services;

import java.net.MalformedURLException;
import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.github.GitHubService;

public class RepoService {
	private GitHubService githubService;

	public RepoService() {
		githubService = new GitHubService();
	}

	public Repo createRepo(String url) throws RepoConnectionException,
			RepoResponseException, MalformedURLException {
		String trimed_url = url.trim();
		if (trimed_url.endsWith("/")) {
			trimed_url = trimed_url.substring(0, trimed_url.length() - 1);
		}
		return githubService.createGithubRepo(trimed_url);
	}

	public Collection<RepoBranch> fetchBranches(Repo repo)
			throws RepoConnectionException, RepoResponseException {
		return githubService.fetchGitHubBranches(repo);
	}

	public Collection<RepoResource> fetchChildren(RepoResource repoContainer)
			throws RepoResponseException, RepoConnectionException {
		return githubService.fetchGitHubChildren(repoContainer);
	}

	public RepoFileInfo fetchFileInfo(RepoFile repoFile)
			throws RepoResponseException, RepoConnectionException {
		return githubService.fetchGitHubFileInfo(repoFile);
	}

	/**
	 * @return the default branch to be opened
	 * @param repo
	 *            the repository
	 */
	public RepoBranch getDefaultBranch(Repo repo) {
		try {
			return githubService.getGitHubDefaultBranch(repo);
		} catch (RepoResponseException e) {
			// there may be some repository provider allows the user to
			// remove/rename the default branch. In this case, we return null
			return null;
		}
	}

}
