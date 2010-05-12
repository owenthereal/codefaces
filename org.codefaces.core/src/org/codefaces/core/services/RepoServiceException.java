package org.codefaces.core.services;

public class RepoServiceException extends Exception {
	private static final long serialVersionUID = -4979080649956075092L;

	public RepoServiceException() {}
	
	public RepoServiceException(String message) { super(message); } 
	
	public RepoServiceException(Throwable cause) { super(cause); } 
}
