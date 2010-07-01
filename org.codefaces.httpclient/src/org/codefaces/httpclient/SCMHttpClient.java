package org.codefaces.httpclient;


public interface SCMHttpClient {
	String getResponseBody(String url) throws SCMResponseException;
	
	void dispose();
}
