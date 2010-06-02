package org.codefaces.ui.widgets.ajaxclient;

/**
 * A Enum class for HTTP Method. 
 *  
 * @author kklo
 */
public enum HttpMethod {
	PUT, POST, GET, DELETE;
	
	/**
	 * Return string in lower-case of the HTTP Method
	 */
	@Override
	public String toString(){
		return name().toLowerCase();
	}
}
