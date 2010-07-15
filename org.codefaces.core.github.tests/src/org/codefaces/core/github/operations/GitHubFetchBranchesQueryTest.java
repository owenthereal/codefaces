package org.codefaces.core.github.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.codefaces.core.github.connectors.GitHubConnector;
import org.codefaces.core.github.operations.dto.GitHubBranchesDto;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubFetchBranchesQueryTest {
	private static final String KIND_GIT_HUB = "GitHub";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_BRANCH_URL = "http://github.com/api/v2/json/repos/show/jingweno/ruby_grep/branches";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_BRANCH_MASTER = "master";

	private GitHubFetchBranchesOperationHandler query;

	private GitHubConnector connector;

	@Before
	public void setUp() {
		connector = new GitHubConnector(new ManagedHttpClient());
		query = new GitHubFetchBranchesOperationHandler();
	}

	@Test
	public void test_createFetchBranchesUrl() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		String githubShowUrl = query.createFetchBranchesUrl(repo);

		assertEquals(TEST_BRANCH_URL, githubShowUrl);
	}

	@Test
	public void test_getBranchesDto() {
		GitHubBranchesDto branchesDto = query.getBranchesDto(connector,
				TEST_BRANCH_URL);
		Map<String, String> branches = branchesDto.getBrances();

		assertEquals(2, branches.size());
		assertTrue(branches.containsKey(TEST_BRANCH_MASTER));
	}

	@Test
	public void test_execute() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_REPO, repo);
		Collection<RepoBranch> branches = query.execute(connector, para);

		assertEquals(2, branches.size());
	}

	@Test
	public void test_execute_masterBranch() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_REPO, repo);
		Collection<RepoBranch> branches = query.execute(connector, para);

		int masterBranchCounter = 0;
		for (RepoBranch branch : branches) {
			if (branch.isMaster()) {
				assertEquals(TEST_BRANCH_MASTER, branch.getName());
				masterBranchCounter++;
			}
		}

		assertEquals(1, masterBranchCounter);
	}
}
