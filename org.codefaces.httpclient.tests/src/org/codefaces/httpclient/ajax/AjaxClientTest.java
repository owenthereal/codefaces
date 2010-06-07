package org.codefaces.httpclient.ajax;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AjaxClientTest {
	private AjaxClient client;

	private volatile Thread thread;

	@Before
	public void setUp() throws Exception {
		client = new AjaxClient(null, null) {
			@Override
			protected void sendJsonRequest(JsonGet cachedJsonGet) {
				this.atomicJsonResponse.set(new JsonResponse(
						JsonResponse.STATUS.SUCCESS, "content"));
			}
			
			@Override
			protected void runEventLoop() {
				// do nothing
			}
		};

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (true) {
					client.flush();
				}
			}
		};

		thread = new Thread(runnable);
		thread.start();
	}

	@After
	public void tearDown() throws Exception {
		thread = null;
	}

	@Test
	public void test_execute() {
		JsonGet jsonGet = new JsonGet("url");
		JsonResponse response = client.execute(jsonGet);
		assertEquals(JsonResponse.STATUS.SUCCESS, response.getStatus());
	}
}
