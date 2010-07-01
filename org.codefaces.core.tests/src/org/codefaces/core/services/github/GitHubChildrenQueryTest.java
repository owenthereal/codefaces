package org.codefaces.core.services.github;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.httpclient.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubChildrenQueryTest {
	private GitHubFetchChildrenQuery query;

	private ManagedHttpClient client;

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_FETCH_CHILDREN_URL = "http://github.com/api/v2/json/tree/show/jingweno/ruby_grep/7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_BRANCH_MASTER_SHA = "7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_BRANCH_MASTER = "master";

	@Before
	public void setUp() {
		query = new GitHubFetchChildrenQuery();
		client = new ManagedHttpClient();
	}

	@Test
	public void test_createFetchChildrenUrl() {
		Repo repo = new Repo(TEST_REPO_URL, TEST_REPO_NAME, new RepoCredential(
				TEST_OWNER_NAME, null, null));
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, true);
		String url = query.createFetchChildrenUrl(repo, branch);

		assertEquals(TEST_FETCH_CHILDREN_URL, url);
	}

	@Test
	public void test_execute() throws MalformedURLException {
		Repo repo = new Repo(TEST_REPO_URL, TEST_REPO_NAME, new RepoCredential(
				TEST_OWNER_NAME, null, null));
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, true);
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO_RESOURCE, branch);
		Collection<RepoResource> children = query.execute(client, para);

		assertEquals(6, children.size());
	}
}
