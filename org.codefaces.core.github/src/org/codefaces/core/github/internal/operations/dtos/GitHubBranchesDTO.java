package org.codefaces.core.github.internal.operations.dtos;

import java.util.LinkedHashMap;
import java.util.Map;

public class GitHubBranchesDTO {
	private Map<String, String> branches = new LinkedHashMap<String, String>();

	public Map<String, String> getBrances() {
		return branches;
	}
}
