package org.codefaces.web.urls.googlecode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codefaces.ui.SCMConfigurableElement;
import org.codefaces.ui.SCMURLConfiguration;
import org.codefaces.web.urls.URLParsingStrategy;

public class GoogleCodeSvnPageParsingStrategy implements URLParsingStrategy{
	private static final String PROTOCOL_HTTP = "http";
	private static final String PROJECT_PATTERN = "(?:[^.]+)";
	
	private static final String OPTIONAL_SUBPAGES_PATTERN = "(?:/(?:[^/]+))*";	
	private static final String OPTIONAL_ENDING_SLASH_PATTERN = "(?:/)?";
	
	private static final String SVN_URL_PATTERN = Pattern.quote(PROTOCOL_HTTP)
			+ Pattern.quote("://") + PROJECT_PATTERN
			+ Pattern.compile(GoogleCodeConstants.SVN_DOMAIN);
	
	private static final Pattern URL_PATTERN = Pattern.compile(
			"("+ SVN_URL_PATTERN + ")" +
			OPTIONAL_SUBPAGES_PATTERN +
			OPTIONAL_ENDING_SLASH_PATTERN
	);
	@Override
	public SCMURLConfiguration buildConfigurations(String url) {
		SCMURLConfiguration config = new SCMURLConfiguration();

		Matcher matcher = URL_PATTERN.matcher(url);
		if (matcher.matches()) {
			String svnUrl = matcher.group(1);
			config.put(SCMConfigurableElement.REPO_URL, svnUrl);
			config.put(SCMConfigurableElement.SCM_KIND, GoogleCodeConstants.SVN_SCM_KIND);
		}
		return config;
	}

	@Override
	public boolean canParse(String url) {
		return URL_PATTERN.matcher(url).matches();
	}

}
