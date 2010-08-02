package org.codefaces.web.internal.urls.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.codefaces.ui.SCMConfigurableElements;
import org.codefaces.ui.SCMURLConfigurations;
import org.codefaces.web.internal.urls.github.GitHubUrlParseStrategy;
import org.junit.Before;
import org.junit.Test;

public class GithubUrlParseStrategyTest {
	private GitHubUrlParseStrategy strategy;
	
	private static final String GITHUB_BRANCHES_DIR = "branches";
	
	private static final String TEST_SCM_KIND = "GitHub";	

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
		SCMURLConfigurations config = strategy
				.buildConfigurations(TEST_URL_MASTER_ROOT);

		assertEquals("http://github.com/jingweno/ruby_grep", config
				.get(SCMConfigurableElements.URL));
		assertNull(config.get(SCMConfigurableElements.BASE_DIRECTORY));
	}
	
	@Test
	public void repositoryShouldBeParsedCorrectlyWhenMasterRootUrlContainsTrailingSlash(){
		SCMURLConfigurations config = strategy
				.buildConfigurations(TEST_URL_MASTER_ROOT_WITH_TRAILING_SLASH);
		assertEquals("http://github.com/jingweno/ruby_grep",
				config.get(SCMConfigurableElements.URL));
		assertEquals(TEST_SCM_KIND, config.get(SCMConfigurableElements.KIND));
		assertNull(config.get(SCMConfigurableElements.BASE_DIRECTORY));
	}
	
	@Test
	public void test_extractParameters_masterFile() {
		SCMURLConfigurations config = strategy
				.buildConfigurations(TEST_URL_MASTER_FILE);

		assertEquals("http://github.com/jingweno/ruby_grep", config
				.get(SCMConfigurableElements.URL));
		assertEquals(TEST_SCM_KIND, config.get(SCMConfigurableElements.KIND));
		assertEquals(
				GITHUB_BRANCHES_DIR + "/" + "master",
				config.get(SCMConfigurableElements.BASE_DIRECTORY));
	}
	
	@Test
	public void test_extractParameters_branchRoot() {
		SCMURLConfigurations config = strategy
				.buildConfigurations(TEST_URL_BRANCH_ROOT);
		
		assertEquals("http://github.com/schacon/ruby-git", config
				.get(SCMConfigurableElements.URL));
		assertEquals(TEST_SCM_KIND, config.get(SCMConfigurableElements.KIND));
		assertEquals(
				GITHUB_BRANCHES_DIR + "/" + "internals",
				config.get(SCMConfigurableElements.BASE_DIRECTORY));
	}
	
	@Test
	public void repositoryShouldBeParsedCorrectlyWhenBranchRootUrlContainsTrailingSlash(){
		SCMURLConfigurations config = strategy
				.buildConfigurations(TEST_URL_BRANCH_ROOT_WITH_TRAILING_SLASH);
		assertEquals("http://github.com/schacon/ruby-git",
				config.get(SCMConfigurableElements.URL));
		assertEquals(TEST_SCM_KIND, config.get(SCMConfigurableElements.KIND));
		assertEquals(
				GITHUB_BRANCHES_DIR + "/" + "internals",
				config.get(SCMConfigurableElements.BASE_DIRECTORY));
	}
}
