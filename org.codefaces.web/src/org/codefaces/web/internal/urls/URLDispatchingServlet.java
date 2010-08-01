package org.codefaces.web.internal.urls;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codefaces.web.internal.CodeFacesWebActivator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class URLDispatchingServlet extends HttpServlet {
	private static final String CODE_FACES_ENTRY_POINT_ID = "org.codefaces.ui.entrypoint.codefaces";

	private static final String CODE_FACES_URL = getCodeFacesEntryPointPath();

	private static final String AND = "&";

	private static final String FORWARD_SLASH = "/";

	private static final String EQUAL = "=";

	private static final String PARAMETER_START = "?";

	private static final long serialVersionUID = 25248922633642477L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = req.getRequestURI();
		if (uri.indexOf(FORWARD_SLASH) == 0) {
			uri = uri.substring(1).trim();
		}

		URLQueryStrings parameters = new URLQueryStrings();
		URLParsingStrategy strategy = CodeFacesWebActivator.getDefault()
				.getUrlParseStrategies(uri);
		if (strategy != null) {
			parameters = strategy.buildQueryStrings(uri);
		}

		String redirectUrl = createRepoUrl(req, parameters);
		resp.sendRedirect(redirectUrl);
	}

	private String createRepoUrl(HttpServletRequest req,
			URLQueryStrings parameters) {
		StringBuilder url = new StringBuilder();
		url.append(req.getContextPath());
		url.append(req.getServletPath());
		url.append(CODE_FACES_URL);

		Map<String, String> parametersMap = parameters.getParametersMap();
		if (!parametersMap.isEmpty()) {
			url.append(PARAMETER_START);
		}

		Iterator<Entry<String, String>> entryIterator = parametersMap
				.entrySet().iterator();
		while (entryIterator.hasNext()) {
			Entry<String, String> next = entryIterator.next();
			url.append(next.getKey());
			url.append(EQUAL);
			url.append(next.getValue());
			if (entryIterator.hasNext()) {
				url.append(AND);
			}
		}

		return url.toString();
	}

	private static String getCodeFacesEntryPointPath() {
		IConfigurationElement[] configElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"org.eclipse.rap.ui", "branding");
		for (IConfigurationElement element : configElements) {
			String defaultEntryPointId = element
					.getAttribute("defaultEntrypointId");
			if (StringUtils.equals(defaultEntryPointId,
					CODE_FACES_ENTRY_POINT_ID)) {
				return "/" + element.getAttribute("servletName");
			}
		}

		return "/explore";
	}
}
