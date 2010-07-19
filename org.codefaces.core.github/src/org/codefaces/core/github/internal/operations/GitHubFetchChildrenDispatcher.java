package org.codefaces.core.github.internal.operations;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.models.RepoResourceType;
import org.codefaces.core.operations.SCMOperation;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.eclipse.core.runtime.Assert;

public class GitHubFetchChildrenDispatcher implements
		SCMOperationHandler {

	@Override
	public Collection<RepoResource> execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object folderPara = parameter.getParameter(PARA_REPO_FOLDER);
		Assert.isTrue(folderPara instanceof RepoFolder);

		RepoFolder folder = (RepoFolder) folderPara;

		if (folder.getType() == RepoResourceType.FOLDER_ROOT) {
			return fetchChildrenForRoot((RepoFolderRoot) folder);
		}

		if (folder.getParent() instanceof RepoFolderRoot
				&& StringUtils.equals(GitHubOperationConstants.BRANCHES_FOLDER_NAME,
						folder.getName())) {
			return fetchChildrenForBranchesFolder(folder);
		}

		if (folder.getParent() instanceof RepoFolderRoot
				&& StringUtils.equals(GitHubOperationConstants.TAGS_FOLDER_NAME,
						folder.getName())) {
			return fetchChildrenForTagsFolder(folder);
		}

		return fetchChildrenForFolder(connector, folder);
	}

	protected Collection<RepoResource> executeFetchChildrenOperation(String id,
			RepoFolder folder) {
		SCMOperation operation = SCMOperation.newInstance(folder.getRoot()
				.getRepo().getKind(), id);
		operation.addParameter(PARA_REPO_FOLDER, folder);
		return operation.execute();
	}

	protected Collection<RepoResource> fetchChildrenForBranchesFolder(
			RepoFolder folder) {
		return executeFetchChildrenOperation(
				GitHubFetchBranchesHandler.ID, folder);
	}

	private Collection<RepoResource> fetchChildrenForFolder(
			SCMConnector connector, RepoFolder folder) {
		return executeFetchChildrenOperation(
				GitHubFetchChildrenForFolderHandler.ID, folder);
	}

	protected Collection<RepoResource> fetchChildrenForRoot(RepoFolderRoot root) {
		return executeFetchChildrenOperation(
				GitHubFetchChildrenForRootHandler.ID, root);
	}

	// TODO
	protected Collection<RepoResource> fetchChildrenForTagsFolder(
			RepoFolder folder) {
		return Collections.emptyList();
	}

}
