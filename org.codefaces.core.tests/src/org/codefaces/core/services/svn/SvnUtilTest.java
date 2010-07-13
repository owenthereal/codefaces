package org.codefaces.core.services.svn;


import junit.framework.TestCase;

/**
 * This test case have to be run in RAP test which only supports JUnit3
 * click Run As -> RAP JUnit Test
 */
public class SvnUtilTest extends TestCase{

	public void test_ClientAdaptorIsNotNull(){
		assertNotNull(SvnUtil.getClientAdaptor().getClass().toString());
	}
	
}
