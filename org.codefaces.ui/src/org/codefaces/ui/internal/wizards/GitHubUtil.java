package org.codefaces.ui.internal.wizards;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;

public class GitHubUtil {
	
	private static final String BRANCHES_ID = "branches";
	private static final String TAGS_ID = "tags";

	/**
	 * Determine the given resource is a GitHub branch or GitHub Tag
	 */
	public static boolean isBranchOrTag(RepoResource repoResource){
		if(repoResource instanceof RepoFolder){
			if (StringUtils.equals(BRANCHES_ID, repoResource.getParent().getName())
					|| StringUtils.equals(TAGS_ID, repoResource.getParent().getName())) {
				return true;
			}
		}
		return false;
	}
}
