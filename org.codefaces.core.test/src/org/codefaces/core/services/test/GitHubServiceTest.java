package org.codefaces.core.services.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.util.Set;
import java.util.Map.Entry;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.RepoConnectionException;
import org.codefaces.core.services.RepoResponseException;
import org.codefaces.core.services.github.GitHubBranchesDto;
import org.codefaces.core.services.github.GitHubService;
import org.junit.Before;
import org.junit.Test;

public class GitHubServiceTest {
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
	
	private static final String TEST_GITHUB_URL_2 = "http://github.com/schacon/ruby-git";
	
	private static final String TEST_REPO_NAME_2 = "ruby_git";
	
	private static final String TEST_USER_NAME_2 = "schacon";

	private static final String TEST_GITHUB_LIST_CHILDREN_URL = "http://github.com/api/v2/json/tree/show/jingweno/ruby_grep/7b12ed0f174aaf84e426209986665c13d1170706";

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
	public void test_getGitHubBranches() throws RepoConnectionException, RepoResponseException {
		GitHubBranchesDto branches = gitHubService
				.getGitHubBranches(TEST_GITHUB_SHOW_BRANCHES_URL);

		assertEquals(1, branches.getBrances().size());

		Entry<String, String> branch = branches.getBrances().entrySet()
				.iterator().next();
		assertEquals(TEST_BRANCH_MASTER, branch.getKey());
		assertEquals(TEST_BRANCH_MASTER_SHA, branch.getValue());
		
		//Test if there is more than one branches. This url is adopted from the 
		//GitHub API example
		branches = gitHubService
		.getGitHubBranches(TEST_GITHUB_SHOW_BRANCHES_URL_2);
		assertEquals(4, branches.getBrances().size());
		branch = branches.getBrances().entrySet().iterator().next();
	}

	@Test
	public void test_createGithubRepo() throws RepoConnectionException,
			RepoResponseException, MalformedURLException {
		Repo gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_URL);

		assertEquals(TEST_GITHUB_URL, gitHubRepo.getUrl());
		assertEquals(1, gitHubRepo.getBranches().size());
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
		
		// Test if the repository is private
		try{
			gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_PRIVATE_URL);
			fail("Should throw a RepoConnectionException");
		}catch(Exception e){
			assertTrue(e instanceof RepoConnectionException);
		}
		
		// Test if there is no such repository
		try{
			gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_NO_REPO_URL);
			fail("Should throw a RepoConnectionException");
		}catch(Exception e){
			assertTrue(e instanceof RepoConnectionException);
		}
		
		// Test if the url is malformed
		try{
			gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_MALFORMED_URL);
			fail("Should throw an MalformedURLException");
		}catch(Exception e){
			assertTrue(e instanceof MalformedURLException);
		}
	}

	@Test
	public void test_createGitHubListChildrenUrl() {
		RepoCredential credential = new RepoCredential(TEST_USER_NAME, null,
				null);
		Repo mock_repo = new Repo(TEST_GITHUB_URL, TEST_REPO_NAME, null,
				credential);
		RepoBranch mock_resource;

		mock_resource = new RepoBranch(TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, mock_repo);
		String githubListChildrenUrl = gitHubService
				.createGitHubListChildrenUrl(mock_repo, mock_resource);
		assertEquals(TEST_GITHUB_LIST_CHILDREN_URL, githubListChildrenUrl);
	}

	@Test
	public void test_getGitHubChildren() throws RepoConnectionException,
			RepoResponseException, MalformedURLException {
		Repo gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_URL);
		RepoBranch mock_resource = new RepoBranch(TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, gitHubRepo);

		Set<RepoResource> children = gitHubService.listGitHubChildren(
				gitHubRepo, mock_resource);
		assertEquals(6, children.size());
		
		// Test if the branch is empty
		gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_EMPTY_URL);
		mock_resource = new RepoBranch(TEST_GITHUB_EMPTY_MASTER_SHA,
				TEST_BRANCH_MASTER, gitHubRepo);
		children = gitHubService.listGitHubChildren(
				gitHubRepo, mock_resource);
		assertEquals(0, children.size());
		 
	}
	
	public void test_getDefaultRoot() throws RepoConnectionException, RepoResponseException, MalformedURLException{
		Repo gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_URL);
		assertEquals(TEST_BRANCH_MASTER, gitHubService.getGitHubDefaultRoot(
				gitHubRepo).getName());
	}

}