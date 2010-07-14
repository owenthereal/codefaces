package org.codefaces.core.github.operations.dto;

import java.util.LinkedList;
import java.util.List;

public class GitHubResourcesDto {
	List<GitHubResourceDto> tree = new LinkedList<GitHubResourceDto>();
	
	public List<GitHubResourceDto> getResources(){
		return tree;
	}
}
