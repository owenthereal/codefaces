package org.codefaces.core.svn.operations;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.svn.clientadaptor.SVNClientAdaptor;
import org.codefaces.core.svn.clientadaptor.SVNResource;
import org.codefaces.core.svn.connectors.SVNConnector;
import org.eclipse.core.runtime.Assert;

public class SVNFetchFileInfoOperationHandler implements SCMOperationHandler {

	@Override
	public RepoFileInfo execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object file = parameter.getParameter(PARA_REPO_FILE);
		Assert.isTrue(file instanceof RepoFile);
		RepoFile repoFile = (RepoFile) file;

		
		SVNConnector svnConnector = (SVNConnector) connector;
		SVNClientAdaptor svnClient = svnConnector.getSvnClient();
		
		String url = SVNConnector.createSvnUrlFromResource(repoFile);
		
		RepoCredential credential = repoFile.getRoot().getRepo().getCredential();
		
		SVNResource svnResource = svnClient.getResource(url,
				credential.getUser(), credential.getPassword());

		return new RepoFileInfo(svnResource.getContent(),
				svnResource.getMimetype(), null, svnResource.getSize());
	}

}
