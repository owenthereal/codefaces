package org.codefaces.core.services.svn;

import java.text.SimpleDateFormat;

import org.codefaces.core.models.Repo;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;

public class SvnQuery {

	protected ISVNClientAdapter getSvnClient(){
		return SvnUtil.getClientAdaptor();
	}
	
	
	/**
	 * @return an ID for a given SVN Dir entry
	 */
	protected String generateRepoResourceID(Repo repo, ISVNDirEntry entry){
		String url = repo.getId();
		String resourcePath = entry.getPath();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = fmt.format(entry.getLastChangedDate());
		String id = url + ":" + resourcePath + ":" + timestamp;
		return id;
	}
}
