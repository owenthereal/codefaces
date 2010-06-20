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
import org.eclipse.rwt.SessionSingletonBase;

public class RepoService {
	private GitHubService githubService;
	private CodeFacesHttpClient httpClient;

	public RepoService() {
		httpClient = new AjaxClientDelegate();
		githubService = new GitHubService(httpClient);
	}

	public Repo createRepo(String url) throws MalformedURLException {
		String trimed_url = url.trim();
		return getServiceInternal().createGithubRepo(trimed_url);
	}

	public void dispose() {
		httpClient.dispose();
	}

	private GitHubService getServiceInternal() {
		return githubService;
	}

	public Collection<RepoBranch> fetchBranches(Repo repo)
			throws RepoResponseException {
		return getServiceInternal().fetchGitHubBranches(repo);
	}

	public Collection<RepoResource> fetchChildren(RepoResource repoContainer)
			throws RepoResponseException {
		return getServiceInternal().fetchGitHubChildren(repoContainer);
	}

	public RepoFileInfo fetchFileInfo(RepoFile repoFile)
			throws RepoResponseException {
		return getServiceInternal().fetchGitHubFileInfo(repoFile);
	}

	public RepoBranch getDefaultBranch(Repo repo) {
		return getServiceInternal().getGitHubDefaultBranch(repo);
	}

	public static RepoService getCurrent() {
		return (RepoService) SessionSingletonBase
				.getInstance(RepoService.class);
	}

}
