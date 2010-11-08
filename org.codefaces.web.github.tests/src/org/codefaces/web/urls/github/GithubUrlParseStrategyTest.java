package org.codefaces.web.urls.github;

import static org.junit.Assert.*;

import org.codefaces.ui.SCMConfigurableElement;
import org.codefaces.ui.SCMURLConfiguration;
import org.codefaces.web.urls.github.GitHubUrlParseStrategy;
import org.junit.Before;
import org.junit.Test;

public class GithubUrlParseStrategyTest {
	private GitHubUrlParseStrategy strategy;

	private static final String GITHUB_BRANCHES_DIR = "branches";

	private static final String SCM_KIND_GITHUB = "GitHub";

	private static final String MASTER_BRANCH = GITHUB_BRANCHES_DIR + "/master";

	private static final String HTTP_GITHUB_MASTER_BRANCH_URL = "http://github.com/jingweno/ruby_grep";

	private static final String HTTP_GITHUB_MASTER_BRANCH_URL_WITH_TRAILING_SLASH = "http://github.com/jingweno/ruby_grep/";

	private static final String HTTP_GITHUB_MASTER_BRANCH_FILE_URL = "http://github.com/jingweno/ruby_grep/blob/master/lib/ruby_grep.rb";

	private static final String HTTP_GITHUB_BRANCH_URL = "http://github.com/schacon/ruby-git/tree/internals";

	private static final String HTTP_GITHUB_BRANCH_URL_WITH_TRAILING_SLASH = "http://github.com/schacon/ruby-git/tree/internals/";

	private static final String HTTPS_GITHUB_MASTER_BRANCH_URL = "https://github.com/cowboyd/hudson.rb";

	@Before
	public void setUp() {
		strategy = new GitHubUrlParseStrategy();
	}

	@Test
	public void extractParameters_masterRoot() {
		SCMURLConfiguration config = strategy
				.buildConfigurations(HTTP_GITHUB_MASTER_BRANCH_URL);

		assertEquals(HTTP_GITHUB_MASTER_BRANCH_URL,
				config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(MASTER_BRANCH,
				config.get(SCMConfigurableElement.BASE_DIRECTORY));
	}

	@Test
	public void repositoryShouldBeParsedCorrectlyWhenMasterRootUrlContainsTrailingSlash() {
		SCMURLConfiguration config = strategy
				.buildConfigurations(HTTP_GITHUB_MASTER_BRANCH_URL_WITH_TRAILING_SLASH);

		assertEquals(HTTP_GITHUB_MASTER_BRANCH_URL,
				config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(SCM_KIND_GITHUB,
				config.get(SCMConfigurableElement.SCM_KIND));
		assertEquals(MASTER_BRANCH,
				config.get(SCMConfigurableElement.BASE_DIRECTORY));
	}

	@Test
	public void extractParameters_masterFile() {
		SCMURLConfiguration config = strategy
				.buildConfigurations(HTTP_GITHUB_MASTER_BRANCH_FILE_URL);

		assertEquals(HTTP_GITHUB_MASTER_BRANCH_URL,
				config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(SCM_KIND_GITHUB,
				config.get(SCMConfigurableElement.SCM_KIND));
		assertEquals(MASTER_BRANCH,
				config.get(SCMConfigurableElement.BASE_DIRECTORY));
	}

	@Test
	public void extractParameters_branchRoot() {
		SCMURLConfiguration config = strategy
				.buildConfigurations(HTTP_GITHUB_BRANCH_URL);

		assertEquals("http://github.com/schacon/ruby-git",
				config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(SCM_KIND_GITHUB,
				config.get(SCMConfigurableElement.SCM_KIND));
		assertEquals(GITHUB_BRANCHES_DIR + "/" + "internals",
				config.get(SCMConfigurableElement.BASE_DIRECTORY));
	}

	@Test
	public void repositoryShouldBeParsedCorrectlyWhenBranchRootUrlContainsTrailingSlash() {
		SCMURLConfiguration config = strategy
				.buildConfigurations(HTTP_GITHUB_BRANCH_URL_WITH_TRAILING_SLASH);

		assertEquals("http://github.com/schacon/ruby-git",
				config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(SCM_KIND_GITHUB,
				config.get(SCMConfigurableElement.SCM_KIND));
		assertEquals(GITHUB_BRANCHES_DIR + "/" + "internals",
				config.get(SCMConfigurableElement.BASE_DIRECTORY));
	}

	@Test
	public void canParseHTTPSGitHubMasterBranchURL() {
		assertTrue(strategy.canParse(HTTPS_GITHUB_MASTER_BRANCH_URL));
	}

	@Test
	public void buildConfigurationsForHTTPSGitHubMasterBranchURL() {
		SCMURLConfiguration config = strategy
				.buildConfigurations(HTTPS_GITHUB_MASTER_BRANCH_URL);

		assertEquals(HTTPS_GITHUB_MASTER_BRANCH_URL,
				config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(SCM_KIND_GITHUB,
				config.get(SCMConfigurableElement.SCM_KIND));
		assertEquals(MASTER_BRANCH,
				config.get(SCMConfigurableElement.BASE_DIRECTORY));
	}
}
