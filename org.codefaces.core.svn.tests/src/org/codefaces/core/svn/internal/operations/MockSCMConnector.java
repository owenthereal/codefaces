package org.codefaces.core.svn.internal.operations;

import org.codefaces.core.svn.internal.clientadaptor.SVNClientAdaptor;
import org.codefaces.core.svn.internal.connectors.SVNConnector;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;

/**
 * A Mock SCMConnector which opened the clientAdaptor
 */
public class MockSCMConnector extends SVNConnector {

	private SVNClientAdaptor clientAdaptor;
	
	class MockSVNClientAdaptor extends SVNClientAdaptor{
		public MockSVNClientAdaptor(ISVNClientAdapter client) {
			super(client);
		}
	}

	public MockSCMConnector(ISVNClientAdapter clientAdaptor){
		this.clientAdaptor = new MockSVNClientAdaptor(clientAdaptor);
	}
	
	@Override
	public SVNClientAdaptor getSvnClient() {
		return clientAdaptor;
	}
}
