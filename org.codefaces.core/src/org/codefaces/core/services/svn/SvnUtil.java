package org.codefaces.core.services.svn;

import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.SVNClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapterFactory;


public class SvnUtil {
	
	// there are a few client type. Including JavaHL, SVNKit and simply 
	// command line
	public static String CLIENT_TYPE = JhlClientAdapterFactory.JAVAHL_CLIENT;

	/*
	 * Static initialization. 
	 * To initialize the SVN client adaptor factory
	 */
	static {
		try {
			// create an JavaHL adaptor factory
			JhlClientAdapterFactory.setup();
		} catch (SVNClientException e) {
			// can't register the adapter factory
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return a SVN client adaptor implementation based on CLIENT_TYPE  
	 */
	public static ISVNClientAdapter getClient(){
		ISVNClientAdapter svnClient;
		svnClient = SVNClientAdapterFactory.createSVNClient(CLIENT_TYPE);
		return svnClient;
	}
	
}
