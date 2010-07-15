package org.codefaces.core.services.svn;
import static org.junit.Assert.*;

import java.util.UUID;

import org.codefaces.core.models.Repo;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.httpclient.SCMResponseException;
import org.junit.Before;
import org.junit.Test;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;


public class SvnRepoQueryTest {
	private static final String TEST_PASSWORD = "password";
	private static final String TEST_USERNAME = "username";
	private static final String TEST_NORMAL_URL = "http://code.djangoproject.com/svn/django/trunk";
	private static final String TEST_NO_SUCH_URL = "http://svn.nosuchurl.org/svn";
	private static final String TEST_NO_PERMISSION = "https://secure.jms1.net";

	// mock query
	class MockSvnRepoQuery extends SvnRepoQuery{
		private ISVNClientAdapter testClient;
		
		public void setClient(ISVNClientAdapter testClient){
			this.testClient = testClient;
		}
		
		@Override
		protected ISVNClientAdapter getSvnClient(){
			return testClient == null? new MockSVNClientAdaptor() : testClient;
		}
	}
	
	//A mock client
	class ReoQueryMockSVNClientAdaptor extends MockSVNClientAdaptor{
		private boolean isUrlValid;
		
		public ReoQueryMockSVNClientAdaptor(boolean isUrlValid){
			this.isUrlValid = isUrlValid;
		}
		
		@Override
		public ISVNInfo getInfo(SVNUrl url) throws SVNClientException {
			return getInfo(url, null, null);
		}

		@Override
		//if isValid is false, throw an exception
		public ISVNInfo getInfo(SVNUrl url, SVNRevision revision,
				SVNRevision peg) throws SVNClientException {
			if(isUrlValid) {
				MockSVNInfo info = new MockSVNInfo();
				info.setUrlString(url.toString());
				info.setUuid(UUID.randomUUID().toString());
				return info;
			}
			else throw new SVNClientException();
		}
	}
	
	private MockSvnRepoQuery query;


	@Before
	public void setUp(){
		query = new MockSvnRepoQuery();
	}

	@Test
	public void credentialShouldBeSetWhenUsernameAndPasswordArePassedAsParameters(){
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_URL, TEST_NORMAL_URL);
		para.addParameter(SCMQuery.PARA_USERNAME, TEST_USERNAME);
		para.addParameter(SCMQuery.PARA_PASSWORD, TEST_PASSWORD);
		
		query.setClient(new ReoQueryMockSVNClientAdaptor(true));
		Repo svnRepo = query.execute(null, para);
		assertEquals(TEST_NORMAL_URL, svnRepo.getUrl());
		assertEquals(TEST_USERNAME, svnRepo.getCredential().getUser());
		assertEquals(TEST_PASSWORD, svnRepo.getCredential().getPassword());
	}
	
	@Test
	public void credentialShouldNotBeSetWhenUsernameAndPasswordAreNotPassedAsParameters(){
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_URL, TEST_NORMAL_URL);
		
		query.setClient(new ReoQueryMockSVNClientAdaptor(true));
		Repo svnRepo = query.execute(null, para);
		assertNull(svnRepo.getCredential().getUser());
		assertNull(svnRepo.getCredential().getPassword());
	}

	@Test(expected = SCMResponseException.class)
	public void throwExceptionWhenNoSuchRepository(){
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_URL, TEST_NO_SUCH_URL);
		//we use a real client here
		query.setClient(TestSvnJavaHlClientAdaptor.getClient());
		query.execute(null, para);
	}
	
	@Test(expected = SCMResponseException.class)
	public void throwExceptionWhenNoPermission(){
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_URL, TEST_NO_PERMISSION);
		//we use a real client here
		query.setClient(TestSvnJavaHlClientAdaptor.getClient());
		query.execute(null, para);
	}
	
	@Test(expected = SCMResponseException.class)
	public void throwExceptionWhenAdaptorThrowsException(){
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_URL, TEST_NORMAL_URL);
		query.setClient(new ReoQueryMockSVNClientAdaptor(false));
		query.execute(null, para);
	}

}
