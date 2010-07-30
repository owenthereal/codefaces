package org.codefaces.web.internal.urls.github;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codefaces.web.internal.urls.URLQueryStrings;
import org.codefaces.web.internal.urls.URLParsingStrategy;

public class GitHubUrlParseStrategy implements URLParsingStrategy {
	private static final String HTTP_WWW_GITHUB_ORG = "http://www.github.org";

	private static final String HTTP_GITHUB_COM = "http://github.com";

	private static final String OPTIONAL_ENDING_SLASH_PATTERN = "(?:/)?";
	
	private static final Pattern URL_PATTERN = Pattern.compile("((?:"
			+ Pattern.quote(HTTP_WWW_GITHUB_ORG) + "|"
			+ Pattern.quote(HTTP_GITHUB_COM)
			+ ")/[^/]+/[^/]+)(?:/[^/]+/([^/]+).*)?"
			+ OPTIONAL_ENDING_SLASH_PATTERN);


	@Override
	public boolean canParse(String url) {
		return URL_PATTERN.matcher(url).matches();
	}

	@Override
	public URLQueryStrings buildQueryStrings(String url) {
		URLQueryStrings parameters = new URLQueryStrings();

		Matcher matcher = URL_PATTERN.matcher(url);
		if (matcher.matches()) {
			String repoUrl = matcher.group(1);
			String branchName = matcher.group(2);

			if (repoUrl != null) {
				parameters.addParameter(URLQueryStrings.REPO, repoUrl);
			}

			if (branchName != null) {
				parameters.addParameter(URLQueryStrings.BRANCH, branchName);
			}
		}

		return parameters;
	}
}
