package org.codefaces.core.svn.internal.operations;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

/**
 * An abstract class which represents a general scm url. Notice that it does not
 * support any references (protocol://url/foobar#reference) or query parameters
 * (protocol://url/foobar?query)
 * 
 * this class is immutable
 */
public abstract class SCMUrl {

	private static final String USER_INFO_PATTERN = "(?:([^:]+)(?::(\\S+))?@)?";
	private static final String HOSTNAME_PATTERN = "([^\\/:]+)";
	private static final String PORT_NUMBER_PATTERN = "(?::([0-9]+))?";
	private static final String PATH_PATTERN = "((?:\\/[^\\/]+)*)";
	private static final String OPTIONAL_ENDING_SLASH_PATTERN = "(?:/)?";
	
	private String protocol;
	private String username;
	private String password;
	private String host;
	private int port = -1;
	private String path;

	/**
	 * Parse and return a url object from the given url string. 
	 * The constructor asscept url in the following format:
	 *   protocol://[username[:password]@]host[:port]/path
	 * where protocol is defined in the {@link #getSupportedProtocols} 
	 * method
	 * 
	 * @throws MalformedURLException if the url is malformed 
	 */
	public SCMUrl(String url) throws MalformedURLException{
		if (url == null)
			throw new MalformedURLException("Svn url cannot be null");
		parseUrl(url.trim());
	}
	
	/**
	 * Return a list of supported protocols in a String array. e.g. 
	 * if the url supports the prefixes "https://" and "http://", this
	 * method should return {"http", "https"}
	 * 
	 * @return a list of supported protocols
	 */
	protected abstract String[] getSupportedProtocols();
	
	/**
	 * @return a regular expression pattern for matching URL
	 */
	private Pattern getURLPattern() {
		StringBuilder patternBuilder = new StringBuilder();
		patternBuilder.append(getProtocolPattern());
		patternBuilder.append(USER_INFO_PATTERN);
		patternBuilder.append(HOSTNAME_PATTERN);
		patternBuilder.append(PORT_NUMBER_PATTERN);
		patternBuilder.append(PATH_PATTERN);
		patternBuilder.append(OPTIONAL_ENDING_SLASH_PATTERN);
		return Pattern.compile(patternBuilder.toString());
	}

	/**
	 * @return the protocol regex pattern in an URL
	 */
	private String getProtocolPattern() {
		StringBuilder protocolPatternBuilder = new StringBuilder();
		protocolPatternBuilder.append('(');
		protocolPatternBuilder.append(Pattern.quote(getSupportedProtocols()[0]));
		for(int i=1; i< getSupportedProtocols().length; i++){
			protocolPatternBuilder.append('|');
			protocolPatternBuilder.append(Pattern.quote(getSupportedProtocols()[i]));
		}
		protocolPatternBuilder.append(')');
		protocolPatternBuilder.append(Pattern.quote("://"));
		return protocolPatternBuilder.toString();
	}

	/**
	 * parse the given url 
	 * @param url the url
	 * @throws MalformedURLException if the URL is malformed
	 */
	protected void parseUrl(String url) throws MalformedURLException {
		Matcher matcher = getURLPattern().matcher(url);
		String portString = null;
		if (matcher.matches()) {
			protocol = matcher.group(1);
			username =  matcher.group(2);
			password = matcher.group(3);
			host = matcher.group(4);
			portString = matcher.group(5);
			path = matcher.group(6);
		}
		else{
			throw new MalformedURLException("Invalid URL: " + url);
		}
	
		if(!ArrayUtils.contains(getSupportedProtocols(), protocol)){
			throw new MalformedURLException("Protocol " + protocol + " is not supported");
		}
		
		try{
			if(portString != null){
				port = Integer.parseInt(portString);
			}
		}catch(NumberFormatException e){
			throw new MalformedURLException("Invalid port number: " + portString);
		}
	}

	/**
	 * @return a well formed URL with the given input. 
	 *         Username, password, path can be null. Set the port to negative if it is intended 
	 *         not to be specified in the URL 
	 */
	public static String toUrl(String protocol, String username,
			String password, String host, int port, String path) {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(protocol);
		urlBuilder.append("://");
		if(username != null){
			urlBuilder.append(username);
			if(password != null){
				urlBuilder.append(':');
				urlBuilder.append(password);
			}
			urlBuilder.append('@');
		}
		urlBuilder.append(host);
		if(port > -1){
			urlBuilder.append(':');
			urlBuilder.append(port);
		}
		urlBuilder.append(path);
		return urlBuilder.toString();
	}
	
	/**
	 * @return a well formed URL in string
	 */
	public String toString(){
		return toUrl(protocol, username, password, host, port, path);
	}
	
	/**
	 * @return the user name in the URL
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password in the URL
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the host name in the URL
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the path in the URL. Notice that the path returned by this method
	 *         does not contain a trailing slash, but contains a heading slash if path is not null
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the protocol in the URL
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @return the port number, -1 if the port is not set
	 */
	public int getPort() {
		return port;
	}

}