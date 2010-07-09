package org.codefaces.core.services.svn;

import java.net.MalformedURLException;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.httpclient.SCMHttpClient;
import org.codefaces.httpclient.SCMURLException;
import org.eclipse.core.runtime.Assert;

public class SvnRepoQuery implements SCMQuery<Repo> {

	
	@Override
	public Repo execute(SCMHttpClient client, SCMQueryParameter parameter) {
		Object urlPara = parameter.getParameter(PARA_URL);
		Assert.isTrue(urlPara instanceof String);

		String url = (String) urlPara;
		try {
			SvnUrl svnUrl = new SvnUrl(url);
			
			String username = svnUrl.getUsername();
			String password = svnUrl.getPassword();
			
			String repoName = svnUrl.getHost()+svnUrl.getPath();
			RepoCredential credential = new RepoCredential(null, username, password);
			return new Repo(url, repoName, credential);
			
		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		}
	}

}
