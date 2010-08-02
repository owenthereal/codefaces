package org.codefaces.core.github.internal.operations;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.dtos.GitHubBranchesDTO;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameter;
import org.codefaces.core.operations.SCMOperationParameters;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchBranchesHandler implements SCMOperationHandler {
	private static final String SHOW_GITHUB_BRANCHES = "http://github.com/api/v2/json/repos/show";

	public static final String ID = "org.codefaces.core.operations.SCMOperation.github.fetchBranches";

	@Override
	public Collection<RepoResource> execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object folderPara = parameter.getParameter(SCMOperationParameter.REPO_FOLDER);
		Assert.isTrue(folderPara instanceof RepoFolder);

		RepoFolder folder = (RepoFolder) folderPara;
		RepoFolderRoot root = folder.getRoot();
		Repo repo = root.getRepo();

		String url = createFetchBranchesURL(repo);

		GitHubBranchesDTO dtos = getBranchesDto((GitHubConnector) connector,
				url);
		Set<RepoResource> branches = new LinkedHashSet<RepoResource>();
		for (Entry<String, String> dtoEntry : dtos.getBrances().entrySet()) {
			String id = dtoEntry.getValue();
			String name = dtoEntry.getKey();
			branches.add(new RepoFolder(root, folder, id, name));
		}

		return branches;
	}

	protected GitHubBranchesDTO getBranchesDto(GitHubConnector connector,
			String url) {
		String respBody = connector.getResponseBody(url);
		return parseContent(respBody);
	}

	protected String createFetchBranchesURL(Repo repo) {
		return GitHubOperationUtil.makeURI(SHOW_GITHUB_BRANCHES, repo
				.getCredential().getOwner(), repo.getName(),
				GitHubOperationConstants.BRANCHES_FOLDER_NAME);
	}

	private GitHubBranchesDTO parseContent(String respBody)
			throws SCMResponseException {
		try {
			return GitHubOperationUtil.fromJson(respBody,
					GitHubBranchesDTO.class);
		} catch (Exception e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}

}
