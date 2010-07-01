package org.codefaces.core.services;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.github.GitHubHttpQueryDescriber;
import org.codefaces.httpclient.SCMHttpClient;
import org.codefaces.httpclient.SCMResponseException;
import org.codefaces.httpclient.ajax.AjaxClientAdapter;
import org.eclipse.rwt.SessionSingletonBase;

public class SCMService {
	private SCMQueryDescriber queryDescriber;

	private SCMHttpClient httpClient;

	public SCMService() {
		httpClient = new AjaxClientAdapter();
		queryDescriber = new GitHubHttpQueryDescriber();
	}

	public Repo createRepo(String url) {
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_URL, url.trim());

		return execute(queryDescriber.getFetchRepoQuery(), para);
	}

	public void dispose() {
		httpClient.dispose();
	}

	public Collection<RepoBranch> fetchBranches(Repo repo) {
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO, repo);

		return execute(queryDescriber.getFetchBranchesQuery(), para);
	}

	public Collection<RepoResource> fetchChildren(RepoResource parent)
			throws SCMResponseException {
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO_RESOURCE, parent);

		return execute(queryDescriber.getFetchChildrenQuery(), para);
	}

	public RepoFileInfo fetchFileInfo(RepoFile file)
			throws SCMResponseException {
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO_FOLDER, file.getParent());
		para.addParameter(SCMQuery.PARA_REPO_FILE_NAME, file.getName());

		return execute(queryDescriber.getFetchFileInfoQuery(), para);
	}

	public static SCMService getCurrent() {
		return (SCMService) SessionSingletonBase.getInstance(SCMService.class);
	}

	private <T> T execute(SCMQuery<T> query, SCMQueryParameter parameter) {
		return query.execute(httpClient, parameter);
	}
}
