package org.codefaces.core.svn.internal.operations;

import static org.junit.Assert.*;

import org.codefaces.core.SCMConfigurableElements;
import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.svn.internal.operations.SVNFetchFileInfoOperationHandler;

import org.junit.Before;
import org.junit.Test;

public class SVNFetchFileInfoOperationHandlerTest {
	
	private static final String SEPERATOR = "/";

	private static final String TEST_USERNAME =  null;
	private static final String TEST_PASSWORD = null;
	
	private static final String TEST_URL_IN_TRUNK = "http://code.djangoproject.com/svn/django/trunk";
	private static final String TEST_FILEPATH_IN_TRUNK = "/README";
	private static final String TEST_PREFIX_FOR_TESTFILE_IN_TRUNK = "Django";
		
	private static final String TEST_URL_IN_NORMAL_STRUCTION = "http://code.djangoproject.com/svn/django";
	private static final String TEST_FILEPATH_IN_SOME_FOLDER = "/branches/0.96-bugfixes/MANIFEST.in";
	private static final String TEST_PREFIX_FOR_TESTFILE_IN_SOME_FOLDER = "include AUTHORS";
	
	private SVNFetchFileInfoOperationHandler handler;
	private SCMConnector connector;

	@Before
	public void setUp(){
		connector = new MockSCMConnector(TestSvnJavaHlClientAdaptor.getClient());
		handler = new SVNFetchFileInfoOperationHandler();
	}
	
	
	/**
	 * @return a mock file for testing
	 */
	private RepoFile createMockFile(String repoUrl, String username,
			String password, String fileFullPath){ 
		RepoCredential credential = new RepoCredential(null, username, password);
		Repo repo = new Repo(null, repoUrl, repoUrl, credential);
		RepoFolderRoot root = repo.getRoot();
		
		String[] segments = fileFullPath.split(SEPERATOR);
		
		RepoFolder parent = repo.getRoot();
		for(int i = 1; i < segments.length - 1; i++){
			String folderName = segments[i];
			RepoFolder folder = new RepoFolder(root, parent, parent.getId()
					+ "/" + folderName, folderName);
			parent = folder;
		}
		String fileName = segments[segments.length-1];
		RepoFile file = new RepoFile(root, parent, fileName, fileName);
		return file;
	}
	
	@Test
	public void fetchFileInfoShouldGetFileContentForFileInRoot(){
		RepoFile mockFile = createMockFile(TEST_URL_IN_TRUNK, TEST_USERNAME,
				TEST_PASSWORD, TEST_FILEPATH_IN_TRUNK);
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMConfigurableElements.REPO_FILE, mockFile);
		RepoFileInfo info = handler.execute(connector, para);
		
		assertTrue(info.getContent().startsWith(TEST_PREFIX_FOR_TESTFILE_IN_TRUNK));
	}
	
	@Test
	public void fetchFileInfoShouldGetFileContentForFileInFolder(){
		RepoFile mockFile = createMockFile(TEST_URL_IN_NORMAL_STRUCTION, TEST_USERNAME,
				TEST_PASSWORD, TEST_FILEPATH_IN_SOME_FOLDER);
		SCMOperationParameters para = SCMOperationParameters.newInstance();
		para.addParameter(SCMConfigurableElements.REPO_FILE, mockFile);
		RepoFileInfo info = handler.execute(connector, para);
		
		assertTrue(info.getContent().startsWith(TEST_PREFIX_FOR_TESTFILE_IN_SOME_FOLDER));
	}

}
