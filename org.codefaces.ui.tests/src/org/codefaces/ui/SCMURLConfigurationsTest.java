package org.codefaces.ui;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SCMURLConfigurationsTest {
	
	private static final String[][] NORMAL_HTML_QUERY_PARAMETERS = {
		{SCMConfigurableElement.REPO_URL.toString().toLowerCase(), "http://testing/url"},
		{SCMConfigurableElement.SCM_KIND.toString().toLowerCase(), "test_kind"},
		{SCMConfigurableElement.USER.toString().toLowerCase(), "test_user"},
		{SCMConfigurableElement.PASSWORD.toString().toLowerCase(), "test_password"},
		{SCMConfigurableElement.BASE_DIRECTORY.toString().toLowerCase(), "test/base/dir"}
	};
	
	private static final Object[][] HTML_QUERY_PARAMETERS_WITH_2_URLS = {
		{
			SCMConfigurableElement.REPO_URL.toString().toLowerCase(), 
			new String[] { "http://testing/url1", "http://testing/url1"}
		}
	};
	
	private static final String[][] HTML_QUERY_PARAMETERS_WITH_UNKNOWN_PARAMETERS = {
		{"unknowParameter1", "unknowValue1"},
		{SCMConfigurableElement.REPO_URL.toString().toLowerCase(), "http://testing/url"},
		{"unknowParameter2", "unknowValue2"}
	};
	
	private static final String TEST_URL = "http://test/url";
	private static final String TEST_KIND = "GitHub";
	
	
	@Test
	public void configurationsShouldBeCorrectlyCreatedFormNormalHTMLQueryParameters()
			throws MalformedURLException {
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		for(int i=0; i<NORMAL_HTML_QUERY_PARAMETERS.length ; i++){
			parameters.put(NORMAL_HTML_QUERY_PARAMETERS[i][0],
					new String[] { NORMAL_HTML_QUERY_PARAMETERS[i][1] });
		}
		
		SCMURLConfiguration configuration = SCMURLConfiguration.fromHTTPParametersMap(parameters);
		assertEquals(NORMAL_HTML_QUERY_PARAMETERS[0][1], configuration.get(SCMConfigurableElement.REPO_URL));
		assertEquals(NORMAL_HTML_QUERY_PARAMETERS[1][1], configuration.get(SCMConfigurableElement.SCM_KIND));
		assertEquals(NORMAL_HTML_QUERY_PARAMETERS[2][1], configuration.get(SCMConfigurableElement.USER));
		assertEquals(NORMAL_HTML_QUERY_PARAMETERS[3][1], configuration.get(SCMConfigurableElement.PASSWORD));
		assertEquals(NORMAL_HTML_QUERY_PARAMETERS[4][1], configuration.get(SCMConfigurableElement.BASE_DIRECTORY));
	}
	
	@Test(expected=MalformedURLException.class)
	public void exceptionShouldBeThrownWhenKnownParametersContainsMultipleValues() throws MalformedURLException{
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put((String)HTML_QUERY_PARAMETERS_WITH_2_URLS[0][0],
				(String[])HTML_QUERY_PARAMETERS_WITH_2_URLS[0][1]);
		SCMURLConfiguration.fromHTTPParametersMap(parameters);
	}
	
	@Test
	public void configurationsShouldSimplyIgnoreUnknownHTMLQueryParamerters() throws MalformedURLException{
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		for(int i=0; i<HTML_QUERY_PARAMETERS_WITH_UNKNOWN_PARAMETERS.length ; i++){
			parameters.put(HTML_QUERY_PARAMETERS_WITH_UNKNOWN_PARAMETERS[i][0],
					new String[] { HTML_QUERY_PARAMETERS_WITH_UNKNOWN_PARAMETERS[i][1] });
		}
		SCMURLConfiguration configuration = SCMURLConfiguration.fromHTTPParametersMap(parameters);
		assertEquals(1, configuration.getConfigurationsMap().size());
		assertEquals(HTML_QUERY_PARAMETERS_WITH_UNKNOWN_PARAMETERS[1][1], configuration.get(SCMConfigurableElement.REPO_URL));
	}
	
	@Test
	public void queryStringFromedShouldContainsParametersAsExpected() throws Exception{
		SCMURLConfiguration configurations = new SCMURLConfiguration();
		configurations.put(SCMConfigurableElement.REPO_URL, TEST_URL);
		configurations.put(SCMConfigurableElement.SCM_KIND, TEST_KIND);
		
		String queryString = configurations.toHTTPQueryString();
		Map<String, List<String>> parsedQueryParametersMap = getParametersMap(queryString);
		assertEquals(
				TEST_URL,
				parsedQueryParametersMap.get(
						SCMConfigurableElement.REPO_URL.toString().toLowerCase()).get(0));
		assertEquals(
				TEST_KIND,
				parsedQueryParametersMap.get(
						SCMConfigurableElement.SCM_KIND.toString().toLowerCase()).get(0));
	}
	
	@Test
	public void queryStringShouldBeNullForEmptyConfiguratin(){
		SCMURLConfiguration configurations = new SCMURLConfiguration();
		String queryString = configurations.toHTTPQueryString();
		assertNull(queryString);
	}
	
	
	private Map<String, List<String>> getParametersMap(String url) throws Exception{
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		String[] urlParts = url.split("\\?");
		if (urlParts.length > 1) {
		    String query = urlParts[1];
		    for (String param : query.split("&")) {
		        String[] pair = param.split("=");
		        String key = URLDecoder.decode(pair[0], "UTF-8");
		        String value = URLDecoder.decode(pair[1], "UTF-8");
		        List<String> values = params.get(key);
		        if (values == null) {
		            values = new ArrayList<String>();
		            params.put(key, values);
		        }
		        values.add(value);
		    }
		}
		return params;
	}
	
}
