package org.codefaces.core.services.svn;

import static org.junit.Assert.*;

import java.util.Collection;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.junit.Before;
import org.junit.Test;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;

public class SvnFetchChildrenQueryTest {
	
	private static final String TEST_URL_WITHOUT_BRANCHES =
		"http://subclipse.tigris.org/svn/subclipse/trunk";
	private static final String TEST_URL_WITH_BRANCHES =
		"http://subclipse.tigris.org/svn/subclipse";
	private static final String TEST_USERNAME =  "guest";
	private static final String TEST_PASSWORD = null;

	class MockSvnFetchChildrenQuery extends SvnFetchChildrenQuery{
		private ISVNClientAdapter testClient;

		public void setClient(ISVNClientAdapter testClient){
			this.testClient = testClient;
		}
		
		@Override
		protected ISVNClientAdapter getSvnClient(){
			return testClient == null? new MockSVNClientAdaptor() : testClient;
		}
	}


	private MockSvnFetchChildrenQuery query;
	
	@Before
	public void setup(){
		query = new MockSvnFetchChildrenQuery();
	}
	
	@Test
	public void childrenReturnedByFetchingFromTheDefaultBranchShouldBeCorrect(){
		RepoCredential mockCredential = new RepoCredential(null, TEST_USERNAME, TEST_PASSWORD);
		Repo mockRepo = new Repo(TEST_URL_WITHOUT_BRANCHES, "id", mockCredential);
		RepoBranch defaultBranch = new RepoBranch(mockRepo, "id", "default", true);
		
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO_RESOURCE, defaultBranch.getRoot());
		
		query.setClient(TestSvnJavaHlClientAdaptor.getClient());
		
		Collection<RepoResource> children = query.execute(null, para);
		assertTrue(children.size() > 0);
	}
	
	@Test
	public void childrenReturnedByFetchingFromNonDefaultBranchShouldBeCorrect(){
		RepoCredential mockCredential = new RepoCredential(null, TEST_USERNAME, TEST_PASSWORD);
		Repo mockRepo = new Repo(TEST_URL_WITH_BRANCHES, "id", mockCredential);
		RepoBranch defaultBranch = new RepoBranch(mockRepo, "id", "default", false);
		
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_REPO_RESOURCE, defaultBranch.getRoot());
		
		query.setClient(TestSvnJavaHlClientAdaptor.getClient());
		
		Collection<RepoResource> children = query.execute(null, para);
		assertTrue(children.size() > 0);
	}
}
