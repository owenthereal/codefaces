package org.codefaces.core.github.internal.operations.dtos;

import java.util.LinkedHashMap;
import java.util.Map;

public class GitHubTagsDTO {
	private Map<String, String> tags = new LinkedHashMap<String, String>();

	public Map<String, String> getTags() {
		return tags;
	}
}
