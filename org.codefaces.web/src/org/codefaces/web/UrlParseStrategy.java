package org.codefaces.web;

public interface UrlParseStrategy {
	RepoParameters extractParameters(String url);

	boolean canParse(String url);
}
