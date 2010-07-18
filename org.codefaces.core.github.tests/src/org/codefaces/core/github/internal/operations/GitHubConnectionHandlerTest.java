package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;

import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.GitHubConnectionHandler;
import org.codefaces.core.models.Repo;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.internal.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubConnectionHandlerTest {
	private static final String TEST_REPO_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_REPO_URL_WITH_ENDING_SLASH = "http://github.com/jingweno/ruby_grep/";

	private static final String TEST_REPO_NAME = "ruby_grep";

	private static final String TEST_USER_NAME = "jingweno";

	private GitHubConnectionHandler query;

	private GitHubConnector connector;

	@Before
	public void setUp() {
		connector = new GitHubConnector(new ManagedHttpClient());
		query = new GitHubConnectionHandler();
	}

	@Test
	public void test_execute() throws MalformedURLException {
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_URL, TEST_REPO_URL);
		Repo gitHubRepo = query.execute(connector, para);

		assertEquals(TEST_REPO_URL, gitHubRepo.getUrl());
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME, gitHubRepo.getName());

		para.addParameter(SCMOperationHandler.PARA_URL, TEST_REPO_URL_WITH_ENDING_SLASH);
		gitHubRepo = query.execute(connector, para);

		assertEquals(TEST_REPO_URL_WITH_ENDING_SLASH, gitHubRepo.getUrl());
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME, gitHubRepo.getName());
	}
}
