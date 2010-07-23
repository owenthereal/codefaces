package org.codefaces.core.github.internal.operations;

import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GitHubOperationUtil {
	private static final String SEPARATOR = "/";

	private static final String GITHUB_DATE_FORMAT = "yyyy/MM/dd kk:mm:ss Z";
	private static final Gson GSON = new GsonBuilder().setDateFormat(
			GITHUB_DATE_FORMAT).create();

	public static <T> T fromJson(String jsonString, Class<T> dtoClass) {
		return GSON.fromJson(jsonString, dtoClass);
	}

	public static String makeURI(String context, String... segments) {
		URI contextURI = URI.create(context);

		StringBuilder builder = new StringBuilder();
		builder.append(contextURI.toString());
		for (String segment : segments) {
			builder.append(SEPARATOR);
			builder.append(segment);
		}

		return URI.create(builder.toString()).toString();
	}
}
