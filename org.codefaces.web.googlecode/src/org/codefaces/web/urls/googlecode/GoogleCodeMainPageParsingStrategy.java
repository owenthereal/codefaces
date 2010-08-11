package org.codefaces.web.urls.googlecode;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.codefaces.ui.SCMURLConfiguration;
import org.codefaces.web.urls.URLParsingStrategy;

//TODO extract scmKind to extensionPoint
public class GoogleCodeMainPageParsingStrategy implements URLParsingStrategy{
	
	private static final String PROTOCOL_HTTP = "http";
	private static final String PROJECT_PATTERN = "([^/]+)";

	private static final String MAIN_PAGE_URL_DOMAIN = "code.google.com/p/";
	
	private static final String OPTIONAL_SUBPAGES_PATTERN = "(?:/(?:[^/]+))*";	
	private static final String OPTIONAL_ENDING_SLASH_PATTERN = "(?:/)?";
	
	private static final Pattern URL_PATTERN = Pattern.compile(
			Pattern.quote(PROTOCOL_HTTP) +
			Pattern.quote("://") +
			Pattern.quote(MAIN_PAGE_URL_DOMAIN) + PROJECT_PATTERN +
			OPTIONAL_SUBPAGES_PATTERN +
			OPTIONAL_ENDING_SLASH_PATTERN
	);
	
	private GoogleCodeSvnPageParsingStrategy svnStrategy;
	
	public GoogleCodeMainPageParsingStrategy(){
		svnStrategy = new GoogleCodeSvnPageParsingStrategy(); 
	}

	/**
	 * Since a Google code must contains a SVN repository. Current
	 * implementation only delegate all the URL to SVN URL parser.
	 */
	@Override
	public SCMURLConfiguration buildConfigurations(String url) {
		String projectName = getProjectNameFromUrl(url);
		String svnUrl = reconstructUrl(projectName, GoogleCodeConstants.SVN_SCM_KIND);
		if(isUrlExists(svnUrl)){
			return svnStrategy.buildConfigurations(svnUrl);
		}
		return null;
	}

	@Override
	public boolean canParse(String url) {
		return URL_PATTERN.matcher(url).matches();
	}
	
	protected String getProjectNameFromUrl(String url){
		Matcher matcher = URL_PATTERN.matcher(url);
		String urlProjectName = null;
		if (matcher.matches()) {
			urlProjectName  = matcher.group(1);
		}
		return urlProjectName;
	}

	//Construct a URL that can be parsed by sub-parsing strategies
	protected String reconstructUrl(String projectName, String scmKind){
		if(StringUtils.equals(GoogleCodeConstants.SVN_SCM_KIND, scmKind)){
			return "http://" + projectName + GoogleCodeConstants.SVN_DOMAIN; 
		}
		if(StringUtils.equals(GoogleCodeConstants.HG_SCM_KIND, scmKind)){
			return "http://" + projectName + GoogleCodeConstants.HG_DOMAIN; 
		}
		return null;
	}
	
	private boolean isUrlExists(String url){
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url)
					.openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}
}
