package org.codefaces.core.svn.internal.operations;

import java.net.URI;
import java.net.URISyntaxException;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.connectors.SCMURLException;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameter;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.svn.internal.clientadaptor.SVNRepoInfo;
import org.codefaces.core.svn.internal.connectors.SVNConnector;
import org.eclipse.core.runtime.Assert;

public class SVNConnectionOperationHandler implements SCMOperationHandler {

	@Override
	public Repo execute(SCMConnector connector, SCMOperationParameters parameter) {
		Object urlPara = parameter.getParameter(SCMOperationParameter.URL);
		Assert.isTrue(urlPara instanceof String);
		Object usernamePara = parameter.getParameter(SCMOperationParameter.USER);
		Assert.isTrue(usernamePara == null || usernamePara instanceof String);
		Object passwordPara = parameter.getParameter(SCMOperationParameter.PASSWORD);
		Assert.isTrue(passwordPara == null || passwordPara instanceof String);

		String url = (String) urlPara;

		try {
			URI uri = new URI(url);
			String username = null;
			// if username in url, assign it first
			if (uri.getUserInfo() != null) {
				username = uri.getUserInfo();
			}
			// now override it if it is passed by parameter
			if (usernamePara != null) {
				username = (String) usernamePara;
			}
			String password = (passwordPara != null) ? (String) passwordPara
					: null;

			SVNConnector svnConnector = (SVNConnector) connector;

			SVNRepoInfo repoInfo = svnConnector.getSvnClient().getRepoInfo(url,
					username, password);

			RepoCredential credential = new RepoCredential(username, password);
			return new Repo(connector.getKind(), repoInfo.getUrl(),
					repoInfo.getUuid(), credential);

		} catch (URISyntaxException e) {
			throw new SCMURLException("Invalid Subversion repository url: " + url);
		}

	}

}
