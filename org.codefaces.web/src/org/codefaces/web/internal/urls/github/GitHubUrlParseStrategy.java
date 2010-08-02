package org.codefaces.web.internal.urls.github;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codefaces.ui.SCMConfigurableElement;
import org.codefaces.ui.SCMURLConfiguration;
import org.codefaces.web.internal.urls.URLParsingStrategy;

public class GitHubUrlParseStrategy implements URLParsingStrategy {
	//TODO use GitHubConstants
	private static final String BRANCHES_FOLDER_NAME = "branches";
	
	private static final String SCM_KIND = "GitHub";
	
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
	public SCMURLConfiguration buildConfigurations(String url) {
		SCMURLConfiguration config = new SCMURLConfiguration();

		Matcher matcher = URL_PATTERN.matcher(url);
		if (matcher.matches()) {
			String repoUrl = matcher.group(1);
			String branchName = matcher.group(2);

			if (repoUrl != null) {
				config.put(SCMConfigurableElement.URL, repoUrl);
			}

			if (branchName != null) {
				config.put(
						SCMConfigurableElement.BASE_DIRECTORY,
						BRANCHES_FOLDER_NAME + "/" + branchName);
			}
			
			config.put(SCMConfigurableElement.KIND, SCM_KIND);
		}

		return config;
	}

	@Override
	public String getScmKind() {
		return SCM_KIND;
	}
}
