package org.codefaces.httpclient;


public interface CodeFacesHttpClient {
	String getResponseBody(String url) throws RepoResponseException;
	
	void dispose();
}
