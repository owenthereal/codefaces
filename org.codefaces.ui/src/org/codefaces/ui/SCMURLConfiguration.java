package org.codefaces.ui;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class SCMURLConfiguration {
	
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
	
	//Map<String, String[]> is the return type of ServletRequest.html#getParameterMap()
	public static SCMURLConfiguration fromHTTPParametersMap(Map<String, String[]> parameters) throws MalformedURLException{
		SCMURLConfiguration configurations = new SCMURLConfiguration();
		for(String key : parameters.keySet()){
			try{
				SCMConfigurableElement element = SCMConfigurableElement.valueOf(key.toUpperCase());
				String[] values = parameters.get(key);
				if(values.length != 1) throw new MalformedURLException("Invalid number of value for: " + key);
				
				configurations.put(element, values[0]);
			}
			catch(IllegalArgumentException e){
				//DO Nothing.
				//This exception is thrown from the Enum.valueOf method.
				//We simply ignore unknown Query parameter and keep on doing.
			}
		}
		return configurations;
	}
	
	//return null if no config in the configurations
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
			queryString.append(next.getKey().toString().toLowerCase());
			queryString.append(EQUAL);
			queryString.append(next.getValue());
			if(iterator.hasNext()){
				queryString.append(AND);
			}
		}
		return queryString.toString();
	}
}
