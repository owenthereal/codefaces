package org.codefaces.core.services;

import java.util.Collection;

import org.codefaces.core.impl.CodeFacesCoreActivator;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperation;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.httpclient.SCMResponseException;

public class SCMService {
	public Repo connect(String kind, String url) {
		SCMOperation operation = SCMOperation.newInstance(kind,
				SCMOperation.CONNECTION_OPERAION);
		operation.addParameter(SCMOperationHandler.PARA_URL, url.trim());

		return operation.execute();
	}

	public Collection<RepoBranch> fetchBranches(Repo repo) {
		SCMOperation operation = SCMOperation.newInstance(repo.getKind(),
				SCMOperation.FETCH_BRANCHES_OPERAION);
		operation.addParameter(SCMOperationHandler.PARA_REPO, repo);

		return operation.execute();
	}

	public Collection<RepoResource> fetchChildren(RepoResource parent)
			throws SCMResponseException {
		SCMOperation operation = SCMOperation.newInstance(parent.getRoot()
				.getRepo().getKind(), SCMOperation.FETCH_CHILDREN_OPERAION);
		operation.addParameter(SCMOperationHandler.PARA_REPO_RESOURCE, parent);

		return operation.execute();
	}

	public RepoFileInfo fetchFileInfo(RepoFile file)
			throws SCMResponseException {
		SCMOperation operation = SCMOperation.newInstance(file.getRoot()
				.getRepo().getKind(), SCMOperation.FETCH_FILE_INFO_OPERAION);
		operation.addParameter(SCMOperationHandler.PARA_REPO_FILE, file);

		return operation.execute();
	}

	public static SCMService getCurrent() {
		return CodeFacesCoreActivator.getDefault().getSCMService();
	}
}
