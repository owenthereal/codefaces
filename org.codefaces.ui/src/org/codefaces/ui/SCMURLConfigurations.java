package org.codefaces.ui;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class SCMURLConfigurations {
	
	private static final char PARAMETER_START = '?';
	private static final char EQUAL = '=';
	private static final char AND = '&';
	
	
	private Map<SCMConfigurableElements, String> configurations;

	public SCMURLConfigurations() {
		configurations = new HashMap<SCMConfigurableElements, String>();
	}
	
	public void put(SCMConfigurableElements element, String value){
		configurations.put(element, value);
	}
	
	public String get(SCMConfigurableElements element){
		return configurations.get(element);
	}
	
	public Map<SCMConfigurableElements, String> getConfigurationsMap() {
		return Collections.unmodifiableMap(configurations);
	}
	
	//Map<String, String[]> is the return type of ServletRequest.html#getParameterMap()
	public static SCMURLConfigurations fromHTTPParametersMap(Map<String, String[]> parameters) throws MalformedURLException{
		SCMURLConfigurations configurations = new SCMURLConfigurations();
		for(String key : parameters.keySet()){
			try{
				SCMConfigurableElements element = SCMConfigurableElements.valueOf(key.toUpperCase());
				String[] values = parameters.get(key);
				if(values.length != 1) throw new IllegalArgumentException("Invalid number of value for: " + key);
				
				configurations.put(element, values[0]);
			}
			catch(IllegalArgumentException e){
				throw new MalformedURLException(e.getMessage());
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
		Iterator<Entry<SCMConfigurableElements, String>> iterator = configurations
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<SCMConfigurableElements, String> next = iterator.next(); 
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
