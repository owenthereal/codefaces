package org.codefaces.core.svn.operations;

import java.net.MalformedURLException;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.core.svn.SVNConnector;
import org.codefaces.httpclient.SCMResponseException;
import org.codefaces.httpclient.SCMURLException;
import org.eclipse.core.runtime.Assert;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNUrl;

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

		try {
			SVNUrl svnUrl = new SVNUrl(url);
			ISVNInfo info = svnConnector.getSvnClient().getInfo(svnUrl);
			RepoCredential credential = new RepoCredential(null, username,
					password);

			return new Repo(connector.getKind(), svnUrl.toString(),
					info.getUuid(), credential);

		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		} catch (SVNClientException e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}

}
