package org.codefaces.ui;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class SCMURLConfiguration {
	
	private static final String ENCODING = "UTF-8";
	private static final char PARAMETER_START = '?';
	private static final char EQUAL = '=';
	private static final char AND = '&';
	
	
	private Map<SCMConfigurableElement, String> configurations;

	public SCMURLConfiguration() {
		configurations = new HashMap<SCMConfigurableElement, String>();
	}
	
	public void put(SCMConfigurableElement element, String value){
		configurations.put(element, value);
	}
	
	public String get(SCMConfigurableElement element){
		return configurations.get(element);
	}
	
	public Map<SCMConfigurableElement, String> getConfigurationsMap() {
		return Collections.unmodifiableMap(configurations);
	}

	/**
	 * Create a SCMURLConfiguration instance from Parameter map. Unknown
	 * parameters are ignored.
	 * 
	 * Map<String, String[]> is the return type of
	 * ServletRequest.html#getParameterMap()
	 * 
	 * @throws MalformedURLException
	 *             if there are unexpected values in known parameters.
	 * 
	 */
	public static SCMURLConfiguration fromHTTPParametersMap(
			Map<String, String[]> parameters) throws MalformedURLException {
		SCMURLConfiguration configurations = new SCMURLConfiguration();
		for(String key : parameters.keySet()){
			try{
				SCMConfigurableElement element = urlDecode(key);
				String[] values = parameters.get(key);
				
				if (values.length != 1){
					throw new MalformedURLException(
							"Invalid number of value for: " + key );
				}
				configurations.put(element, URLDecoder.decode(values[0], ENCODING));
			}
			catch(RuntimeException e){
				//DO Nothing.
				//This exception is thrown from the Enum.valueOf method.
				//We simply ignore unknown Query parameter and keep on doing.
			} catch (UnsupportedEncodingException e) {
				//DO Nothing
				//This exception is thrown if the parameter values cannot be decoded correctly.
				//We ignore this because it may be cause by the end user. 
			}
		}
		return configurations;
	}

	/**
	 * @return an escaped URL query string in "?para1=value1&para2=value2"
	 *         format, or null if no configuration.
	 */
	public String toHTTPQueryString(){
		StringBuilder queryString = new StringBuilder();
		
		if(configurations.isEmpty()){
			return null;
		}
		
		queryString.append(PARAMETER_START);
		Iterator<Entry<SCMConfigurableElement, String>> iterator = configurations
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<SCMConfigurableElement, String> next = iterator.next();
			
			try {
				queryString.append(urlEncode(next.getKey()));
				queryString.append(EQUAL);
				queryString.append(URLEncoder.encode(next.getValue(), ENCODING));
			} catch (UnsupportedEncodingException e) {
				IStatus status = new Status(Status.ERROR,
						CodeFacesUIActivator.PLUGIN_ID,
						"Errors occurs encoding " + next.getValue() , e);
				CodeFacesUIActivator.getDefault().getLog().log(status);
			}
			
			if(iterator.hasNext()){
				queryString.append(AND);
			}
		}
		return queryString.toString();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(Entry<SCMConfigurableElement, String> entry: configurations.entrySet()){
			builder.append(entry.getKey().toString());
			builder.append(": ");
			builder.append(entry.getValue());
			builder.append('\n');
		}
		return builder.toString();
	}
	
	//Encode the SCMConfigurableElement to url parameter name
	public static String urlEncode(SCMConfigurableElement element){
		return element.name().toLowerCase();
	}
	
	//decode the url parameter name to SCMConfigurableElement
	public static SCMConfigurableElement urlDecode(String configElementName){
		return SCMConfigurableElement.valueOf(configElementName.toUpperCase());
	}
}
