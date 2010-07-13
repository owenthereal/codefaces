package org.codefaces.core.services.svn;

import org.tigris.subversion.clientadapter.Activator;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;



public class SvnUtil {
	
	// id of the default svn client adaptor if it is not set in the extension point
	public static String CLIENT_ADAPTOR_ID = "svnkit";

	
	/**
	 * @return a SVN client adaptor  
	 */
	public static ISVNClientAdapter getClientAdaptor(){
		Activator activator = Activator.getDefault();
		return activator.getClientAdapter(CLIENT_ADAPTOR_ID);
	}
}
