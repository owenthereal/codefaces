package org.codefaces.core.svn.clientadaptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.codefaces.core.connectors.SCMIOException;
import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.core.connectors.SCMURLException;
import org.codefaces.core.svn.operations.SVNConstants;
import org.eclipse.rwt.SessionSingletonBase;
import org.tigris.subversion.clientadapter.Activator;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * An abstract layer to adapt the underlining client provider
 */
public class SVNClientAdaptor {

	private ISVNClientAdapter client;

	/**
	 * no parameter constructor for session singleton use
	 */
	protected SVNClientAdaptor() {
		Activator activator = Activator.getDefault();
		ISVNClientAdapter client = activator
				.getClientAdapter(SVNConstants.CLIENT_ADAPTOR_ID);
		this.client = client;
	}

	/**
	 * constructor for testing purpose
	 */
	protected SVNClientAdaptor(ISVNClientAdapter client) {
		this.client = client;
	}

	/**
	 * @return underlining client
	 */
	private ISVNClientAdapter getClient() {
		return client;
	}

	/**
	 * @return the info of the given svn repository
	 * @param url
	 *            the URL of the repository
	 * @param username
	 *            user name of the repository, can be null
	 * @param password
	 *            password of the repository, can be null
	 */
	public SVNRepoInfo getRepoInfo(String url, String username, String password) {
		SVNRepoInfo svnInfo = null;
		try {
			SVNUrl svnUrl = new SVNUrl(url);
			ISVNClientAdapter client = getClient();
			
			String svnUsername = (username == null? "" : username);
			String svnPassword = (password == null? "" : password);
			client.setUsername(svnUsername);
			client.setPassword(svnPassword);

			ISVNInfo info = client.getInfo(svnUrl);
			svnInfo = new SVNRepoInfo(info.getUrlString(), info.getUuid(), info
					.getRevision().getNumber());
		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		} catch (SVNClientException e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
		return svnInfo;
	}

	/**
	 * @return the directory entries in the given url
	 * @param url
	 *            the URL of the directory
	 * @param username
	 *            user name of the repository, can be null
	 * @param password
	 *            password of the repository, can be null
	 */
	public SVNDirectoryEntry[] getDirectoryEntries(String url, String username,
			String password) {
		SVNDirectoryEntry[] svnEntries = null;
		try {
			SVNUrl svnUrl = new SVNUrl(url);
			ISVNClientAdapter client = getClient();

			String svnUsername = (username == null? "" : username);
			String svnPassword = (password == null? "" : password);
			client.setUsername(svnUsername);
			client.setPassword(svnPassword);
			
			ISVNDirEntry[] entries = client.getList(svnUrl, SVNRevision.HEAD,
					false);

			svnEntries = new SVNDirectoryEntry[entries.length];
			for (int i = 0; i < entries.length; i++) {
				ISVNDirEntry entry = entries[i];
				svnEntries[i] = new SVNDirectoryEntry(svnUrl + "/"
						+ entry.getPath(), entry.getPath(),
						getResourceKind(entry.getNodeKind()), entry.getSize(),
						entry.getLastChangedDate(), entry
								.getLastChangedRevision().getNumber());
			}

			return svnEntries;

		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		} catch (SVNClientException e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
	}

	/**
	 * @return checkout the resource of the given url
	 */
	public SVNResource getResource(String url, String username, String password) {
		SVNResource resource = null;
		try {
			SVNUrl svnUrl = new SVNUrl(url);
			ISVNClientAdapter client = getClient();
			
			String svnUsername = (username == null? "" : username);
			String svnPassword = (password == null? "" : password);
			client.setUsername(svnUsername);
			client.setPassword(svnPassword);

			ISVNDirEntry entry = client.getDirEntry(svnUrl, SVNRevision.HEAD);
			InputStream stream = client.getContent(svnUrl, SVNRevision.HEAD);
			StringWriter writer = new StringWriter();
			IOUtils.copy(stream, writer);

			resource = new SVNResource(svnUrl + "/" + entry.getPath(),
					entry.getPath(), null, entry.getSize(), writer.toString(),
					entry.getLastChangedDate(), entry.getLastChangedRevision()
							.getNumber());

		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		} catch (SVNClientException e) {
			throw new SCMResponseException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SCMIOException(e.getMessage(), e);
		}
		return resource;
	}

	/**
	 * Map the nodekind of the underlining client to SVNResourceKind
	 */
	private SVNResourceKind getResourceKind(SVNNodeKind nodeKind) {
		if (nodeKind == SVNNodeKind.DIR) {
			return SVNResourceKind.DIRECTORY;
		}
		if (nodeKind == SVNNodeKind.FILE) {
			return SVNResourceKind.FILE;
		}
		return SVNResourceKind.UNKNOWN;
	}

	/**
	 * @return a session-based instance of SVNClinetAdaptor
	 */

	public static SVNClientAdaptor getCurrent() {
		return (SVNClientAdaptor) SessionSingletonBase
				.getInstance(SVNClientAdaptor.class);
	}
}
