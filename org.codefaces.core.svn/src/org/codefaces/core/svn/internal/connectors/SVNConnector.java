package org.codefaces.core.svn.internal.connectors;

import java.text.SimpleDateFormat;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.svn.internal.clientadaptor.SVNClientAdaptor;
import org.codefaces.core.svn.internal.clientadaptor.SVNDirectoryEntry;
import org.codefaces.core.svn.internal.operations.SVNConstants;

public class SVNConnector implements SCMConnector {
	private static final String KIND = "Subversion";

	@Override
	public String getKind() {
		return KIND;
	}

	/**
	 * @return a session-based svn client
	 */
	public SVNClientAdaptor getSvnClient() {
		return SVNClientAdaptor.getCurrent();
	}
	
	/**
	 * @return an ID for a given SVN Dir entry
	 */
	public static String generateRepoResourceID(SVNDirectoryEntry entry){
		String url = entry.getUrl();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = fmt.format(entry.getLastChangedDate());
		String id = url + ":" + timestamp;
		return id;
	}
	
	/**
	 * we construct the SVN URL by getting the URL of root and append it with
	 * the full path of the resource.
	 * 
	 * @return a SVN URL for of the resource
	 */
	public static String createSvnUrlFromResource(RepoResource resource) {
		RepoFolderRoot repoFolderRoot = resource.getRoot();
		Repo repo = repoFolderRoot.getRepo();
		String svnUrl;
		// if it is default branch, easy
		if (repoFolderRoot.getBranch().isMaster()) {
			svnUrl = repo.getUrl() + resource.getFullPath();
		} else {
			// if not, we have to append it with "/branches"
			StringBuilder urlBuilder = new StringBuilder();
			String branchName = resource.getRoot().getBranch().getName();
			urlBuilder.append(repo.getUrl());
			urlBuilder.append('/');
			urlBuilder.append(SVNConstants.BRANCH_DIRECTORY);
			urlBuilder.append('/');
			urlBuilder.append(branchName);
			urlBuilder.append(resource.getFullPath());
			svnUrl = urlBuilder.toString();
		}
		return svnUrl;
	}

}
