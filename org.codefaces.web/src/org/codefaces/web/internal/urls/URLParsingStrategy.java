package org.codefaces.web.internal.urls;

public interface URLParsingStrategy {
	URLQueryStrings buildQueryStrings(String url);
	
	String getScmKind(); 

	boolean canParse(String url);
}
