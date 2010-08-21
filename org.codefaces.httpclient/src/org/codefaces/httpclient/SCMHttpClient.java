package org.codefaces.httpclient;

import org.codefaces.core.connectors.SCMResponseException;


public interface SCMHttpClient {
	String getResponseBody(String url) throws SCMResponseException;
	
	void dispose();
}
