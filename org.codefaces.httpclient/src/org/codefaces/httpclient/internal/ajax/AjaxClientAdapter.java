package org.codefaces.httpclient.internal.ajax;

import org.codefaces.httpclient.SCMHttpClient;
import org.codefaces.core.connectors.SCMResponseException;

public class AjaxClientAdapter implements SCMHttpClient {

	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public String getResponseBody(String url) throws SCMResponseException {
		return AjaxClientWidget.getCurrent().getClient().getResponseBody(url);
	}
}
