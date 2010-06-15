package org.codefaces.core.services;

import java.net.MalformedURLException;
import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.github.GitHubService;
import org.codefaces.httpclient.CodeFacesHttpClient;
import org.codefaces.httpclient.RepoResponseException;
import org.codefaces.httpclient.ajax.AjaxClientDelegate;

public class RepoService {
	private GitHubService githubService;
	private CodeFacesHttpClient httpClient;

	public RepoService() {
		httpClient = new AjaxClientDelegate();
		githubService = new GitHubService(httpClient);
	}

	public Repo createRepo(String url) throws RepoResponseException,
			RepoResponseException, MalformedURLException {
		String trimed_url = url.trim();
		if (trimed_url.endsWith("/")) {
			trimed_url = trimed_url.substring(0, trimed_url.length() - 1);
		}
		return getServiceInternal().createGithubRepo(trimed_url);
	}
	
	public void dispose() {
		httpClient.dispose();
	}

	private GitHubService getServiceInternal() {
		return githubService;
	}

	public Collection<RepoBranch> fetchBranches(Repo repo)
			throws RepoResponseException, RepoResponseException {
		return getServiceInternal().fetchGitHubBranches(repo);
	}

	public Collection<RepoResource> fetchChildren(RepoResource repoContainer)
			throws RepoResponseException, RepoResponseException {
		return getServiceInternal().fetchGitHubChildren(repoContainer);
	}

	public RepoFileInfo fetchFileInfo(RepoFile repoFile)
			throws RepoResponseException, RepoResponseException {
		return getServiceInternal().fetchGitHubFileInfo(repoFile);
	}

	/**
	 * @return the default branch to be opened
	 * @param repo
	 *            the repository
	 */
	public RepoBranch getDefaultBranch(Repo repo) {
		try {
			return getServiceInternal().getGitHubDefaultBranch(repo);
		} catch (RepoResponseException e) {
			// there may be some repository provider allows the user to
			// remove/rename the default branch. In this case, we return null
			return null;
		}
	}

}
