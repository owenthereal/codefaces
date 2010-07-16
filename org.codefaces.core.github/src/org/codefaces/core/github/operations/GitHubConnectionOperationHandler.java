package org.codefaces.core.github.operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoCredential;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.codefaces.httpclient.SCMURLException;
import org.eclipse.core.runtime.Assert;

public class GitHubConnectionOperationHandler implements SCMOperationHandler {
	private static final String HTTP_WWW_GITHUB_ORG = "http://www.github.org";
	private static final String HTTP_GITHUB_COM = "http://github.com";
	private static final String OPTIONAL_ENDING_SLASH_PATTERN = "(?:/)?";
	private static final Pattern HTTP_URL_PATTERN = Pattern.compile("(?:"
			+ Pattern.quote(HTTP_WWW_GITHUB_ORG) + "|"
			+ Pattern.quote(HTTP_GITHUB_COM) + ")/([^/]+)/([^/]+)"
			+ OPTIONAL_ENDING_SLASH_PATTERN);
	
	private static final String GIT_GITHUB_ORG = "git://github.org";
	private static final String GIT_GITHUB_COM = "git://github.com";
	private static final Pattern GIT_URL_PATTERN = Pattern.compile("(?:"
			+ Pattern.quote(GIT_GITHUB_ORG) + "|"
			+ Pattern.quote(GIT_GITHUB_COM) + ")/([^/]+)/([^/]+)\\.git");
	

	@Override
	public Repo execute(SCMConnector connector, SCMOperationParameters parameter) {
		Object urlPara = parameter.getParameter(PARA_URL);
		Assert.isTrue(urlPara instanceof String);

		String url = (String) urlPara;
		Matcher httpMatcher = HTTP_URL_PATTERN.matcher(url);
		Matcher gitMatcher = GIT_URL_PATTERN.matcher(url);
		Matcher matcher = httpMatcher.matches() ? httpMatcher : gitMatcher;
		
		if (matcher.matches()) {
			String owner = matcher.group(1);
			String repoName = matcher.group(2);

			RepoCredential credential = new RepoCredential(owner, null, null);

			return new Repo(connector.getKind(), url, repoName, credential);
		}

		throw new SCMURLException("Invalid repository url: " + url);
	}
}
