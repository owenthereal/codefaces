package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.dtos.GitHubBranchesDTO;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.internal.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubFetchBranchesHandlerTest {
	private static final String KIND_GIT_HUB = "GitHub";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_BRANCH_URL = "http://github.com/api/v2/json/repos/show/jingweno/ruby_grep/branches";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_BRANCH_MASTER = "master";

	private GitHubFetchBranchesHandler handler;

	private GitHubConnector connector;

	@Before
	public void setUp() {
		connector = new GitHubConnector(new ManagedHttpClient());
		handler = new GitHubFetchBranchesHandler();
	}

	@Test
	public void createFetchBranchesURL() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		String githubShowUrl = handler.createFetchBranchesURL(repo);

		assertEquals(TEST_BRANCH_URL, githubShowUrl);
	}

	@Test
	public void getBranchesDto() {
		GitHubBranchesDTO branchesDto = handler.getBranchesDto(connector,
				TEST_BRANCH_URL);
		Map<String, String> branches = branchesDto.getBrances();

		assertEquals(2, branches.size());
		assertTrue(branches.containsKey(TEST_BRANCH_MASTER));
	}
	
	@Test
	public void fetchChildrenFromBranchesFolderReturnsExpectedNumberOfBranches() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		RepoFolder branchesFolder = new RepoFolder(repo.getRoot(),
				repo.getRoot(), "branches", "branches");

		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_REPO_FOLDER,
				branchesFolder);
		Collection<RepoResource> children = handler.execute(connector, para);

		assertEquals(2, children.size());
	}
}
