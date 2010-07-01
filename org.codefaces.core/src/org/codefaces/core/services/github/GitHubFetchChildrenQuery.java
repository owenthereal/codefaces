package org.codefaces.core.services.github;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.core.services.github.dtos.GitHubResourceDto;
import org.codefaces.core.services.github.dtos.GitHubResourcesDto;
import org.codefaces.httpclient.SCMHttpClient;
import org.codefaces.httpclient.SCMResponseException;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchChildrenQuery implements
		SCMQuery<Collection<RepoResource>> {
	private static final String SHOW_GITHUB_CHILDREN = "http://github.com/api/v2/json/tree/show";

	private static final String GITHUB_TYPE_BLOB = "blob";

	private static final String GITHUB_TYPE_TREE = "tree";

	@Override
	public Collection<RepoResource> execute(SCMHttpClient client,
			SCMQueryParameter parameter) {
		Object resPara = parameter.getParameter(PARA_REPO_RESOURCE);
		Assert.isTrue(resPara instanceof RepoResource);

		try {
			RepoResource container = (RepoResource) resPara;
			RepoFolderRoot root = container.getRoot();
			Repo repo = root.getRepo();

			String url = createFetchChildrenUrl(repo, container);
			GitHubResourcesDto resourceDto = fetchChildrenDto(client, url);

			Set<RepoResource> children = new HashSet<RepoResource>();
			for (GitHubResourceDto rscDto : resourceDto.getResources()) {
				String type = rscDto.getType();
				RepoResource child = createRepoResourceFromType(type, root,
						container, rscDto.getSha(), rscDto.getName());
				if (child == null) {
					throw new UnsupportedOperationException(
							"Unknown github resource type: " + type);
				}

				children.add(child);
			}

			return children;
		} catch (Exception e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}

	private RepoResource createRepoResourceFromType(String type,
			RepoFolderRoot root, RepoResource parent, String id, String name) {
		if (StringUtils.equals(GITHUB_TYPE_BLOB, type)) {
			return new RepoFile(root, parent, id, name);
		}

		if (StringUtils.equals(GITHUB_TYPE_TREE, type)) {
			return new RepoFolder(root, parent, id, name);
		}

		return null;
	}

	protected GitHubResourcesDto fetchChildrenDto(SCMHttpClient client, String url) {
		String respBody = client.getResponseBody(url);
		return GitHubUtil.fromJson(respBody, GitHubResourcesDto.class);
	}

	protected String createFetchChildrenUrl(Repo repo, RepoResource resource) {
		return GitHubUtil.makeURI(SHOW_GITHUB_CHILDREN, repo.getCredential()
				.getOwner(), repo.getName(), resource.getId());
	}
}
