package org.codefaces.httpclient.internal.ajax;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AjaxClientTest {
	@Test
	public void executeReturnsResponse() {
		final JsonGet request = new JsonGet("url");
		final JsonResponse response = new JsonResponse(request.getRequestId(),
				JsonResponse.Status.SUCCESS, "content");
		AjaxClient client = new AjaxClient(null) {
			@Override
			protected void runOnUIThread(Runnable runnable) {
				runnable.run();
			}

			@Override
			protected void runEventLoop() {
				setJsonResponse(response);
			}
		};
		JsonResponse result = client.execute(request);

		assertEquals(response, result);
	}
}
