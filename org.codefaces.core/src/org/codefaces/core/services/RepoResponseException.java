package org.codefaces.core.services;

/**
 * Indicate Any Error when parsing Repository Response. 
 * E.g. Unsupported response format.  
 * 
 * @author kklo
 */
public class RepoResponseException extends RepoServiceException {
	private static final long serialVersionUID = 8730504730637125347L;

	public RepoResponseException() {}
	
	public RepoResponseException(String message) { super(message); } 
	
	public RepoResponseException(Throwable cause) { super(cause); } 
}
