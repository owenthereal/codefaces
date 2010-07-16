package org.codefaces.core.github.operations;

import static org.junit.Assert.assertEquals;

import org.codefaces.core.github.connectors.GitHubConnector;
import org.codefaces.core.github.operations.GitHubConnectionOperationHandler;
import org.codefaces.core.models.Repo;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class GitHubRepoQueryTest {
	
	private static final String TEST_HTTP_REPO_URL = "http://github.com/jingweno/ruby_grep";
	private static final String TEST_HTTP_REPO_URL_WITH_ENDING_SLASH = "http://github.com/jingweno/ruby_grep/";
	private static final String TEST_REPO_NAME_FOR_HTTP_URL = "ruby_grep";
	private static final String TEST_OWNER_NAME_FOR_HTTP_URL = "jingweno";
	
	private static final String TEST_REPO_GIT_URL = "git://github.com/smilebase/org.eclipse.mylyn.github.git";
	private static final String TEST_REPO_NAME_FOR_GIT_URL = "org.eclipse.mylyn.github";
	private static final String TEST_REPO_OWNER_FOR_GIT_URL = "smilebase";
	
	
	private GitHubConnectionOperationHandler query;

	private GitHubConnector connector;

	@Before
	public void setUp() {
		connector = new GitHubConnector(new ManagedHttpClient());
		query = new GitHubConnectionOperationHandler();
	}

	@Test
	public void urlParsedCorrectlyForHttpUrl(){
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_URL, TEST_HTTP_REPO_URL);
		Repo gitHubRepo = query.execute(connector, para);

		assertEquals(TEST_HTTP_REPO_URL, gitHubRepo.getUrl());
		assertEquals(TEST_OWNER_NAME_FOR_HTTP_URL, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME_FOR_HTTP_URL, gitHubRepo.getName());
	}
	
	@Test
	public void urlParsedCorrectlyForHttpUrlWithTrailingSlash(){
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_URL, TEST_HTTP_REPO_URL_WITH_ENDING_SLASH);
		Repo gitHubRepo = query.execute(connector, para);

		assertEquals(TEST_HTTP_REPO_URL_WITH_ENDING_SLASH, gitHubRepo.getUrl());
		assertEquals(TEST_OWNER_NAME_FOR_HTTP_URL, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME_FOR_HTTP_URL, gitHubRepo.getName());		
	}
	
	@Test
	public void urlParsedCorrectlyForGitURL() {
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_URL, TEST_REPO_GIT_URL);
		Repo gitHubRepo = query.execute(connector, para);
		
		assertEquals(TEST_REPO_GIT_URL, gitHubRepo.getUrl());
		assertEquals(TEST_REPO_OWNER_FOR_GIT_URL, gitHubRepo.getCredential().getOwner());
		assertEquals(TEST_REPO_NAME_FOR_GIT_URL, gitHubRepo.getName());
	}
}
