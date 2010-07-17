package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.Collection;

import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.GitHubFetchChildrenOperationHandler;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.internal.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubChildrenQueryTest {
	private static final String KIND_GIT_HUB = "GitHub";

	private GitHubFetchChildrenOperationHandler query;

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_FETCH_CHILDREN_URL = "http://github.com/api/v2/json/tree/show/jingweno/ruby_grep/7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_BRANCH_MASTER_SHA = "7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_BRANCH_MASTER = "master";

	private GitHubConnector connector;

	@Before
	public void setUp() {
		connector = new GitHubConnector(new ManagedHttpClient());
		query = new GitHubFetchChildrenOperationHandler();
	}

	@Test
	public void test_createFetchChildrenUrl() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, true);
		String url = query.createFetchChildrenUrl(repo, branch);

		assertEquals(TEST_FETCH_CHILDREN_URL, url);
	}

	@Test
	public void test_execute() throws MalformedURLException {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, true);
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_REPO_RESOURCE, branch);
		Collection<RepoResource> children = query.execute(connector, para);

		assertEquals(6, children.size());
	}
}
