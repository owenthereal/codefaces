package org.codefaces.core.github.internal.operations;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.dtos.GitHubResourceDTO;
import org.codefaces.core.github.internal.operations.dtos.GitHubResourcesDTO;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.connectors.SCMResponseException;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchChildrenOperationHandler implements SCMOperationHandler {
	private static final String SHOW_GITHUB_CHILDREN = "http://github.com/api/v2/json/tree/show";

	private static final String GITHUB_TYPE_BLOB = "blob";

	private static final String GITHUB_TYPE_TREE = "tree";

	@Override
	public Collection<RepoResource> execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object resPara = parameter.getParameter(PARA_REPO_RESOURCE);
		Assert.isTrue(resPara instanceof RepoResource);

		try {
			RepoResource container = (RepoResource) resPara;
			RepoFolderRoot root = container.getRoot();
			Repo repo = root.getRepo();

			String url = createFetchChildrenUrl(repo, container);
			GitHubResourcesDTO resourceDto = fetchChildrenDto(
					(GitHubConnector) connector, url);

			Set<RepoResource> children = new HashSet<RepoResource>();
			for (GitHubResourceDTO rscDto : resourceDto.getResources()) {
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

	protected GitHubResourcesDTO fetchChildrenDto(GitHubConnector connector,
			String url) {
		String respBody = connector.getResponseBody(url);
		return GitHubOperationUtil.fromJson(respBody, GitHubResourcesDTO.class);
	}

	protected String createFetchChildrenUrl(Repo repo, RepoResource resource) {
		return GitHubOperationUtil.makeURI(SHOW_GITHUB_CHILDREN, repo.getCredential()
				.getOwner(), repo.getName(), resource.getId());
	}
}
