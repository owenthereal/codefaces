package org.codefaces.core.services.github;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.core.services.github.dtos.GitHubBranchesDto;
import org.codefaces.httpclient.SCMHttpClient;
import org.codefaces.httpclient.SCMResponseException;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchBranchesQuery implements
		SCMQuery<Collection<RepoBranch>> {
	private static final String URI_BRANCHES_SQGMENT = "branches";

	private static final String SHOW_GITHUB_BRANCHES = "http://github.com/api/v2/json/repos/show";

	private static final String MASTER_BRANCH_NAME = "master";

	@Override
	public Collection<RepoBranch> execute(SCMHttpClient client,
			SCMQueryParameter parameter) {
		Object repoPara = parameter.getParameter(PARA_REPO);
		Assert.isTrue(repoPara instanceof Repo);

		Repo repo = (Repo) repoPara;
		String url = createFetchBranchesUrl(repo);

		GitHubBranchesDto dtos = getBranchesDto(client, url);

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

	protected GitHubBranchesDto getBranchesDto(SCMHttpClient client, String url) {
		String respBody = client.getResponseBody(url);
		return parseContent(respBody);
	}

	protected String createFetchBranchesUrl(Repo repo) {
		return GitHubUtil.makeURI(SHOW_GITHUB_BRANCHES, repo.getCredential()
				.getOwner(), repo.getName(), URI_BRANCHES_SQGMENT);
	}

	private GitHubBranchesDto parseContent(String respBody)
			throws SCMResponseException {
		try {
			return GitHubUtil.fromJson(respBody, GitHubBranchesDto.class);
		} catch (Exception e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}
}
