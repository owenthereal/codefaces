package org.codefaces.core.services.svn;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.httpclient.SCMHttpClient;
import org.codefaces.httpclient.SCMResponseException;
import org.codefaces.httpclient.SCMURLException;
import org.eclipse.core.runtime.Assert;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNUrl;

public class SvnRepoQuery extends SvnQuery implements SCMQuery<Repo> {

	
	@Override
	public Repo execute(SCMHttpClient client, SCMQueryParameter parameter) {
		//Assert the parameters
		Object urlPara = parameter.getParameter(PARA_URL);
		Assert.isTrue(urlPara instanceof String);
		Object usernamePara = parameter.getParameter(PARA_USERNAME);
		Assert.isTrue(usernamePara == null || usernamePara instanceof String);
		Object passwordPara = parameter.getParameter(PARA_PASSWORD);
		Assert.isTrue(passwordPara == null || usernamePara instanceof String);

		String url = (String) urlPara;
		String username = (usernamePara != null)? (String)usernamePara : null;
		String password = (passwordPara != null)? (String)passwordPara : null;
		
		try {
			SVNUrl svnUrl = new SVNUrl(url);
			ISVNInfo info = getSvnClient().getInfo(svnUrl);
			RepoCredential credential = new RepoCredential(null, username, password);
			
			return new Repo(svnUrl.toString(), info.getUuid(), credential);
			
		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		} catch (SVNClientException e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}

}
