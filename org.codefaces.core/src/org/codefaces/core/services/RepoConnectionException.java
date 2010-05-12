package org.codefaces.core.services;

/**
 * Indicate Any Connection Error. This includes HTTP and IO Exceptions 
 * @author kklo
 */
public class RepoConnectionException extends RepoServiceException {

	private static final long serialVersionUID = -4075804895978456530L;

	public RepoConnectionException(){};
	
	public RepoConnectionException(String message) { super(message); } 
	
	public RepoConnectionException(Throwable cause) { super(cause); } 
}
