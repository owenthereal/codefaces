package org.codefaces.core.services.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.core.services.github.dtos.GitHubBranchesDto;
import org.codefaces.httpclient.http.ManagedHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GitHubFetchBranchesQueryTest {
	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_BRANCH_URL = "http://github.com/api/v2/json/repos/show/jingweno/ruby_grep/branches";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_BRANCH_MASTER = "master";

	private GitHubFetchBranchesQuery query;

	private ManagedHttpClient client;

	@Before
	public void setUp() {
		query = new GitHubFetchBranchesQuery();
		client = new ManagedHttpClient();
	}

	@After
	public void tearDown() {
		client.dispose();
	}

	@Test
	public void test_createFetchBranchesUrl() {
		Repo repo = new Repo(TEST_REPO_URL, TEST_REPO_NAME, new RepoCredential(
				TEST_OWNER_NAME, null, null));
		String githubShowUrl = query.createFetchBranchesUrl(repo);

		assertEquals(TEST_BRANCH_URL, githubShowUrl);
	}

	@Test
	public void test_getBranchesDto() {
		GitHubBranchesDto branchesDto = query.getBranchesDto(client,
				TEST_BRANCH_URL);
		Map<String, String> branches = branchesDto.getBrances();

		assertEquals(2, branches.size());
		assertTrue(branches.containsKey(TEST_BRANCH_MASTER));
	}

	@Test
	public void test_execute() {
		Repo repo = new Repo(TEST_REPO_URL, TEST_REPO_NAME, new RepoCredential(
				TEST_OWNER_NAME, null, null));
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO, repo);
		Collection<RepoBranch> branches = query.execute(client, para);

		assertEquals(2, branches.size());
	}

	@Test
	public void test_execute_masterBranch() {
		Repo repo = new Repo(TEST_REPO_URL, TEST_REPO_NAME, new RepoCredential(
				TEST_OWNER_NAME, null, null));
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO, repo);
		Collection<RepoBranch> branches = query.execute(client, para);

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
