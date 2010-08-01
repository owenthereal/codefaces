package org.codefaces.core.svn.internal.operations;

import static org.junit.Assert.*;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.SCMConfigurableElements;
import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.svn.internal.operations.SVNFetchChildrenOperationHandler;
import org.junit.Before;
import org.junit.Test;

public class SVNFetchChildrenOperationHandlerTest {
	
	private static String TEST_URL_IN_NORMAL_STRUCTURE = "http://code.djangoproject.com/svn/django";
	private static final String TEST_USERNAME =  null;
	private static final String TEST_PASSWORD = null;
	
	private static final String TAGS_DIRECTORY = "tags";
	private static final String BRANCHES_DIRECTORY = "branches";
	private static final String TRUNK_DIRECTORY = "trunk";
		
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
	public void fetchChildrenFromRootShouldReturnExpectedNumberOfChildren(){
		Repo mockRepo = createMockRepo(TEST_URL_IN_NORMAL_STRUCTURE,
				TEST_USERNAME, TEST_PASSWORD);
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMConfigurableElements.REPO_FOLDER, mockRepo.getRoot());
		Collection<RepoResource> children = handler.execute(connector, para);
		
		boolean hasTrunk = false;
		boolean hasBranches = false;
		boolean hasTags = false;
		
		for(RepoResource child : children){
			if(StringUtils.equals(TRUNK_DIRECTORY, child.getName())){ hasTrunk = true; }
			if(StringUtils.equals(BRANCHES_DIRECTORY, child.getName())){ hasBranches = true; }
			if(StringUtils.equals(TAGS_DIRECTORY, child.getName())){ hasTags = true; }
		}
		
		assertTrue(hasTrunk);
		assertTrue(hasBranches);
		assertTrue(hasTags);
	}
	
	@Test
	public void fetchChildrenFromFolderShouldReturnExpectedNumberOfChildren(){
		Repo mockRepo = createMockRepo(TEST_URL_IN_NORMAL_STRUCTURE,
				TEST_USERNAME, TEST_PASSWORD);
		RepoFolder mockFolder = new RepoFolder(mockRepo.getRoot(),
				mockRepo.getRoot(), TAGS_DIRECTORY, TAGS_DIRECTORY);
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMConfigurableElements.REPO_FOLDER, mockFolder);
		Collection<RepoResource> children = handler.execute(connector, para);
		
		assertEquals(2, children.size());
	}	
	
}
