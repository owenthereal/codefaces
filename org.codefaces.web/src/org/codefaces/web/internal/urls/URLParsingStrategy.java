package org.codefaces.web.internal.urls;

import org.codefaces.ui.SCMURLConfiguration;

public interface URLParsingStrategy {
	SCMURLConfiguration buildConfigurations(String url);
	
	String getScmKind(); 

	boolean canParse(String url);
}
