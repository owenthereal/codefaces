package org.codefaces.core.svn.internal.operations;

import org.codefaces.core.svn.internal.clientadaptor.SVNClientAdaptor;
import org.codefaces.core.svn.internal.connectors.SVNConnector;

public class MockSVNConnector extends SVNConnector{
	
	class MockSVNClientAdaptor extends SVNClientAdaptor{
		public MockSVNClientAdaptor(){};
	}
	
	private SVNClientAdaptor client;
	
	public MockSVNConnector(){
		this.client = new MockSVNClientAdaptor();
	}
	
	
	public SVNClientAdaptor getSvnClient() {
		return client;
	}
	
}
