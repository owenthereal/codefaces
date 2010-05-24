package org.codefaces.core.services.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map.Entry;

import org.apache.http.impl.client.DefaultHttpClient;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.ManagedHttpClient;
import org.codefaces.core.services.RepoResponseException;
import org.codefaces.core.services.github.GitHubBranchesDto;
import org.codefaces.core.services.github.GitHubService;
import org.junit.Before;
import org.junit.Test;

public class GitHubServiceTest {
	private static final String TEST_GITHUB_FILE_NAME = "LICENSE";

	private static final String TEST_BRANCH_MASTER = "master";

	private static final String TEST_BRANCH_MASTER_SHA = "7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_GITHUB_SHOW_BRANCHES_URL = "http://github.com/api/v2/json/repos/show/jingweno/ruby_grep/branches";

	private static final String TEST_GITHUB_SHOW_BRANCHES_URL_2 = "http://github.com/api/v2/json/repos/show/schacon/ruby-git/branches";

	private static final String TEST_GITHUB_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_GITHUB_PRIVATE_URL = "http://github.com/jingweno/code_faces";

	private static final String TEST_GITHUB_NO_REPO_URL = "http://github.com/jingweno/xyz";

	private static final String TEST_GITHUB_MALFORMED_URL = "http://github.com/hello";

	private static final String TEST_GITHUB_EMPTY_URL = "http://github.com/kklo/empty_testing_project";

	private static final String TEST_GITHUB_EMPTY_MASTER_SHA = "0a5216bf5231380e77d21e3ccad1243153eafe2d";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_USER_NAME = "jingweno";

	private static final String TEST_GITHUB_LIST_CHILDREN_URL = "http://github.com/api/v2/json/tree/show/jingweno/ruby_grep/7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_GET_GITHUB_FILE_URL = "http://github.com/api/v2/json/blob/show/jingweno/ruby_grep/7b12ed0f174aaf84e426209986665c13d1170706/LICENSE";

	private static final String TEST_GITHUB_BRANCH_SHA = "7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_GITHUB_FILE_SHA = "7911a0917911f6e775d85c8f514ba5e0c6ebf71a";

	private GitHubService gitHubService;

	@Before
	public void setUp() {
		gitHubService = new GitHubService(new ManagedHttpClient().getClient());
	}

	@Test
	public void test_createGithubRepoShowUrl() {
		String githubShowUrl = gitHubService.createGitHubShowBranchesUrl(
				TEST_USER_NAME, TEST_REPO_NAME);

		assertEquals(TEST_GITHUB_SHOW_BRANCHES_URL, githubShowUrl);
	}

	@Test
	public void test_getGitHubBranches() throws RepoResponseException {
		GitHubBranchesDto branches = gitHubService
				.getGitHubBranches(TEST_GITHUB_SHOW_BRANCHES_URL);

		assertEquals(1, branches.getBrances().size());

		Entry<String, String> branch = branches.getBrances().entrySet()
				.iterator().next();
		assertEquals(TEST_BRANCH_MASTER, branch.getKey());
		assertEquals(TEST_BRANCH_MASTER_SHA, branch.getValue());

		// Test if there is more than one branches. This url is adopted from the
		// GitHub API example
		branches = gitHubService
				.getGitHubBranches(TEST_GITHUB_SHOW_BRANCHES_URL_2);
		assertEquals(4, branches.getBrances().size());
	}

	@Test
	public void test_createGithubRepo() throws RepoResponseException,
			MalformedURLException {
		Repo gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_URL);

		assertEquals(TEST_GITHUB_URL, gitHubRepo.getUrl());
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
	}

	@Test
	public void test_createGitHubListChildrenUrl() {
		RepoCredential credential = new RepoCredential(TEST_USER_NAME, null,
				null);
		Repo repo = new Repo(TEST_GITHUB_URL, TEST_REPO_NAME, credential);
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER);
		String githubListChildrenUrl = gitHubService
				.createGitHubListChildrenUrl(repo, branch);
		assertEquals(TEST_GITHUB_LIST_CHILDREN_URL, githubListChildrenUrl);
	}

	@Test
	public void test_getGitHubChildren() throws RepoResponseException,
			MalformedURLException {
		Repo repo = gitHubService.createGithubRepo(TEST_GITHUB_URL);
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER);

		Collection<RepoResource> children = gitHubService
				.fetchGitHubChildren(branch);
		assertEquals(6, children.size());

		// Test if the branch is empty
		repo = gitHubService.createGithubRepo(TEST_GITHUB_EMPTY_URL);
		branch = new RepoBranch(repo, TEST_GITHUB_EMPTY_MASTER_SHA,
				TEST_BRANCH_MASTER);
		children = gitHubService.fetchGitHubChildren(branch);
		assertEquals(0, children.size());
	}

	public void test_getDefaultRoot() throws RepoResponseException,
			MalformedURLException {
		Repo gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_URL);
		assertEquals(TEST_BRANCH_MASTER, gitHubService.getGitHubDefaultBranch(
				gitHubRepo).getName());
	}

	@Test
	public void test_createGetGitHubFileUrl() throws RepoResponseException,
			MalformedURLException {
		Repo repo = gitHubService.createGithubRepo(TEST_GITHUB_URL);
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER);
		RepoFolderRoot root = branch.getRoot();
		RepoFile file = new RepoFile(root, root, TEST_GITHUB_BRANCH_SHA,
				TEST_GITHUB_FILE_NAME);

		assertEquals(TEST_GET_GITHUB_FILE_URL, gitHubService
				.createGetGitHubFileUrl(repo, file));
	}

	@Test
	public void test_getGitHubFile() throws RepoResponseException,
			MalformedURLException {
		Repo repo = gitHubService.createGithubRepo(TEST_GITHUB_URL);
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER);
		RepoFolderRoot root = branch.getRoot();
		RepoFile file = new RepoFile(root, root, TEST_GITHUB_BRANCH_SHA,
				TEST_GITHUB_FILE_NAME);

		RepoFileInfo info = gitHubService.fetchGitHubFileInfo(file);

		assertEquals(file, info.getContext());
		assertEquals(1059, info.getSize());
		assertEquals("text/plain", info.getMimeType());
		assertEquals("100644", info.getMode());
		assertNotNull(info.getContent());
	}
}