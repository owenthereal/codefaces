package org.codefaces.web.github.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.codefaces.web.internal.urls.URLQueryStrings;
import org.codefaces.web.internal.urls.github.GitHubUrlParseStrategy;
import org.junit.Before;
import org.junit.Test;

public class GithubUrlParseStrategyTest {
	private GitHubUrlParseStrategy strategy;

	private static final String TEST_URL_MASTER_ROOT = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_URL_MASTER_ROOT_WITH_TRAILING_SLASH = "http://github.com/jingweno/ruby_grep/";
	
	private static final String TEST_URL_MASTER_FILE = "http://github.com/jingweno/ruby_grep/blob/master/lib/ruby_grep.rb";

	private static final String TEST_URL_BRANCH_ROOT = "http://github.com/schacon/ruby-git/tree/internals";
	
	private static final String TEST_URL_BRANCH_ROOT_WITH_TRAILING_SLASH = "http://github.com/schacon/ruby-git/tree/internals/";
	

	@Before
	public void setUp() {
		strategy = new GitHubUrlParseStrategy();
	}

	@Test
	public void test_extractParameters_masterRoot() {
		URLQueryStrings parameters = strategy
				.buildQueryStrings(TEST_URL_MASTER_ROOT);

		assertEquals("http://github.com/jingweno/ruby_grep", parameters
				.getParameter(URLQueryStrings.REPO));
		assertNull(parameters.getParameter(URLQueryStrings.BRANCH));
	}
	
	@Test
	public void repositoryShouldBeParsedCorrectlyWhenMasterRootUrlContainsTrailingSlash(){
		URLQueryStrings parameters = strategy
		.buildQueryStrings(TEST_URL_MASTER_ROOT_WITH_TRAILING_SLASH);
		assertEquals("http://github.com/jingweno/ruby_grep",
				parameters.getParameter(URLQueryStrings.REPO));
		assertNull(parameters.getParameter(URLQueryStrings.BRANCH));
	}
	
	@Test
	public void test_extractParameters_masterFile() {
		URLQueryStrings parameters = strategy
				.buildQueryStrings(TEST_URL_MASTER_FILE);

		assertEquals("http://github.com/jingweno/ruby_grep", parameters
				.getParameter(URLQueryStrings.REPO));
		assertEquals("master", parameters.getParameter(URLQueryStrings.BRANCH));
	}
	
	@Test
	public void test_extractParameters_branchRoot() {
		URLQueryStrings parameters = strategy
		.buildQueryStrings(TEST_URL_BRANCH_ROOT);
		
		assertEquals("http://github.com/schacon/ruby-git", parameters
				.getParameter(URLQueryStrings.REPO));
		assertEquals("internals", parameters.getParameter(URLQueryStrings.BRANCH));
	}
	
	@Test
	public void repositoryShouldBeParsedCorrectlyWhenBranchRootUrlContainsTrailingSlash(){
		URLQueryStrings parameters = strategy
				.buildQueryStrings(TEST_URL_BRANCH_ROOT_WITH_TRAILING_SLASH);
		assertEquals("http://github.com/schacon/ruby-git",
				parameters.getParameter(URLQueryStrings.REPO));
		assertEquals("internals", parameters.getParameter(URLQueryStrings.BRANCH));
	}
}
