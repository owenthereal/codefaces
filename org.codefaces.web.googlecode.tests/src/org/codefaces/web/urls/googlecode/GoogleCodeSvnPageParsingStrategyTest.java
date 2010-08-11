package org.codefaces.web.urls.googlecode;
import static org.junit.Assert.*;

import org.codefaces.ui.SCMConfigurableElement;
import org.codefaces.ui.SCMURLConfiguration;
import org.codefaces.web.urls.googlecode.GoogleCodeSvnPageParsingStrategy;
import org.junit.Before;
import org.junit.Test;


public class GoogleCodeSvnPageParsingStrategyTest {
	private static final String TEST_REPO_URL = "http://aurorasdk.googlecode.com/svn";
	private static final String TEST_SCM_KIND = "Subversion";

	private static final String TEST_SVN_MAIN_PAGE_URL = "http://aurorasdk.googlecode.com/svn";
	private static final String TEST_SVN_MAIN_PAGE_URL_WITH_TRAILING_SLASH = TEST_SVN_MAIN_PAGE_URL + "/";
	private static final String TEST_SVN_OTHER_PAGE_URL = "http://aurorasdk.googlecode.com/svn/trunk/%20aurorasdk%20--username%20jingweno/";
	
	
	private GoogleCodeSvnPageParsingStrategy strategy;

	@Before
	public void setUp(){
		strategy = new GoogleCodeSvnPageParsingStrategy();
	}
	
	@Test
	public void urlShouldBeCorrectlyParsedWhenMainPageSVNUrlIsGiven(){
		SCMURLConfiguration config = strategy.buildConfigurations(TEST_SVN_MAIN_PAGE_URL);
		assertEquals(TEST_REPO_URL, config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(TEST_SCM_KIND, config.get(SCMConfigurableElement.SCM_KIND));
		assertNull(config.get(SCMConfigurableElement.USER));
	}
	
	@Test
	public void urlShouldBeCorrectlyParsedWhenMainPageWithTrailingSlashSVNUrlIsGiven(){
		SCMURLConfiguration config = strategy
				.buildConfigurations(TEST_SVN_MAIN_PAGE_URL_WITH_TRAILING_SLASH);
		assertEquals(TEST_REPO_URL, config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(TEST_SCM_KIND, config.get(SCMConfigurableElement.SCM_KIND));
		assertNull(config.get(SCMConfigurableElement.USER));
	}
	
	@Test
	public void urlShouldBeCorrectlyParsedWhenOtherPageSVNUrlIsGiven(){
		SCMURLConfiguration config = strategy.buildConfigurations(TEST_SVN_OTHER_PAGE_URL);
		assertEquals(TEST_REPO_URL, config.get(SCMConfigurableElement.REPO_URL));
		assertEquals(TEST_SCM_KIND, config.get(SCMConfigurableElement.SCM_KIND));
		assertNull(config.get(SCMConfigurableElement.USER));
	}
}
