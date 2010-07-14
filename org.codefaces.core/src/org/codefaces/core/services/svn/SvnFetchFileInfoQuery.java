package org.codefaces.core.services.svn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.codefaces.core.models.Repo;
import org.codefaces.core.models.RepoFile;
import org.codefaces.core.models.RepoFileInfo;
import org.codefaces.core.models.RepoFolder;
import org.codefaces.core.services.SCMQuery;
import org.codefaces.core.services.SCMQueryParameter;
import org.codefaces.httpclient.SCMHttpClient;
import org.eclipse.core.runtime.Assert;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.ISVNProperty;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

public class SvnFetchFileInfoQuery extends SvnQuery implements
		SCMQuery<RepoFileInfo> {

	@Override
	public RepoFileInfo execute(SCMHttpClient client,
			SCMQueryParameter parameter) {
		Object file = parameter.getParameter(PARA_REPO_FILE);
		Assert.isTrue(file instanceof RepoFile);
		RepoFile repoFile = (RepoFile) file;
		
		String url = SvnUtil.createSvnUrlFromResource(repoFile);
		ISVNClientAdapter svnClient = getSvnClient();
		
		try {
			SVNUrl svnUrl = new SVNUrl(url);
			//System.out.println(svnUrl);
			//ISVNProperty[] properties = svnClient.getProperties(svnUrl);
			//System.out.println(properties[0].getName());
			/*for(ISVNProperty property: properties){
				System.out.println(property.getName());
			}*/
			
			InputStream stream = svnClient.getContent(svnUrl, SVNRevision.HEAD);
			StringWriter writer = new StringWriter();
			IOUtils.copy(stream, writer);
			return new RepoFileInfo(writer.toString(),
					null, null, -1);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SVNClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
