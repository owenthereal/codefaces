package org.codefaces.httpclient.ajax;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.codefaces.httpclient.RepoResponseException;
import org.codefaces.httpclient.internal.CodeFacesHttpClientActivator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.swt.widgets.Display;

public class AjaxClient {
	protected AtomicReference<JsonGet> atomicJsonGet;

	protected AtomicReference<JsonResponse> atomicJsonResponse;

	private static final String JS_SEND_JSONP_REQUEST = "sendJsonpRequest";

	private final JSWriter jsWriter;

	private final Display display;

	public AjaxClient(Display display, JSWriter jsWriter) {
		this.display = display;
		this.jsWriter = jsWriter;
		atomicJsonGet = new AtomicReference<JsonGet>();
		atomicJsonResponse = new AtomicReference<JsonResponse>();
	}

	public synchronized JsonResponse execute(JsonGet jsonGet) {
		atomicJsonGet.set(jsonGet);

		// waiting for response
		JsonResponse jsonResponse = atomicJsonResponse.get();
		while (jsonResponse == null) {
			runEventLoop();
			jsonResponse = atomicJsonResponse.get();
		}

		// release the response object
		atomicJsonResponse.set(null);

		return jsonResponse;
	}

	protected void runEventLoop() {
		if (!display.readAndDispatch()) {
			display.sleep();
		}
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
			IStatus status = new Status(Status.ERROR,
					CodeFacesHttpClientActivator.PLUGIN_ID,
					"Errors occurs when sending ajax request to "
							+ cachedJsonGet.getUrl(), e);
			CodeFacesHttpClientActivator.getDefault().getLog().log(status);
		}
	}

	public String getResponseBody(String url) throws RepoResponseException {
		JsonResponse resp = execute(new JsonGet(url));

		if (resp.getStatus() != JsonResponse.STATUS.SUCCESS) {
			throw new RepoResponseException("Errors loading " + url + ".");
		}

		return resp.getContent();
	}
}
