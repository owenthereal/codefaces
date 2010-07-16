package org.codefaces.core.svn.operations;

import static org.junit.Assert.*;

import java.util.Collection;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.junit.Before;
import org.junit.Test;

public class SvnFetchChildrenQueryTest {
	
	private static String TEST_URL_WITHOUT_BRANCHES = "http://code.djangoproject.com/svn/django/trunk";
	private static String TEST_URL_WITH_BRANCHES = "http://code.djangoproject.com/svn/django";
	private static final String TEST_USERNAME =  null;
	private static final String TEST_PASSWORD = null;
	
	private static final String RESOURCE_FOUND_IN_TRUNK = "README";
	private static final String TEST_BRANCH = "releases";
	private static final String RESOURCE_IN_BRANCH = "1.2.X";

	private SVNFetchChildrenOperationHandler handler;
	private SCMConnector connector;

	private Repo createMockRepo(String url, String username, String password){
		RepoCredential credential = new RepoCredential(null, username, password);
		return new Repo(null, url, url, credential);
	}
	
	@Before
	public void setUp(){
		connector = new MockSCMConnector(TestSvnJavaHlClientAdaptor.getClient());
		handler = new SVNFetchChildrenOperationHandler();
	}
	
	@Test
	public void childrenReturnedByFetchingFromTheDefaultBranchShouldBeCorrect(){
		Repo mockRepo = createMockRepo(TEST_URL_WITHOUT_BRANCHES, TEST_USERNAME, TEST_PASSWORD);
		RepoBranch defaultBranch = new RepoBranch(mockRepo, "id",
				SVNConstants.DEFAULT_BRANCH, true);
		
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_REPO_RESOURCE, defaultBranch.getRoot());
		
		Collection<RepoResource> children = handler.execute(connector, para);
		
		boolean found = false;
		for(RepoResource child: children){
			if(child.getName().equals(RESOURCE_FOUND_IN_TRUNK)){
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
	
	
	@Test
	public void childrenReturnedByFetchingFromNonDefaultBranchShouldBeCorrect(){
		Repo mockRepo = createMockRepo(TEST_URL_WITH_BRANCHES, TEST_USERNAME, TEST_PASSWORD);
		RepoBranch branch = new RepoBranch(mockRepo, "id", TEST_BRANCH, false);
		
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMOperationHandler.PARA_REPO_RESOURCE, branch.getRoot());
		
		Collection<RepoResource> children = handler.execute(connector, para);
		
		boolean found = false;
		for(RepoResource child: children){
			if(child.getName().equals(RESOURCE_IN_BRANCH)){
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
}
