package org.codefaces.httpclient.ajax;

import static org.junit.Assert.assertEquals;

import org.codefaces.httpclient.internal.ajax.AjaxClient;
import org.codefaces.httpclient.internal.ajax.JsonGet;
import org.codefaces.httpclient.internal.ajax.JsonResponse;
import org.junit.Test;

public class AjaxClientTest {
	@Test
	public void test_execute() {
		final JsonGet jsonGet = new JsonGet("url");
		AjaxClient client = new AjaxClient(null) {
			@Override
			protected void runEventLoop() {
				// mocking the response
				setJsonResponse(new JsonResponse(jsonGet.getRequestId(),
						JsonResponse.Status.SUCCESS, "content"));
			}
		};
		JsonResponse response = client.execute(jsonGet);
		assertEquals(JsonResponse.Status.SUCCESS, response.getStatus());
	}
}
