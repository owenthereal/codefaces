package org.codefaces.core.github.operations;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.github.connectors.GitHubConnector;
import org.codefaces.core.github.operations.dtos.GitHubBranchesDTO;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.connectors.SCMResponseException;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchBranchesOperationHandler implements SCMOperationHandler {
	private static final String URI_BRANCHES_SQGMENT = "branches";

	private static final String SHOW_GITHUB_BRANCHES = "http://github.com/api/v2/json/repos/show";

	private static final String MASTER_BRANCH_NAME = "master";

	@Override
	public Collection<RepoBranch> execute(SCMConnector connector, SCMOperationParameters parameter) {
		Object repoPara = parameter.getParameter(PARA_REPO);
		Assert.isTrue(repoPara instanceof Repo);

		Repo repo = (Repo) repoPara;
		String url = createFetchBranchesUrl(repo);

		GitHubBranchesDTO dtos = getBranchesDto((GitHubConnector) connector,
				url);

		Set<RepoBranch> branches = new LinkedHashSet<RepoBranch>();
		for (Entry<String, String> dtoEntry : dtos.getBrances().entrySet()) {
			String id = dtoEntry.getValue();
			String name = dtoEntry.getKey();
			boolean isMaster = false;
			if (StringUtils.equals(MASTER_BRANCH_NAME, name)) {
				isMaster = true;
			}

			branches.add(new RepoBranch(repo, id, name, isMaster));
		}

		return branches;
	}

	protected GitHubBranchesDTO getBranchesDto(GitHubConnector connector,
			String url) {
		String respBody = connector.getResponseBody(url);
		return parseContent(respBody);
	}

	protected String createFetchBranchesUrl(Repo repo) {
		return GitHubOperationUtil.makeURI(SHOW_GITHUB_BRANCHES, repo.getCredential()
				.getOwner(), repo.getName(), URI_BRANCHES_SQGMENT);
	}

	private GitHubBranchesDTO parseContent(String respBody)
			throws SCMResponseException {
		try {
			return GitHubOperationUtil.fromJson(respBody, GitHubBranchesDTO.class);
		} catch (Exception e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}

}
