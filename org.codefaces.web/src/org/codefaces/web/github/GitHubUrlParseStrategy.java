package org.codefaces.web.github;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codefaces.web.RepoParameters;
import org.codefaces.web.UrlParseStrategy;

public class GitHubUrlParseStrategy implements UrlParseStrategy {
	private static final String HTTP_WWW_GITHUB_ORG = "http://www.github.org";

	private static final String HTTP_GITHUB_COM = "http://github.com";

	private static final Pattern URL_PATTERN = Pattern.compile("((?:"
			+ Pattern.quote(HTTP_WWW_GITHUB_ORG) + "|"
			+ Pattern.quote(HTTP_GITHUB_COM)
			+ ")/[^/]+/[^/]+)(?:/[^/]+/([^/]+).*)?");

	@Override
	public boolean canParse(String url) {
		return URL_PATTERN.matcher(url).matches();
	}

	@Override
	public RepoParameters extractParameters(String url) {
		RepoParameters parameters = new RepoParameters();

		Matcher matcher = URL_PATTERN.matcher(url);
		if (matcher.matches()) {
			String repoUrl = matcher.group(1);
			String branchName = matcher.group(2);

			if (repoUrl != null) {
				parameters.addParameter(RepoParameters.REPO, repoUrl);
			}

			if (branchName != null) {
				parameters.addParameter(RepoParameters.BRANCH, branchName);
			}
		}

		return parameters;
	}
}
