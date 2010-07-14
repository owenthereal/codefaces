package org.codefaces.core.services.svn;

import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFolderRoot;
import org.codefaces.core.models.RepoResource;
import org.tigris.subversion.clientadapter.Activator;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;



public class SvnUtil {

	/**
	 * @return a SVN client adaptor. 
	 */
	public static ISVNClientAdapter getClientAdaptor(){
		//we do not cache the client here so that a new client is created when this method
		//is called (According to Subclipse's implementation of SVNKit adaptor).
		Activator activator = Activator.getDefault();
		return activator.getClientAdapter(SvnConstants.CLIENT_ADAPTOR_ID);
	}

	/**
	 * we construct the SVN URL by getting the URL of root and append it
	 * with the full path of the resource.
	 * 
	 * @return a SVN URL for of the resource
	 */
	public static String createSvnUrlFromResource(RepoResource resource) {
		RepoFolderRoot repoFolderRoot = resource.getRoot();
		Repo repo = repoFolderRoot.getRepo();
		String svnUrl;
		//if it is default branch, easy
		if(repoFolderRoot.getBranch().isMaster()){
			svnUrl = repo.getUrl() + resource.getFullPath();
		}
		else{
			//if not, we have to append it with "/branches"
			svnUrl = repo.getUrl() + "/"
					+ SvnConstants.BRANCH_DIRECTORY
					+ resource.getFullPath();
		}
		return svnUrl;
	}

}
