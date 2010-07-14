package org.codefaces.core.services.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.httpclient.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubFetchFileInfoQueryTest {
	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_BRANCH_MASTER = "master";

	private static final String TEST_BRANCH_MASTER_SHA = "7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_FETCH_FILE_URL = "http://github.com/api/v2/json/blob/show/jingweno/ruby_grep/7b12ed0f174aaf84e426209986665c13d1170706/LICENSE";

	private static final String TEST_FILE_NAME = "LICENSE";

	private GitHubFetchFileInfoQuery query;

	private ManagedHttpClient client;

	@Before
	public void setUp() {
		query = new GitHubFetchFileInfoQuery();
		client = new ManagedHttpClient();
	}

	@Test
	public void test_createGetGitHubFileUrl() {
		Repo repo = new Repo(TEST_REPO_URL, TEST_REPO_NAME, new RepoCredential(
				TEST_OWNER_NAME, null, null));
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, true);
		String url = query.createFetchFileInfoUrl(repo, branch.getRoot(),
				TEST_FILE_NAME);

		assertEquals(TEST_FETCH_FILE_URL, url);
	}

	@Test
	public void test_getGitHubFile_mimeTypeText() throws MalformedURLException {
		Repo repo = new Repo(TEST_REPO_URL, TEST_REPO_NAME, new RepoCredential(
				TEST_OWNER_NAME, null, null));
		RepoBranch branch = new RepoBranch(repo, TEST_BRANCH_MASTER_SHA,
				TEST_BRANCH_MASTER, true);
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		RepoFile file = new RepoFile(branch.getRoot(), branch.getRoot(),
				"id", TEST_FILE_NAME);
		para.addParameter(SCMQuery.PARA_REPO_FILE, file);
		RepoFileInfo info = query.execute(client, para);

		assertEquals(1059, info.getSize());
		assertEquals("text/plain", info.getMimeType());
		assertEquals("100644", info.getMode());
		assertNotNull(info.getContent());
	}
}
