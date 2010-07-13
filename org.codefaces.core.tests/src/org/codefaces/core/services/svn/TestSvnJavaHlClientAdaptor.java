package org.codefaces.core.services.svn;

import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.SVNClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapterFactory;

public class TestSvnJavaHlClientAdaptor {

	private static String CLIENT = JhlClientAdapterFactory.JAVAHL_CLIENT;
	
	/*
	 * Static initialization. 
	 * To initialize the SVN client adaptor factory
	 */
	static {
		try {
			// create an JavaHL adaptor factory
			JhlClientAdapterFactory.setup();
		} catch (SVNClientException e) {
			//can't register the adapter factory
			e.printStackTrace();
		}
	}
	
	
	public static ISVNClientAdapter getClient(){
		ISVNClientAdapter svnClient;
		svnClient = SVNClientAdapterFactory.createSVNClient(CLIENT);
		return svnClient;
	}
	
}
