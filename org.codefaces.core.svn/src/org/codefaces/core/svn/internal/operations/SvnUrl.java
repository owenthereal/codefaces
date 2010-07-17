package org.codefaces.core.svn.internal.operations;

import java.net.MalformedURLException;

public class SvnUrl extends SCMUrl {
	private static final String SVN_PROTOCOL = "svn";
	private static final String SVNSSH_PROTOCOL = "svn+ssh";
	private static final String HTTP_PROTOCOL = "http";
	private static final String HTTPS_PROTOCOL = "https";
	
	private static final String[] SUPPORTED_PROTOCOLS = new String[] {
			SVN_PROTOCOL, SVNSSH_PROTOCOL, HTTP_PROTOCOL, HTTPS_PROTOCOL };
	
	public SvnUrl(String url) throws MalformedURLException {
		super(url);
	}

	@Override
	protected String[] getSupportedProtocols(){
		return SUPPORTED_PROTOCOLS;
	}

}
