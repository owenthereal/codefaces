package org.codefaces.core.services.svn;
import static org.junit.Assert.*;

import org.codefaces.core.models.Repo;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.junit.Test;

public class SvnRepoQueryTest {
	private static String TEST_REPO_URL_1 = "http://code.djangoproject.com/svn/django/trunk";
	private static String TEST_REPO_NAME_1 = "code.djangoproject.com/svn/django/trunk";
	
	private static String TEST_REPO_URL_2 = "http://guest@subclipse.tigris.org/svn/subclipse/";
	private static String TEST_REPO_USER_2 = "guest";
	private static String TEST_REPO_NAME_2 = "subclipse.tigris.org/svn/subclipse";
	

	
	@Test
	public void test_execute(){
		//Testing with a URL with normal anonymous user and password
		SvnRepoQuery query1 = new SvnRepoQuery();
		SCMQueryParameter para = SCMQueryParameter.newInstance();
		para.addParameter(SCMQuery.PARA_URL, TEST_REPO_URL_1);
		Repo svnRepo = query1.execute(null, para);
		assertEquals(TEST_REPO_URL_1, svnRepo.getUrl());
		assertNull(svnRepo.getCredential().getUser());
		assertEquals(TEST_REPO_NAME_1, svnRepo.getName());
		
		//Testing with a URL with user name supplied
		SvnRepoQuery query2 = new SvnRepoQuery();
		SCMQueryParameter para2 = SCMQueryParameter.newInstance();
		para2.addParameter(SCMQuery.PARA_URL, TEST_REPO_URL_2);
		Repo svnRepo2 = query2.execute(null, para2);
		assertEquals(TEST_REPO_URL_2, svnRepo2.getUrl());
		assertEquals(TEST_REPO_USER_2, svnRepo2.getCredential().getUser());
		assertNull(svnRepo2.getCredential().getPassword());
		assertEquals(TEST_REPO_NAME_2, svnRepo2.getName());
	}
}
