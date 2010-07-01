package org.codefaces.core.services.github;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.junit.Before;
import org.junit.Test;

public class GitHubRepoQueryTest {
	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_REPO_URL_WITH_ENDING_SLASH = "http://github.com/jingweno/ruby_grep/";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_USER_NAME = "jingweno";

	private GitHubRepoQuery query;

	@Before
	public void setUp() {
		query = new GitHubRepoQuery();
	}

	@Test
	public void test_execute() throws MalformedURLException {
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_URL, TEST_REPO_URL);
		Repo gitHubRepo = query.execute(null, para);

		assertEquals(TEST_REPO_URL, gitHubRepo.getUrl());
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME, gitHubRepo.getName());

		para.addParameter(SCMQuery.PARA_URL,
				TEST_REPO_URL_WITH_ENDING_SLASH);
		gitHubRepo = query.execute(null, para);
		
		assertEquals(TEST_REPO_URL_WITH_ENDING_SLASH, gitHubRepo.getUrl());
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME, gitHubRepo.getName());
	}
}
