package org.codefaces.core.github.internal.operations;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameter;
import org.codefaces.core.operations.SCMOperationParameters;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchChildrenForRootHandler implements SCMOperationHandler{
	public static final String ID = "org.codefaces.core.operations.SCMOperation.github.fetchChildrenFromRoot";
	
	@Override
	public Collection<RepoResource> execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		
		Object folderRootPara = parameter.getParameter(SCMOperationParameter.REPO_FOLDER);
		Assert.isTrue(folderRootPara instanceof RepoFolderRoot);
		
		RepoFolderRoot root = (RepoFolderRoot)folderRootPara;
		Repo repo = root.getRepo();

		RepoFolder branchesFolder = new RepoFolder(root, root,
				GitHubOperationUtil.makeURI(repo.getFullPath().toString(),
						GitHubOperationConstants.BRANCHES_FOLDER_NAME),
						GitHubOperationConstants.BRANCHES_FOLDER_NAME);
		RepoFolder tagsFolder = new RepoFolder(root, root,
				GitHubOperationUtil.makeURI(repo.getFullPath().toString(),
						GitHubOperationConstants.TAGS_FOLDER_NAME),
						GitHubOperationConstants.TAGS_FOLDER_NAME);

		Set<RepoResource> children = new HashSet<RepoResource>();
		children.add(branchesFolder);
		children.add(tagsFolder);

		return children;
	}

}
