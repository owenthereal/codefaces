package org.codefaces.core.github.internal.operations;

import java.net.URI;

import com.google.gson.Gson;

public class GitHubOperationUtil {
	private static final Gson GSON = new Gson();

	public static <T> T fromJson(String jsonString, Class<T> dtoClass) {
		return GSON.fromJson(jsonString, dtoClass);
	}

	public static String makeURI(String context, String... segments) {
		URI contextURI = URI.create(context);
		
		StringBuilder builder = new StringBuilder();
		builder.append(contextURI.toString());
		for (String segment : segments) {
			builder.append("/");
			builder.append(segment);
		}
		
		return URI.create(builder.toString()).toString();
	}
}
