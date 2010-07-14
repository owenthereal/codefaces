package org.codefaces.core.services.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.httpclient.SCMHttpClient;
import org.codefaces.httpclient.SCMResponseException;
import org.eclipse.core.runtime.Assert;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;

import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

public class SvnFetchChildrenQuery extends SvnQuery implements
		SCMQuery<Collection<RepoResource>> {

	@Override
	public Collection<RepoResource> execute(SCMHttpClient client,
			SCMQueryParameter parameter) {
		Object resPara = parameter.getParameter(PARA_REPO_RESOURCE);
		Assert.isTrue(resPara instanceof RepoResource);

		RepoResource container = (RepoResource)resPara;
		

		String svnUrl = SvnUtil.createSvnUrlFromResource(container);
		List<RepoResource> children = new ArrayList<RepoResource>();
		
		try {
			ISVNClientAdapter svnClient = getSvnClient();
			
			RepoFolderRoot folderRoot = container.getRoot();
			Repo repo = folderRoot.getRepo();
			
			String username = repo.getCredential().getUser();
			String password = repo.getCredential().getPassword();
			if(username!=null){ svnClient.setUsername(username); }
			if(password!=null){ svnClient.setPassword(password); }
			
			ISVNDirEntry[] entries = svnClient.getList(new SVNUrl(svnUrl),
					SVNRevision.HEAD, false);
			for(ISVNDirEntry entry: entries){
				RepoResource child = createRepoResourceFromType(
						entry.getNodeKind(), folderRoot, container,
						generateRepoResourceID(repo, entry),
						entry.getPath());
				children.add(child);
			}
		} catch (Exception e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
		return children;
	}

	private RepoResource createRepoResourceFromType(SVNNodeKind svnNodeKind,
			RepoFolderRoot root, RepoResource parent, String id, String name) {
		if (svnNodeKind == SVNNodeKind.FILE) {
			return new RepoFile(root, parent, id, name);
		}
		else if (svnNodeKind == SVNNodeKind.DIR) {
			return new RepoFolder(root, parent, id, name);
		}

		return null;
	}

}
