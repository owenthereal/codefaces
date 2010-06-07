package org.codefaces.httpclient.http;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;


public class ManagedHttpClient {
	private static class IdleConnectionMonitorThread extends Thread {

		private final ClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(ClientConnectionManager connMgr) {
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(5000);
						// Close expired connections
						connMgr.closeExpiredConnections();
						// Optionally, close connections
						// that have been idle longer than 30 sec
						connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
				// terminate
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}

	private volatile IdleConnectionMonitorThread connectionMonitorThread;

	private HttpClient httpClient;

	private HttpClient createClient() {
		HttpParams params = new BasicHttpParams();
		// Increase max total connection to 200
		ConnManagerParams.setMaxTotalConnections(params, 200);
		// Increase default max connection per route to 20
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);

		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);

		connectionMonitorThread = new IdleConnectionMonitorThread(cm);
		connectionMonitorThread.start();

		return new DefaultHttpClient(cm, params);
	}

	public ManagedHttpClient() {
		httpClient = createClient();
	}

	public String getResponseBody(String url) throws RepoResponseException {
		try {
			HttpGet httpGet = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute(httpGet, responseHandler);
		} catch (HttpResponseException exception) {
			throw handleHttpExceptionStatus(exception);
		} catch (IOException exception) {
			throw new RepoResponseException(exception.getMessage(), exception);
		} 
	}

	private RepoResponseException handleHttpExceptionStatus(
			HttpResponseException exception) {
		int status = exception.getStatusCode();
		switch (status) {
		case HttpStatus.SC_NOT_FOUND:
			return new RepoResponseException(status, "HTTP Error: " + status
					+ ". Request Resource Not Found.", exception);
		case HttpStatus.SC_UNAUTHORIZED:
		case HttpStatus.SC_FORBIDDEN:
			return new RepoResponseException(status, "HTTP Error: " + status
					+ "Unauthorized Request.", exception);
		}

		return new RepoResponseException(status, "HTTP Error: " + status,
				exception);
	}

	public HttpClient getClient() {
		return httpClient;
	}

	public void dispose() {
		connectionMonitorThread.shutdown();
		httpClient.getConnectionManager().shutdown();
	}
}
