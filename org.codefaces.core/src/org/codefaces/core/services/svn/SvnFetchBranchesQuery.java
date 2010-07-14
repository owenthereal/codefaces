package org.codefaces.core.services.svn;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoBranch;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.httpclient.SCMHttpClient;
import org.eclipse.core.runtime.Assert;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

public class SvnFetchBranchesQuery extends SvnQuery implements
		SCMQuery<Collection<RepoBranch>> {

	@Override
	public Collection<RepoBranch> execute(SCMHttpClient client,
			SCMQueryParameter parameter) {
		//Assert the parameter
		Object repoPara = parameter.getParameter(PARA_REPO);
		Assert.isTrue(repoPara instanceof Repo);
		Repo repo = (Repo)repoPara;
		
		String url = repo.getUrl();
		String branchUrl = url + "/" + SvnConstants.BRANCH_DIRECTORY;
		String username = repo.getCredential().getUser();
		String password = repo.getCredential().getPassword();
		
		ISVNClientAdapter svnClient = getSvnClient();
		if(username!=null){ svnClient.setUsername(username); }
		if(password!=null){ svnClient.setPassword(password); }
		
		Set<RepoBranch> branches = new LinkedHashSet<RepoBranch>();
		
		//Test if the branches directory exists
		try{
			ISVNDirEntry[] branchEntries = svnClient.getList(new SVNUrl(branchUrl),
					SVNRevision.HEAD, false);
			for(ISVNDirEntry branchEntry: branchEntries){
				if(branchEntry.getNodeKind() == SVNNodeKind.DIR){
					RepoBranch branch = new RepoBranch(repo,
							generateRepoResourceID(repo, branchEntry),
							branchEntry.getPath(), false); 
					branches.add(branch);
				}
			}
		} catch (Exception e) {
			// Do nothing, there is no "branches" directory
		}
		
		//Create a default branch
		String defaultBranchId = repo.getId() + ":"
				+ SvnConstants.DEFAULT_BRANCH;
		RepoBranch defaultBranch = new RepoBranch(repo, defaultBranchId,
				SvnConstants.DEFAULT_BRANCH, true);
		branches.add(defaultBranch);

		return branches;
	}

}
