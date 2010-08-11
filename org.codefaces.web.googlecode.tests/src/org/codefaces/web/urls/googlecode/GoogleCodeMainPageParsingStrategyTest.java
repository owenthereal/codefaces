package org.codefaces.web.urls.googlecode;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class GoogleCodeMainPageParsingStrategyTest {
	private static final String TEST_MAIN_PAGE_SVN_URL = "http://code.google.com/p/aurorasdk";
	private static final String TEST_MAIN_PAGE_SVN_URL_WITH_TRAILING_SLASH = TEST_MAIN_PAGE_SVN_URL + "/";
	private static final String TEST_OTHER_PAGE_SVN_URL = "http://code.google.com/p/aurorasdk/issues/detail?id=1";

	private static final String TEST_SVN_PROJECT_NAME = "aurorasdk";
	private static final String TEST_SVN_SCM_KIND = "Subversion";
	private static final String TEST_SVN_REPO_URL = "http://aurorasdk.googlecode.com/svn";
	
	private static final String TEST_HG_PROJECT_NAME = "ocropus";
	private static final String TEST_HG_SCM_KIND = "Mercurial";
	private static final String TEST_HG_REPO_URL = "http://ocropus.googlecode.com/hg";

	
	private GoogleCodeMainPageParsingStrategy strategy;

	@Before
	public void setUp(){
		strategy = new GoogleCodeMainPageParsingStrategy();
	}
	
	@Test
	public void canParseSvnUrl(){
		assertTrue(strategy.canParse(TEST_MAIN_PAGE_SVN_URL));
		assertTrue(strategy.canParse(TEST_MAIN_PAGE_SVN_URL_WITH_TRAILING_SLASH));
		assertTrue(strategy.canParse(TEST_OTHER_PAGE_SVN_URL));
	}
	
	@Test
	public void svnUrlShouldBeCorrectlyReconstructedWhenSvnUrlIsProvided(){
		assertEquals(TEST_SVN_REPO_URL, strategy.reconstructUrl(
				TEST_SVN_PROJECT_NAME, TEST_SVN_SCM_KIND));	
	}
	
	@Test
	public void hgUrlShouldBeCorrectlyReconstructedWhenSvnUrlIsProvided(){
		assertEquals(TEST_HG_REPO_URL, strategy.reconstructUrl(
				TEST_HG_PROJECT_NAME, TEST_HG_SCM_KIND));	
	}
	
	@Test
	public void projectNameShouldBeObtainedWhenMainPageUrlIsProvided(){
		assertEquals(TEST_SVN_PROJECT_NAME,
				strategy.getProjectNameFromUrl(TEST_MAIN_PAGE_SVN_URL));
	}
	
	@Test
	public void projectNameShouldBeObtainedWhenMainPageUrlWithTrailingSlashIsProvided(){
		assertEquals(TEST_SVN_PROJECT_NAME,
				strategy.getProjectNameFromUrl(TEST_MAIN_PAGE_SVN_URL_WITH_TRAILING_SLASH));
	}
	
	@Test
	public void projectNameShouldBeObtainedWhenOtherPageUrlIsProvided(){
		assertEquals(TEST_SVN_PROJECT_NAME,
				strategy.getProjectNameFromUrl(TEST_OTHER_PAGE_SVN_URL));
	}

	
}
