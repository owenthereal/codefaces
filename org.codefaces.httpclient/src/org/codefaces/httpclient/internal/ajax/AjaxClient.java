package org.codefaces.httpclient.internal.ajax;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.codefaces.core.connectors.SCMResponseException;
import org.eclipse.swt.widgets.Display;

public class AjaxClient {
	private final AjaxClientWidget ajaxClientWidget;

	protected ConcurrentLinkedQueue<JsonGet> requestQueue = new ConcurrentLinkedQueue<JsonGet>();

	protected ConcurrentHashMap<String, JsonResponse> responseMap = new ConcurrentHashMap<String, JsonResponse>();
	
	private ReentrantLock lock = new ReentrantLock();
	
	public AjaxClient(AjaxClientWidget ajaxClientWidget) {
		this.ajaxClientWidget = ajaxClientWidget;
	}

	private Display getDisplay() {
		return ajaxClientWidget.getDisplay();
	}

	public JsonResponse execute(JsonGet jsonGet) {
		requestQueue.add(jsonGet);
		final String requestId = jsonGet.getRequestId();

		runOnUIThread(new Runnable() {
			@Override
			public void run() {
				waitForResponse(requestId);
			}
		});

		return responseMap.remove(requestId);
	}

	protected void runOnUIThread(Runnable runnable) {
		getDisplay().syncExec(runnable);
	}

	protected void waitForResponse(final String requestId) {
		while (!responseMap.containsKey(requestId)) {
			runEventLoop();
		}
	}

	protected void runEventLoop() {
		if (!getDisplay().readAndDispatch()) {
			getDisplay().sleep();
		}
	}

	public void setJsonResponse(final JsonResponse response) {
		responseMap.put(response.getRequestId(), response);
	}

	public JsonGet nextRequest() {
		return requestQueue.poll();
	}

	public String getResponseBody(String url) throws SCMResponseException {
		try {
			lock.lock();
			JsonResponse resp = execute(new JsonGet(url));

			if (resp.getStatus() != JsonResponse.Status.SUCCESS) {
				throw new SCMResponseException("Errors loading " + url + ".");
			}

			return resp.getContent();
		} finally {
			lock.unlock();
		}
	}
}
