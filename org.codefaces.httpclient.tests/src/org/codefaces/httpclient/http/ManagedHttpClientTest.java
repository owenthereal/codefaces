package org.codefaces.httpclient.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.httpclient.http.ManagedHttpClient;
import org.junit.Before;
import org.junit.Test;

public class ManagedHttpClientTest {
	private ManagedHttpClient client;

	private static final String TEST_URL_GITHUB = "http://github.com/api/v2/json/repos/show/jingweno/ruby_grep/branches";

	@Before
	public void setUp() {
		client = new ManagedHttpClient();
	}

	@Test
	public void test_getResponseBody_SingleThread()
			throws SCMResponseException {
		String body = client.getResponseBody(TEST_URL_GITHUB);
		assertTrue(!StringUtils.isEmpty(body));
	}

	@Test
	public void test_getResponseBody_MultiThreads()
			throws SCMResponseException, InterruptedException {
		String expectedBody = client.getResponseBody(TEST_URL_GITHUB);

		int N = 20;
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(N);

		List<ConnectionWorker> workers = new ArrayList<ConnectionWorker>();
		for (int i = 0; i < N; i++) {
			ConnectionWorker worker = new ConnectionWorker(startSignal,
					doneSignal, client, TEST_URL_GITHUB);
			workers.add(worker);
			new Thread(worker).start();
		}

		startSignal.countDown();
		doneSignal.await();

		for (ConnectionWorker worker : workers) {
			assertEquals(expectedBody, worker.getResponseBody());
		}
	}

	private class ConnectionWorker implements Runnable {
		private final CountDownLatch startSignal;
		private final CountDownLatch doneSignal;
		private final ManagedHttpClient client;
		private String responseBody;

		private final String url;

		public ConnectionWorker(CountDownLatch startSignal,
				CountDownLatch doneSignal, ManagedHttpClient client, String url) {
			this.startSignal = startSignal;
			this.doneSignal = doneSignal;
			this.client = client;
			this.url = url;
		}

		public void run() {
			try {
				startSignal.await();
				doWork(client, url);
				doneSignal.countDown();
			} catch (InterruptedException ex) {
			} 
		}

		private void doWork(ManagedHttpClient client, String url) {
			try {
				responseBody = client.getResponseBody(url);
			} catch (SCMResponseException e) {
				fail(e.getMessage());
			}
		}

		public String getResponseBody() {
			return responseBody;
		}
	}
}
