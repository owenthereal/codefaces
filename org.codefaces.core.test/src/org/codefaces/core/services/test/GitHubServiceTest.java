package org.codefaces.core.services.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.RepoResourceType;
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
		
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
	}

	@Test
	public void test_createGitHubListChildrenUrl() {
		RepoCredential credential = new RepoCredential(TEST_USER_NAME,
				null, null);
		Repo mock_repo = new Repo(TEST_GITHUB_URL,TEST_REPO_NAME, null, credential);
		RepoResource mock_resource;
		
		// valid setting (type = Branch or Folder)
		mock_resource = new RepoResource(TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, RepoResourceType.BRANCH, null);
		String githubListChildrenUrl = gitHubService.createGitHubListChildrenUrl(mock_repo, mock_resource);
		assertEquals(TEST_GITHUB_LIST_CHILDREN_URL, githubListChildrenUrl);
	    
		// no exception for Folder
		mock_resource = new RepoResource(TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, RepoResourceType.FOLDER, null);
		gitHubService.createGitHubListChildrenUrl(mock_repo, mock_resource);
		
		//throw exceptions if type == REPO
		try{
			mock_resource = new RepoResource(TEST_BRANCH_MASTER_SHA,
					TEST_BRANCH_MASTER, RepoResourceType.REPO, null);
			gitHubService.createGitHubListChildrenUrl(mock_repo, mock_resource);
			fail("Operation not supported by GitHub");
		}catch(Exception ex){
			 assertTrue(ex instanceof UnsupportedOperationException);
		}
		//throw exceptions if type == FILE
		try{
			mock_resource = new RepoResource(TEST_BRANCH_MASTER_SHA,
					TEST_BRANCH_MASTER, RepoResourceType.FILE, null);
			gitHubService.createGitHubListChildrenUrl(mock_repo, mock_resource);
			fail("Operation not supported by GitHub");
		}catch(Exception ex){
			 assertTrue(ex instanceof UnsupportedOperationException);
		} 
	}
	
	@Test
	public void test_getGitHubChildren(){
		Repo gitHubRepo = gitHubService.createGithubRepo(TEST_GITHUB_URL);
		RepoResource mock_resource = new RepoResource(TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, RepoResourceType.BRANCH, null);
		
		Set<RepoResource> children = gitHubService.getGitHubChildren(
				gitHubRepo, mock_resource); 
		assertEquals(6, children.size());
	}

}