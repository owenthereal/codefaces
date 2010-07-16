package org.codefaces.core.svn.operations;

import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.SVNClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapterFactory;

// Since SvnKitAdaptor bundle does not expose its packages, using it will require a
// Plugin test. For simplicity, we use JavaHL for testing
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
