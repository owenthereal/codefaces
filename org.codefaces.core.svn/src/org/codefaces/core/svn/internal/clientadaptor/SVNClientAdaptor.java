package org.codefaces.core.svn.internal.clientadaptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.codefaces.core.connectors.SCMIOException;
import org.codefaces.core.connectors.SCMResponseException;
import org.codefaces.core.connectors.SCMURLException;
import org.eclipse.rwt.SessionSingletonBase;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * An abstract layer to adapt the underlining client provider
 */
public class SVNClientAdaptor implements HttpSessionBindingListener {
	//private static final String SEPERATOR = "/";
	private static final DateFormat ISO8601_FORMAT_IN = 
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	private ReentrantLock resourceLock = new ReentrantLock();

	private ReentrantLock repoLock = new ReentrantLock();

	private ReentrantLock listLock = new ReentrantLock();

	/**
	 * no parameter constructor for session singleton use
	 */
	protected SVNClientAdaptor() {
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
		    String urlString;
		    String uuid;
		    long latestRevision;
		    
			SVNRepository repository = createSvnRepository(url, username, password);

			try {
				repoLock.lock();
				urlString = repository.getLocation().toString();
				uuid = repository.getRepositoryUUID(true);
				latestRevision = repository.getLatestRevision();
				svnInfo = new SVNRepoInfo(urlString, uuid, latestRevision);
			} finally {
				repository.closeSession();
				repoLock.unlock();
			}
		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		} catch (SVNException e) {
			throw new SCMResponseException(e.getMessage(), e);
		}

		return svnInfo;
	}

	/**
	 * @return a configured SVNRepository
	 * @throws MalformedURLException if the URL is invalid
	 */
	private SVNRepository createSvnRepository(String url, String username,
			String password) throws MalformedURLException {
		SVNRepository repository = null;
		try{
			SVNURL svnURL = SVNURL.parseURIDecoded(url);
			repository = SVNRepositoryFactory.create(svnURL);
		}catch(SVNException e){
			throw new MalformedURLException();
		}
		String svnUsername = (username == null ? "" : username);
		String svnPassword = (password == null ? "" : password);
		ISVNAuthenticationManager authManager = 
		     SVNWCUtil.createDefaultAuthenticationManager(svnUsername, svnPassword);
		repository.setAuthenticationManager(authManager);
		return repository;
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
		DirEntryInfoCollector entryInfoCollector = new DirEntryInfoCollector();
		
		try {
			SVNRepository repository = createSvnRepository(url, username, password);

			try {
				listLock.lock();
				repository.getDir("", repository.getLatestRevision(), null,
						entryInfoCollector);
				
			} finally {
				listLock.unlock();
				repository.closeSession();
			}
		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		} catch (SVNException e) {
			throw new SCMResponseException(e.getMessage(), e);
		}
		return entryInfoCollector.getEntries().toArray(new SVNDirectoryEntry[0]);
	}

	/**
	 * @return checkout the resource of the given url
	 */
	public SVNResource getResource(String url, String username, String password) {
		SVNResource resource = null;
		SVNProperties fileProperties = new SVNProperties();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			SVNRepository repository = createSvnRepository(url, username, password);
			try {
				resourceLock.lock();
				repository.getFile("",
						repository.getLatestRevision(), fileProperties, stream);
				resource = new SVNResource(fileProperties.getStringValue(SVNProperty.URL),
						fileProperties.getStringValue(SVNProperty.NAME) , 
						fileProperties.getStringValue(SVNProperty.MIME_TYPE),
						SVNProperty.longValue(fileProperties.getStringValue(SVNProperty.WORKING_SIZE)),
						stream.toString(),
						parseDate(fileProperties.getStringValue(SVNProperty.TEXT_TIME)),
						SVNProperty.longValue(fileProperties.getStringValue(SVNProperty.REVISION)));
			
			} finally {
				resourceLock.unlock();
				repository.closeSession();
			}

		} catch (MalformedURLException e) {
			throw new SCMURLException("Invalid repository url: " + url);
		} catch (SVNException e) {
			throw new SCMResponseException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SCMIOException(e.getMessage(), e);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
			}
		}
		return resource;
	}

	/**
	 * @return a session-based instance of SVNClinetAdaptor
	 */
	public static SVNClientAdaptor getCurrent() {
		return (SVNClientAdaptor) SessionSingletonBase
				.getInstance(SVNClientAdaptor.class);
	}
	
	private Date parseDate(String str) {
		if (str == null) {
			return new Date(0);
		}
		// truncate last nanoseconds.
		str = str.substring(0, 23);
		try {
			return ISO8601_FORMAT_IN.parse(str);
		} catch (Throwable e) {
		}
		return new Date(0);
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {

	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		
	}
}
