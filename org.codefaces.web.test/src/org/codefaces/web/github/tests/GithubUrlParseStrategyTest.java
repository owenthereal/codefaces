package org.codefaces.web.github.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.codefaces.web.RepoParameters;
import org.codefaces.web.github.GitHubUrlParseStrategy;
import org.junit.Before;
import org.junit.Test;

public class GithubUrlParseStrategyTest {
	private GitHubUrlParseStrategy strategy;

	private static final String TEST_URL_MASTER_ROOT = "http://github.com/jingweno/ruby_grep";
	
	private static final String TEST_URL_MASTER_FILE = "http://github.com/jingweno/ruby_grep/blob/master/lib/ruby_grep.rb";

	private static final String TEST_URL_BRANCH_ROOT = "http://github.com/schacon/ruby-git/tree/internals";

	@Before
	public void setUp() {
		strategy = new GitHubUrlParseStrategy();
	}

	@Test
	public void test_extractParameters_masterRoot() {
		RepoParameters parameters = strategy
				.extractParameters(TEST_URL_MASTER_ROOT);

		assertEquals("http://github.com/jingweno/ruby_grep", parameters
				.getParameter(RepoParameters.REPO));
		assertNull(parameters.getParameter(RepoParameters.BRANCH));
	}

	@Test
	public void test_extractParameters_masterFile() {
		RepoParameters parameters = strategy
				.extractParameters(TEST_URL_MASTER_FILE);

		assertEquals("http://github.com/jingweno/ruby_grep", parameters
				.getParameter(RepoParameters.REPO));
		assertEquals("master", parameters.getParameter(RepoParameters.BRANCH));
	}
	
	@Test
	public void test_extractParameters_branchRoot() {
		RepoParameters parameters = strategy
		.extractParameters(TEST_URL_BRANCH_ROOT);
		
		assertEquals("http://github.com/schacon/ruby-git", parameters
				.getParameter(RepoParameters.REPO));
		assertEquals("internals", parameters.getParameter(RepoParameters.BRANCH));
	}
}
