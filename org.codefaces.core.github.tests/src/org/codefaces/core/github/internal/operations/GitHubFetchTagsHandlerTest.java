package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.dtos.GitHubTagsDTO;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationParameter;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.internal.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubFetchTagsHandlerTest {
	private static final String KIND_GIT_HUB = "GitHub";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_TAGS_URL = "http://github.com/api/v2/json/repos/show/jingweno/ruby_grep/tags";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_TAGS = "release1";
	
	private GitHubFetchTagsHandler handler;

	private GitHubConnector connector;

	@Before
	public void createHandler() {
		connector = new GitHubConnector(new ManagedHttpClient());
		handler = new GitHubFetchTagsHandler();
	}
	
	@Test
	public void createFetchTagsURL() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		String githubShowUrl = handler.createFetchTagsURL(repo);

		assertEquals(TEST_TAGS_URL, githubShowUrl);
	}
	
	@Test
	public void getBranchesDto() {
		GitHubTagsDTO tagsDto = handler.getTagsDto(connector,
				TEST_TAGS_URL);
		Map<String, String> tags = tagsDto.getTags();

		assertEquals(1, tags.size());
		assertTrue(tags.containsKey(TEST_TAGS));
	}
	
	@Test
	public void fetchChildrenFromTagsFolderReturnsExpectedNumberOfTags() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(TEST_OWNER_NAME, null, null));
		RepoFolder branchesFolder = new RepoFolder(repo.getRoot(),
				repo.getRoot(), "tags", "tags");

		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationParameter.REPO_FOLDER,
				branchesFolder);
		Collection<RepoResource> children = handler.execute(connector, para);

		assertEquals(1, children.size());
	}
}
