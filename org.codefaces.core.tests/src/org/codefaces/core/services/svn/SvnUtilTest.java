package org.codefaces.core.services.svn;


import org.junit.Ignore;

import junit.framework.TestCase;

/**
 * This test case have to be run in RAP test which only supports JUnit3
 * click Run As -> RAP JUnit Test
 */
@Ignore
public class SvnUtilTest extends TestCase{

	public void test_ClientAdaptorIsNotNull(){
		assertNotNull(SvnUtil.getClientAdaptor().getClass().toString());
	}
	
}
