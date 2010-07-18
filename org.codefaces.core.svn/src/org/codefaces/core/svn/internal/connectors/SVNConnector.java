package org.codefaces.core.svn.internal.connectors;


import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.svn.internal.clientadaptor.SVNClientAdaptor;


public class SVNConnector implements SCMConnector {
	private static final String KIND = "Subversion";

	@Override
	public String getKind() {
		return KIND;
	}

	/**
	 * @return a session-based svn client
	 */
	public SVNClientAdaptor getSvnClient() {
		return SVNClientAdaptor.getCurrent();
	}
	
}
