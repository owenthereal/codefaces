package org.codefaces.web.internal.urls;

public interface URLParsingStrategy {
	URLQueryStrings buildQueryStrings(String url);

	boolean canParse(String url);
}
