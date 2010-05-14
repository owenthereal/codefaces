package org.codefaces.core.services;

import java.util.Collections;
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

	public Set<RepoResource> listChildren(RepoContainer repoContainer)
			throws RepoResponseException, RepoConnectionException {
		Repo repo = (Repo) repoContainer.getAdapter(Repo.class);
		if (repo == null) {
			Collections.emptySet();
		}

		return githubService.listGitHubChildren(repo, repoContainer);
	}

	/**
	 * @return the default branch to be opened
	 * @param repo the repository 
	 */
	public RepoBranch getDefaultBranch(Repo repo){
		try {
			return githubService.getGitHubDefaultBranch(repo);
		} catch (RepoResponseException e) {
			//there may be some repository provider allows the user to 
			//remove/rename the default branch. In this case, we return null
			return null;
		}
	}
	
	
	public RepoFile getRepoFile(RepoFileLite adaptableObject) {
		// TODO
		return null;
	}
}
