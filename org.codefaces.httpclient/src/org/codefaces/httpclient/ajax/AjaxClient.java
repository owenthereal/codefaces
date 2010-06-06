package org.codefaces.httpclient.ajax;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.rwt.lifecycle.JSWriter;

public class AjaxClient {
	protected AtomicReference<JsonGet> atomicJsonGet;

	protected AtomicReference<JsonResponse> atomicJsonResponse;

	private static final String JS_SEND_JSONP_REQUEST = "sendJsonpRequest";

	private final JSWriter jsWriter;

	public AjaxClient(JSWriter jsWriter) {
		this.jsWriter = jsWriter;
		atomicJsonGet = new AtomicReference<JsonGet>();
		atomicJsonResponse = new AtomicReference<JsonResponse>();
	}

	public synchronized JsonResponse execute(JsonGet jsonGet) {
		atomicJsonGet.set(jsonGet);

		// waiting for response
		JsonResponse jsonResponse = atomicJsonResponse.get();
		while (jsonResponse == null) {
			try {
				wait(500);
				jsonResponse = atomicJsonResponse.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// release the response object
		atomicJsonResponse.set(null);

		return jsonResponse;
	}

	public void setJsonResponse(JsonResponse response) {
		atomicJsonResponse.set(response);
	}

	public void flush() {
		// waiting for request
		JsonGet cachedJsonGet = atomicJsonGet.getAndSet(null);
		if (cachedJsonGet == null) {
			return;
		}

		sendJsonRequest(cachedJsonGet);
	}

	protected void sendJsonRequest(JsonGet cachedJsonGet) {
		// sending request
		try {
			String url = cachedJsonGet.getUrl();
			int timeout = cachedJsonGet.getTimeout();
			jsWriter.call(JS_SEND_JSONP_REQUEST, new Object[] { url, timeout });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
