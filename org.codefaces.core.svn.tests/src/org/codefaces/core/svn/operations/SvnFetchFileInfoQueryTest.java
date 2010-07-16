package org.codefaces.core.svn.operations;

import static org.junit.Assert.*;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.core.services.svn.MockSVNClientAdaptor;
import org.codefaces.core.services.svn.TestSvnJavaHlClientAdaptor;
import org.junit.Before;
import org.junit.Test;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;

public class SvnFetchFileInfoQueryTest {
	private static final String TEST_URL_WITHOUT_BRANCHES =
		"http://code.djangoproject.com/svn/django/trunk";
	private static final String TEST_URL_WITH_BRANCHES =
		"http://subclipse.tigris.org/svn/subclipse";
	private static final String TEST_USERNAME =  null;
	private static final String TEST_PASSWORD = null;
	private static final String TEST_FILENAME = "README";
	
	class MockSvnFetchFileInfoQuery extends SvnFetchFileInfoQuery{
		private ISVNClientAdapter testClient;

		public void setClient(ISVNClientAdapter testClient){
			this.testClient = testClient;
		}
		
		@Override
		protected ISVNClientAdapter getSvnClient(){
			return testClient == null? new MockSVNClientAdaptor() : testClient;
		}
	}
	
	private MockSvnFetchFileInfoQuery query;
	
	@Before
	public void setup(){
		query = new MockSvnFetchFileInfoQuery();
	}
	
	
	@Test
	public void gettingFileInDefaultBranch(){
		RepoCredential credential = new RepoCredential(null, TEST_USERNAME, TEST_PASSWORD);
		Repo repo = new Repo(TEST_URL_WITHOUT_BRANCHES, "repoId", credential);
		RepoBranch defaultBranch = new RepoBranch(repo, "branchId", "default", true);
		RepoFile file = new RepoFile(defaultBranch.getRoot(), defaultBranch.getRoot(),
				"fileId",  TEST_FILENAME);
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO_FILE, file);
		//using a real client is more convenience
		query.setClient(TestSvnJavaHlClientAdaptor.getClient());
		RepoFileInfo fileIndo = query.execute(null, para);
		assertEquals("text", fileIndo.getMimeType());
	}
	
	@Test
	public void gettingFileOtherThanDefaultBranch(){
		throw new RuntimeException();
	}
	
	
}
