package org.codefaces.core.github.operations.dtos;

import java.util.LinkedList;
import java.util.List;

public class GitHubResourcesDTO {
	List<GitHubResourceDTO> tree = new LinkedList<GitHubResourceDTO>();
	
	public List<GitHubResourceDTO> getResources(){
		return tree;
	}
}
