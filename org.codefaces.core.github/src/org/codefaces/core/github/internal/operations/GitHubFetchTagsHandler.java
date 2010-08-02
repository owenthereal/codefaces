package org.codefaces.core.github.internal.operations;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.dtos.GitHubTagsDTO;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchTagsHandler implements SCMOperationHandler {
	private static final String SHOW_GITHUB_TAGS = "http://github.com/api/v2/json/repos/show";

	public static final String ID = "org.codefaces.core.operations.SCMOperation.github.fetchTags";

	@Override
	public Collection<RepoResource> execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object folderPara = parameter.getParameter(PARA_REPO_FOLDER);
		Assert.isTrue(folderPara instanceof RepoFolder);

		RepoFolder folder = (RepoFolder) folderPara;
		RepoFolderRoot root = folder.getRoot();
		Repo repo = root.getRepo();

		String url = createFetchTagsURL(repo);

		GitHubTagsDTO dtos = getTagsDto((GitHubConnector) connector, url);
		Set<RepoResource> tags = new LinkedHashSet<RepoResource>();
		for (Entry<String, String> dtoEntry : dtos.getTags().entrySet()) {
			String id = dtoEntry.getValue();
			String name = dtoEntry.getKey();
			tags.add(new RepoFolder(root, folder, id, name));
		}

		return tags;
	}

	protected GitHubTagsDTO getTagsDto(GitHubConnector connector, String url) {
		String respBody = connector.getResponseBody(url);
		return parseContent(respBody);
	}

	protected String createFetchTagsURL(Repo repo) {
		return GitHubOperationUtil.makeURI(SHOW_GITHUB_TAGS, (String) repo
				.getProperty(GitHubOperationConstants.GITHUB_OWNER), repo
				.getName(), GitHubOperationConstants.TAGS_FOLDER_NAME);
	}

	private GitHubTagsDTO parseContent(String respBody)
			throws SCMResponseException {
		try {
			return GitHubOperationUtil.fromJson(respBody, GitHubTagsDTO.class);
		} catch (Exception e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}

}
