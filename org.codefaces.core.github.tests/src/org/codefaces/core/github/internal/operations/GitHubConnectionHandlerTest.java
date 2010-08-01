package org.codefaces.core.github.internal.operations;

import static org.junit.Assert.assertEquals;

import org.codefaces.core.SCMConfigurableElements;
import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.core.github.internal.connectors.GitHubConnector;
import org.codefaces.core.github.internal.operations.GitHubConnectionHandler;
import org.codefaces.core.models.Repo;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.internal.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubConnectionHandlerTest {
	private static final String TEST_REPO_NORMAL_URL = "http://github.com/jingweno/ruby_grep";

	private static final String TEST_REPO_URL_WITH_ENDING_SLASH = "http://github.com/jingweno/ruby_grep/";
	
	private static final String TEST_NO_SUCH_URL = "http://github.com/jingweno/nosuchurl";
	
	private static final String TEST_PRIVATE_REPO = "http://github.com/jingweno/code_faces";

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
	public void repoInfomationShouldBeCorrectlySetForNormalURL(){
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMConfigurableElements.REPO_URL, TEST_REPO_NORMAL_URL);
		Repo gitHubRepo = query.execute(connector, para);

		assertEquals(TEST_REPO_NORMAL_URL, gitHubRepo.getUrl());
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME, gitHubRepo.getName());
	}
	
	@Test
	public void repoInfomationShouldBeCorrectlySetForURLWithTrailingSlash(){
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMConfigurableElements.REPO_URL, TEST_REPO_URL_WITH_ENDING_SLASH);
		Repo gitHubRepo = query.execute(connector, para);

		assertEquals(TEST_REPO_URL_WITH_ENDING_SLASH, gitHubRepo.getUrl());
		assertEquals(TEST_USER_NAME, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME, gitHubRepo.getName());
	}
	
	@Test(expected = SCMResponseException.class)
	public void throwExceptionWhenNoSuchRepository(){
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMConfigurableElements.REPO_URL, TEST_NO_SUCH_URL);
		query.execute(connector, para);
	}
	
	@Test(expected = SCMResponseException.class)
	public void throwExceptionWhenNoPermission(){
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMConfigurableElements.REPO_URL, TEST_PRIVATE_REPO);
		query.execute(connector, para);
	}
}
