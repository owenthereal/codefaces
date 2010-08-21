package org.codefaces.core.services;

import java.util.Collection;

import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.core.internal.CodeFacesCoreActivator;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperation;
import org.codefaces.core.operations.SCMOperationParameter;

public class SCMService {

	public Repo connect(String kind, String url, String username, String password) {
		SCMOperation operation = SCMOperation.newInstance(kind,
				SCMOperation.CONNECTION_OPERAION);
		if(url != null){
			operation.addParameter(SCMOperationParameter.URL, url.trim());
		}
		if(username != null){
			operation.addParameter(SCMOperationParameter.USER, username.trim());
		}
		if(password != null){
			operation.addParameter(SCMOperationParameter.PASSWORD, password.trim());
		}
		return operation.execute();
	}

	public Collection<RepoResource> fetchChildren(RepoFolder parent)
			throws SCMResponseException {
		SCMOperation operation = SCMOperation.newInstance(parent.getRoot()
				.getRepo().getKind(), SCMOperation.FETCH_CHILDREN_OPERAION);
		operation.addParameter(SCMOperationParameter.REPO_FOLDER, parent);

		return operation.execute();
	}

	public RepoFileInfo fetchFileInfo(RepoFile file)
			throws SCMResponseException {
		SCMOperation operation = SCMOperation.newInstance(file.getRoot()
				.getRepo().getKind(), SCMOperation.FETCH_FILE_INFO_OPERAION);
		operation.addParameter(SCMOperationParameter.REPO_FILE, file);

		return operation.execute();
	}

	public static SCMService getCurrent() {
		return CodeFacesCoreActivator.getDefault().getSCMService();
	}
}
