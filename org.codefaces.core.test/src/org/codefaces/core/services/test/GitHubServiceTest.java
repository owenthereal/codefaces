package org.codefaces.core.services.test;

import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import org.codefaces.core.models.Repo;
import org.codefaces.core.services.github.GitHubBranchesDto;
import org.codefaces.core.services.github.GitHubService;
import org.junit.Before;
import org.junit.Test;

public class GitHubServiceTest {
	private static final String TEST_BRANCH_MASTER = "master";

	private static final String TEST_BRANCH_MASTER_SHA = "7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_GITHUB_SHOW_BRANCHES_URL = "http://github.com/api/v2/json/repos/show/jingweno/ruby_grep/branches";

	private static final String TEST_GITHUB_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_USER_NAME = "jingweno";

	private GitHubService gitHubService;

	@Before
	public void setUp() {
		gitHubService = new GitHubService();
	}

	@Test
	public void test_createGithubRepoShowUrl() {
		String githubShowUrl = gitHubService.createGitHubShowBranchesUrl(
				TEST_USER_NAME, TEST_REPO_NAME);

		assertEquals(TEST_GITHUB_SHOW_BRANCHES_URL, githubShowUrl);
	}

	@Test
	public void test_getGitHubBranches() {
		GitHubBranchesDto branches = gitHubService
				.getGitHubBranches(TEST_GITHUB_SHOW_BRANCHES_URL);

		assertEquals(1, branches.getBrances().size());

		Entry<String, String> branch = branches.getBrances().entrySet()
				.iterator().next();
		assertEquals(TEST_BRANCH_MASTER, branch.getKey());
		assertEquals(TEST_BRANCH_MASTER_SHA, branch.getValue());
	}

	@Test
	public void test_createGithubRepo() {
		Repo gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_URL);

		assertEquals(TEST_GITHUB_URL, gitHubRepo.getUrl());
		assertEquals(1, gitHubRepo.getBranches().size());
	}
}
