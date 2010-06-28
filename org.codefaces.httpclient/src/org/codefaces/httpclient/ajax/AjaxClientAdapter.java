package org.codefaces.httpclient.ajax;

import org.codefaces.httpclient.CodeFacesHttpClient;
import org.codefaces.httpclient.RepoResponseException;

public class AjaxClientAdapter implements CodeFacesHttpClient {

	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public String getResponseBody(String url) throws RepoResponseException {
		return AjaxClientWidget.getCurrent().getClient().getResponseBody(url);
	}
}
