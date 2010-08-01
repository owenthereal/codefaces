package org.codefaces.core.svn.internal.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codefaces.core.SCMConfigurableElements;
import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.operations.SCMOperationHandler;
import org.codefaces.core.operations.SCMOperationParameters;
import org.eclipse.core.runtime.Assert;
import org.codefaces.core.svn.internal.clientadaptor.SVNDirectoryEntry;
import org.codefaces.core.svn.internal.clientadaptor.SVNResourceKind;
import org.codefaces.core.svn.internal.connectors.SVNConnector;

public class SVNFetchChildrenOperationHandler implements SCMOperationHandler {

	@Override
	public Collection<RepoResource> execute(SCMConnector connector,
			SCMOperationParameters parameter) {
		Object folderPara = parameter.getParameter(SCMConfigurableElements.REPO_FOLDER);
		Assert.isTrue(folderPara instanceof RepoFolder);

		RepoFolder folder = (RepoFolder) folderPara;

		Repo repo = folder.getRoot().getRepo();
		String username = repo.getCredential().getUser();
		String password = repo.getCredential().getPassword();
		
		String svnUrl = SVNOperationUtil.createSvnUrlFromResource(folder);
		SVNConnector svnConnector = (SVNConnector) connector;
		
		SVNDirectoryEntry[] entries = svnConnector.getSvnClient()
				.getDirectoryEntries(svnUrl, username, password);
		
		List<RepoResource> children = new ArrayList<RepoResource>();
		for(SVNDirectoryEntry entry: entries){
			RepoResource child = createRepoResourceFromSVNDirectoryEntry(
					svnConnector, entry, folder);
			if(child == null){
				throw new SCMResponseException("Unknown SVN resource type for "
						+ entry.getUrl());
			}
			children.add(child);
		}

		return children;
	}

	private RepoResource createRepoResourceFromSVNDirectoryEntry(
			SVNConnector svnConnector, SVNDirectoryEntry entry, RepoResource parent) {
		RepoFolderRoot root = parent.getRoot();
		String id = SVNOperationUtil.generateRepoResourceID(entry);
		if (entry.getResourceKind() == SVNResourceKind.FILE) {
			return new RepoFile(root, parent, id, entry.getName());
		} else if (entry.getResourceKind() == SVNResourceKind.DIRECTORY) {
			return new RepoFolder(root, parent, id, entry.getName());
		}
		return null;
	}

}
