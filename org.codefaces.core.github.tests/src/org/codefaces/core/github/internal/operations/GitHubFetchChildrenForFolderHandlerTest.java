package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationParameter;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.internal.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubFetchChildrenForFolderHandlerTest {

	private static final String KIND_GIT_HUB = "GitHub";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_BRANCH_MASTER_SHA = "7b12ed0f174aaf84e426209986665c13d1170706";

	private static final String TEST_FETCH_CHILDREN_URL = "http://github.com/api/v2/json/tree/show/jingweno/ruby_grep/7b12ed0f174aaf84e426209986665c13d1170706";

	private GitHubFetchChildrenForFolderHandler handler;

	private GitHubConnector connector;

	@Before
	public void setup() {
		connector = new GitHubConnector(new ManagedHttpClient());
		handler = new GitHubFetchChildrenForFolderHandler();
	}

	@Test
	public void fetchChildrenFromFolderReturnsExpectedNumberOfChildren() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		RepoFolder branchesFolder = new RepoFolder(repo.getRoot(),
				repo.getRoot(), "branches", "branches");
		RepoFolder masterBranchFolder = new RepoFolder(repo.getRoot(),
				branchesFolder, TEST_BRANCH_MASTER_SHA, "master");

		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationParameter.REPO_FOLDER,
				masterBranchFolder);
		Collection<RepoResource> children = handler.execute(connector, para);

		assertEquals(6, children.size());
	}

	@Test
	public void test_createFetchChildrenUrl() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		RepoFolder branchesFolder = new RepoFolder(repo.getRoot(),
				repo.getRoot(), "branches", "branches");
		RepoFolder masterBranchFolder = new RepoFolder(repo.getRoot(),
				branchesFolder, TEST_BRANCH_MASTER_SHA, "master");

		String url = handler.createFetchChildrenUrl(repo, masterBranchFolder);

		assertEquals(TEST_FETCH_CHILDREN_URL, url);
	}
}
