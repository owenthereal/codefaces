package org.codefaces.core.services.svn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;

import org.codefaces.core.svn.operations.SvnUrl;
import org.junit.Test;

public class SvnUrlTest {
	
	private static String TEST_URL = "svn://svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_HOST = "svn.blender.org";
	private static String TEST_PATH = "/svnroot/bf-blender/trunk/blender";

	private static String TEST_PROTOCOL_URL1 = "svn://svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_PROTOCOL1 = "svn";
	private static String TEST_PROTOCOL_URL2 = "svn+ssh://svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_PROTOCOL2 = "svn+ssh";
	private static String TEST_PROTOCOL_URL3 = "http://svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_PROTOCOL3 = "http";
	private static String TEST_PROTOCOL_URL4 = "https://svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_PROTOCOL4 = "https";
	private static String TEST_PROTOCOL_URL5 = "file://svn.blender.org/svnroot/bf-blender/trunk/blender";
	
	private static String TEST_USERINFO_URL1 = "svn://svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_USERINFO_URL2 = "svn://rubylist@svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_USERINFO_USERNAME2 = "rubylist";
	private static String TEST_USERINFO_URL3 = "svn://rubylist:abyb2yr4@svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_USERINFO_USERNAME3 = "rubylist";
	private static String TEST_USERINFO_PASSWD3 = "abyb2yr4";
	private static String TEST_USERINFO_URL4 = "svn://rubylist:abyb?yr4@svn.blender.org/svnroot/bf-blender/trunk/blender";
	private static String TEST_USERINFO_USERNAME4 = "rubylist";
	private static String TEST_USERINFO_PASSWD4 = "abyb?yr4";

	private static String TEST_PORT_URL = "svn://svn.blender.org:1024/svnroot/bf-blender/trunk/blender";
	private static int TEST_PORT = 1024;
	
	SvnUrl url = null;
	
	@Test
	public void test_constructor() throws Exception{
		//Test protocol
		url = new SvnUrl(TEST_PROTOCOL_URL1);
		assertEquals(TEST_PROTOCOL1, url.getProtocol());
		url = new SvnUrl(TEST_PROTOCOL_URL2);
		assertEquals(TEST_PROTOCOL2, url.getProtocol());
		url = new SvnUrl(TEST_PROTOCOL_URL3);
		assertEquals(TEST_PROTOCOL3, url.getProtocol());
		url = new SvnUrl(TEST_PROTOCOL_URL4);
		assertEquals(TEST_PROTOCOL4, url.getProtocol());
		try{
			url = new SvnUrl(TEST_PROTOCOL_URL5);
			throw new Exception("This should not pass, protocol is not valid");
		}catch(MalformedURLException e){
			//pass
		}
		
		//Test no username
		url = new SvnUrl(TEST_USERINFO_URL1);
		assertNull(url.getUsername());
		assertNull(url.getPassword());
		//Test username
		url = new SvnUrl(TEST_USERINFO_URL2);
		assertEquals(TEST_USERINFO_USERNAME2, url.getUsername());
		assertNull(url.getPassword());
		//Test username with password
		url = new SvnUrl(TEST_USERINFO_URL3);
		assertEquals(TEST_USERINFO_USERNAME3, url.getUsername());
		assertEquals(TEST_USERINFO_PASSWD3, url.getPassword());
		//Test username, password with special sysbol
		url = new SvnUrl(TEST_USERINFO_URL4);
		assertEquals(TEST_USERINFO_USERNAME4, url.getUsername());
		assertEquals(TEST_USERINFO_PASSWD4, url.getPassword());
		
		//test host and path
		url = new SvnUrl(TEST_URL);
		assertEquals(TEST_HOST, url.getHost());
		assertEquals(TEST_PATH, url.getPath());
		
		//test port
		url = new SvnUrl(TEST_URL);
		assertEquals(-1, url.getPort());
		url = new SvnUrl(TEST_PORT_URL);
		assertEquals(TEST_PORT, url.getPort());
	}
}
