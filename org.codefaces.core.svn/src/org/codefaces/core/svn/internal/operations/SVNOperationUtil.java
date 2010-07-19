package org.codefaces.core.svn.internal.operations;

import java.text.SimpleDateFormat;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.svn.internal.clientadaptor.SVNDirectoryEntry;

public class SVNOperationUtil {
	private static final String SEPARATOR = "/";

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
	 * Construct the SVN URL of a given resource. We create it by getting the
	 * URL of root and append it with the full path of the resource.
	 *
	 * @param the given resource, which is a RepoFile or a RepoRoot
	 * @return a SVN URL for of the resource
	 */
	public static String createSvnUrlFromResource(RepoResource resource) {
		RepoFolderRoot repoFolderRoot = resource.getRoot();
		Repo repo = repoFolderRoot.getRepo();
		String svnUrl;

		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(repo.getUrl());
		urlBuilder.append(SEPARATOR);
		urlBuilder.append(resource.getFullPath());
		svnUrl = urlBuilder.toString();
		
		return svnUrl;
	}
}
