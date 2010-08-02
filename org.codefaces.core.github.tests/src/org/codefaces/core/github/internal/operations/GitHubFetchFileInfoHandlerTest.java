package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.internal.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubFetchFileInfoHandlerTest {
	private static final String KIND_GIT_HUB = "GitHub";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_FETCH_FILE_URL = "http://github.com/api/v2/json/blob/show/jingweno/ruby_grep/7b12ed0f174aaf84e426209986665c13d1170706/LICENSE";

	private static final String TEST_FOLDER_SHA = "7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_FILE_NAME = "LICENSE";

	private GitHubFetchFileInfoHandler query;

	private GitHubConnector connector;

	@Before
	public void setUp() {
		connector = new GitHubConnector(new ManagedHttpClient());
		query = new GitHubFetchFileInfoHandler();
	}

	@Test
	public void createGetGitHubFileURL() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(null, null));
		repo.setProperty(GitHubOperationConstants.GITHUB_OWNER, TEST_OWNER_NAME);
		RepoFolder folder = new RepoFolder(repo.getRoot(), repo.getRoot(),
				TEST_FOLDER_SHA, "master");

		String url = query.createFetchFileInfoUrl(repo, folder, TEST_FILE_NAME);

		assertEquals(TEST_FETCH_FILE_URL, url);
	}

	@Test
	public void executeReturnsSizeMimeTypeModeContent()
			throws MalformedURLException {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(null, null));
		repo.setProperty(GitHubOperationConstants.GITHUB_OWNER, TEST_OWNER_NAME);
		RepoFolder folder = new RepoFolder(repo.getRoot(), repo.getRoot(),
				TEST_FOLDER_SHA, "master");
		RepoFile file = new RepoFile(repo.getRoot(), folder, "id",
				TEST_FILE_NAME);

		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_REPO_FILE, file);
		RepoFileInfo info = query.execute(connector, para);

		assertEquals(1059, info.getSize());
		assertEquals("text/plain", info.getMimeType());
		assertEquals("100644", info.getMode());
		assertNotNull(info.getContent());
	}
}
