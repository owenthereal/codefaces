package org.codefaces.httpclient.ajax;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.codefaces.core.connectors.SCMResponseException;
import org.eclipse.swt.widgets.Display;

public class AjaxClient {
	private final Display display;

	protected ConcurrentLinkedQueue<JsonGet> requestQueue = new ConcurrentLinkedQueue<JsonGet>();

	protected ConcurrentHashMap<String, JsonResponse> responseMap = new ConcurrentHashMap<String, JsonResponse>();

	public AjaxClient(Display display) {
		this.display = display;
	}

	public JsonResponse execute(JsonGet jsonGet) {
		requestQueue.add(jsonGet);
		String requestId = jsonGet.getRequestId();

		// waiting for response
		while (!responseMap.containsKey(requestId)) {
			runEventLoop();
		}

		return responseMap.remove(requestId);
	}

	protected void runEventLoop() {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		});
	}

	public void setJsonResponse(final JsonResponse response) {
		responseMap.put(response.getRequestId(), response);
	}

	public JsonGet nextRequest() {
		return requestQueue.poll();
	}

	public String getResponseBody(String url) throws SCMResponseException {
		JsonResponse resp = execute(new JsonGet(url));

		if (resp.getStatus() != JsonResponse.Status.SUCCESS) {
			throw new SCMResponseException("Errors loading " + url + ".");
		}

		return resp.getContent();
	}
}
