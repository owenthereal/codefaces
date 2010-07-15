package org.codefaces.core.svn.connectors;

import java.text.SimpleDateFormat;

import org.codefaces.core.connectors.SCMConnector;
import org.codefaces.core.models.Repo;
import org.codefaces.core.svn.operations.SvnUtil;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;

public class SVNConnector implements SCMConnector {
	private static final String KIND = "Subversion";

	@Override
	public String getKind() {
		return KIND;
	}

	public ISVNClientAdapter getSvnClient() {
		return SvnUtil.getClientAdaptor();
	}
	
	/**
	 * @return an ID for a given SVN Dir entry
	 */
	public String generateRepoResourceID(Repo repo, ISVNDirEntry entry){
		String url = repo.getId();
		String resourcePath = entry.getPath();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = fmt.format(entry.getLastChangedDate());
		String id = url + ":" + resourcePath + ":" + timestamp;
		return id;
	}

}
