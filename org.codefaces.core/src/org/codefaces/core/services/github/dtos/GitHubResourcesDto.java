package org.codefaces.core.services.github.dtos;

import java.util.LinkedList;
import java.util.List;

public class GitHubResourcesDto {
	List<GitHubResourceDto> tree = new LinkedList<GitHubResourceDto>();
	
	public List<GitHubResourceDto> getResources(){
		return tree;
	}
}
