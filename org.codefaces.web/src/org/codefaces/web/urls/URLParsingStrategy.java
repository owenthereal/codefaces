package org.codefaces.web.urls;

import org.codefaces.ui.SCMURLConfiguration;

public interface URLParsingStrategy {
	SCMURLConfiguration buildConfigurations(String url);
	
	boolean canParse(String url);
}
