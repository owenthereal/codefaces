package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationParameter;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.internal.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubFetchChildrenForRootHandlerTest {
	private static final String KIND_GIT_HUB = "GitHub";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_OWNER_NAME = "jingweno";

	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private GitHubFetchChildrenForRootHandler handler;

	private GitHubConnector connector;

	@Before
	public void setUp() {
		connector = new GitHubConnector(new ManagedHttpClient());
		handler = new GitHubFetchChildrenForRootHandler();
	}

	@Test
	public void fetchRepoFolderRootChildrenReturnsBranchesFolderAndTagsFolder() {
		Repo repo = new Repo(KIND_GIT_HUB, TEST_REPO_URL, TEST_REPO_NAME,
				new RepoCredential(null, null));
		repo.setProperty(GitHubOperationConstants.GITHUB_OWNER, TEST_OWNER_NAME);
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationParameter.REPO_FOLDER,
				repo.getRoot());

		Collection<RepoResource> children = handler.execute(connector, para);

		assertEquals(2, children.size());

		boolean hasBranchesFolder = false;
		boolean hasTagsFolder = false;
		for (RepoResource child : children) {
			if (StringUtils.equals("branches", child.getName())) {
				hasBranchesFolder = true;
			}

			if (StringUtils.equals("tags", child.getName())) {
				hasTagsFolder = true;
			}
		}

		assertTrue(hasBranchesFolder);
		assertTrue(hasTagsFolder);
	}
}
