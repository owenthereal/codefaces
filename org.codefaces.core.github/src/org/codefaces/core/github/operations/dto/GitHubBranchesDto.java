package org.codefaces.core.github.operations.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class GitHubBranchesDto {
	private Map<String, String> branches = new LinkedHashMap<String, String>();

	public Map<String, String> getBrances() {
		return branches;
	}
}