package org.codefaces.core.svn.operations;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.svn.clientadaptor.SVNRepoInfo;
import org.codefaces.core.svn.connectors.SVNConnector;
import org.eclipse.core.runtime.Assert;

public class SVNConnectionOperationHandler implements SCMOperationHandler {

	@Override
	public Repo execute(SCMConnector connector, SCMOperationParameters parameter) {
		Object urlPara = parameter.getParameter(PARA_URL);
		Assert.isTrue(urlPara instanceof String);
		Object usernamePara = parameter.getParameter(PARA_USERNAME);
		Assert.isTrue(usernamePara == null || usernamePara instanceof String);
		Object passwordPara = parameter.getParameter(PARA_PASSWORD);
		Assert.isTrue(passwordPara == null || usernamePara instanceof String);

		String url = (String) urlPara;
		String username = (usernamePara != null) ? (String) usernamePara : null;
		String password = (passwordPara != null) ? (String) passwordPara : null;
		SVNConnector svnConnector = (SVNConnector) connector;
		
		SVNRepoInfo repoInfo = svnConnector.getSvnClient().getRepoInfo(url,
				username, password);
		RepoCredential credential = new RepoCredential(null, username,
				password);
		return new Repo(connector.getKind(), repoInfo.getUrl(),
				repoInfo.getUuid(), credential);
	}

}
